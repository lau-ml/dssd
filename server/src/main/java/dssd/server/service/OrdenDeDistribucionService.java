package dssd.server.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import dssd.server.exception.UsuarioInvalidoException;
import dssd.server.helpers.BonitaState;
import dssd.server.model.*;
import dssd.server.repository.CantidadMaterialRepository;
import dssd.server.repository.CentroRecoleccionRepository;
import dssd.server.repository.MaterialRepository;
import dssd.server.requests.OrdenRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import dssd.server.DTO.OrdenDeDistribucionDTO;
import dssd.server.DTO.PaginatedResponseDTO;
import dssd.server.repository.OrdenDeDistribucionRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrdenDeDistribucionService {
    @Autowired
    private OrdenDeDistribucionRepository ordenDeDistribucionRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private CentroRecoleccionRepository centroRecoleccionRepository;

    @Autowired
    private CantidadMaterialRepository stockRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private BonitaState bonitaState;


    public PaginatedResponseDTO<OrdenDeDistribucionDTO> obtenerOrdenesPaginadasYFiltradas(
            Pageable pageable, String search, String estadoStr, Long centroRecoleccionId) {
        Page<OrdenDeDistribucion> ordenesPage;
        OrdenDeDistribucion.EstadoOrden estado = null;

        if (estadoStr != null && !estadoStr.trim().isEmpty()) {
            try {
                estado = OrdenDeDistribucion.EstadoOrden.valueOf(estadoStr);
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Estado de orden no válido: " + estadoStr);
            }
        }

        if ((search != null && !search.trim().isEmpty()) && estado != null) {
            ordenesPage = ordenDeDistribucionRepository
                    .findByCentroRecoleccionIdAndMaterialNombreContainingIgnoreCaseAndEstado(
                            centroRecoleccionId, search, estado, pageable);
        } else if (search != null && !search.trim().isEmpty()) {
            ordenesPage = ordenDeDistribucionRepository
                    .findByCentroRecoleccionIdAndMaterialNombreContainingIgnoreCase(
                            centroRecoleccionId, search, pageable);
        } else if (estado != null) {
            ordenesPage = ordenDeDistribucionRepository
                    .findByCentroRecoleccionIdAndEstado(centroRecoleccionId, estado, pageable);
        } else {
            ordenesPage = ordenDeDistribucionRepository
                    .findByCentroRecoleccionId(centroRecoleccionId, pageable);
        }

        List<OrdenDeDistribucionDTO> ordenesDTOs = ordenesPage.getContent().stream()
                .map(OrdenDeDistribucionDTO::new)
                .collect(Collectors.toList());

        return new PaginatedResponseDTO<>(
                ordenesDTOs,
                ordenesPage.getTotalPages(),
                ordenesPage.getTotalElements(),
                pageable.getPageNumber(),
                pageable.getPageSize());
    }

    @Transactional
    public void cambiarEstado(Long id, OrdenDeDistribucion.EstadoOrden nuevoEstado) throws UsuarioInvalidoException {
        // Obtener la orden
        OrdenDeDistribucion orden = obtenerOrdenPorId(id);
        // Obtener el usuario logueado
        Usuario usuarioLogueado = userService.recuperarUsuario();

        // Verificar si la transición es válida
        if (esTransicionValida(orden.getEstado(), nuevoEstado)) {
            cambiarEstadoOrden(orden, nuevoEstado);
        } else {
            // Verificar si el estado es ACEPTADA o RECHAZADO y si el usuario tiene permisos
            if (esEstadoAceptadoRechazado(nuevoEstado) && tienePermisosParaCambiarEstado(usuarioLogueado)) {
                cambiarEstadoOrden(orden, nuevoEstado);
            } else {
                throw new IllegalStateException("Transición de estado no permitida");
            }
        }
    }

    // Método para obtener la orden por ID
    private OrdenDeDistribucion obtenerOrdenPorId(Long id) {
        return ordenDeDistribucionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada con ID: " + id));
    }

    // Método para cambiar el estado de la orden y guardarla
    @Transactional
    void cambiarEstadoOrden(OrdenDeDistribucion orden, OrdenDeDistribucion.EstadoOrden nuevoEstado) throws UsuarioInvalidoException {
        bonitaState.cambiarOrdenEstado(orden, orden.getEstado(), nuevoEstado);
        orden.setEstado(nuevoEstado);
        if (nuevoEstado == OrdenDeDistribucion.EstadoOrden.RECHAZADO) {
            CantidadMaterial cantidadMaterial= stockRepository.findByCentroRecoleccionAndMaterial(orden.getCentroRecoleccion(),orden.getMaterial()).get();
            cantidadMaterial.setCantidad(cantidadMaterial.getCantidad()+orden.getCantidad());
            stockRepository.save(cantidadMaterial);
        }
        ordenDeDistribucionRepository.save(orden);
    }

    // Método para verificar si el estado es ACEPTADO o RECHAZADO
    private boolean esEstadoAceptadoRechazado(OrdenDeDistribucion.EstadoOrden nuevoEstado) {
        return nuevoEstado == OrdenDeDistribucion.EstadoOrden.ACEPTADA ||
                nuevoEstado == OrdenDeDistribucion.EstadoOrden.RECHAZADO||
                nuevoEstado == OrdenDeDistribucion.EstadoOrden.ENTREGADA
                ;
    }

    // Método para verificar si el usuario tiene permisos para cambiar el estado
    private boolean tienePermisosParaCambiarEstado(Usuario usuarioLogueado) {
        return usuarioLogueado.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("PERMISO_ACEPTAR_ORDENES_DISTRIBUCION") ||
                        authority.getAuthority().equals("PERMISO_RECHAZAR_ORDENES_DISTRIBUCION")||
                        authority.getAuthority().equals("PERMISO_ENTREGAR_ORDENES_DISTRIBUCION") );
    }

    // Método para verificar si la transición es válida
    private boolean esTransicionValida(OrdenDeDistribucion.EstadoOrden estadoActual, OrdenDeDistribucion.EstadoOrden nuevoEstado) {
        Map<OrdenDeDistribucion.EstadoOrden, List<OrdenDeDistribucion.EstadoOrden>> transicionesValidas = Map.of(
                OrdenDeDistribucion.EstadoOrden.ACEPTADA, List.of(OrdenDeDistribucion.EstadoOrden.EN_PREPARACION),
                OrdenDeDistribucion.EstadoOrden.EN_PREPARACION, List.of(OrdenDeDistribucion.EstadoOrden.PREPARADO),
                OrdenDeDistribucion.EstadoOrden.PREPARADO, List.of(OrdenDeDistribucion.EstadoOrden.ENVIADO)
        );

        return transicionesValidas.getOrDefault(estadoActual, List.of()).contains(nuevoEstado);
    }



    public OrdenDeDistribucion crearOrden(OrdenRequest request) {
        Material material=materialRepository.findById(request.getMaterialId())
                .orElseThrow(() -> new IllegalArgumentException("Material no encontrado con ID: " + request.getMaterialId()));
        CentroRecoleccion centroRecoleccion = centroRecoleccionRepository.findByEmail(request.getCentroRecoleccionEmail());

        OrdenDeDistribucion orden = new OrdenDeDistribucion();
        orden.setDeposito(request.getDeposito());
        orden.setCantidad(request.getCantidad());
        orden.setMaterial(material);
        orden.setCentroRecoleccion(centroRecoleccion);
        orden.setEstado(OrdenDeDistribucion.EstadoOrden.PENDIENTE_DE_ACEPTAR);
        return ordenDeDistribucionRepository.save(orden);
    }

    public void asociarOrdenBonita(Long idOrden, Long idProceso, Long idRootProceso) throws UsuarioInvalidoException {
        OrdenDeDistribucion orden = ordenDeDistribucionRepository.findById(idOrden)
                .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada con ID: " + idOrden));
        bonitaState.asociarOrdenBonita(orden, idProceso, idRootProceso);
    }
}
