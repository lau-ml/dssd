package dssd.server.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import dssd.server.model.CentroRecoleccion;
import dssd.server.model.Material;
import dssd.server.repository.CentroRecoleccionRepository;
import dssd.server.repository.MaterialRepository;
import dssd.server.requests.OrdenRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import dssd.server.DTO.OrdenDeDistribucionDTO;
import dssd.server.DTO.PaginatedResponseDTO;
import dssd.server.model.OrdenDeDistribucion;
import dssd.server.repository.OrdenDeDistribucionRepository;

@Service
public class OrdenDeDistribucionService {
    @Autowired
    private OrdenDeDistribucionRepository ordenDeDistribucionRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private CentroRecoleccionRepository centroRecoleccionRepository;

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

    public void cambiarEstado(Long id, OrdenDeDistribucion.EstadoOrden nuevoEstado) {
        OrdenDeDistribucion orden = ordenDeDistribucionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Orden no encontrada con ID: " + id));

        if (esTransicionValida(orden.getEstado(), nuevoEstado)) {
            orden.setEstado(nuevoEstado);
            ordenDeDistribucionRepository.save(orden);
        } else {
            throw new IllegalStateException("Transición de estado no permitida");
        }
    }

    private boolean esTransicionValida(OrdenDeDistribucion.EstadoOrden estadoActual,
            OrdenDeDistribucion.EstadoOrden nuevoEstado) {
        Map<OrdenDeDistribucion.EstadoOrden, List<OrdenDeDistribucion.EstadoOrden>> transicionesValidas = Map.of(
                OrdenDeDistribucion.EstadoOrden.ACEPTADA, List.of(OrdenDeDistribucion.EstadoOrden.EN_PREPARACION),
                OrdenDeDistribucion.EstadoOrden.EN_PREPARACION, List.of(OrdenDeDistribucion.EstadoOrden.PREPARADO),
                OrdenDeDistribucion.EstadoOrden.PREPARADO, List.of(OrdenDeDistribucion.EstadoOrden.ENVIADO));

        return transicionesValidas.getOrDefault(estadoActual, List.of()).contains(nuevoEstado);
    }

    /*
    public OrdenDeDistribucion(String deposito, String proceso, Integer cantidad, CentroRecoleccion centroRecoleccion,
            Material material, EstadoOrden estado) {
        this.deposito = deposito;
        this.proceso = proceso;
        this.cantidad = cantidad;
        this.centroRecoleccion = centroRecoleccion;
        this.material = material;
        this.estado = estado;
    }
    * */
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
}
