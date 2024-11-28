package dssd.server.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import dssd.server.DTO.DetalleRegistroDTO;
import dssd.server.DTO.RegistroRecoleccionDTO;
import dssd.server.exception.RegistroPendienteException;
import dssd.server.exception.UsuarioInvalidoException;
import dssd.server.helpers.BonitaState;
import dssd.server.helpers.*;
import dssd.server.model.CentroRecoleccion;
import dssd.server.model.DetalleRegistro;
import dssd.server.model.Material;
import dssd.server.model.Pago;
import dssd.server.model.RegistroRecoleccion;
import dssd.server.model.Usuario;
import dssd.server.repository.DetalleRegistroRepository;
import dssd.server.repository.MaterialRepository;
import dssd.server.repository.PagoRepository;
import dssd.server.repository.RegistroRecoleccionRepository;
import dssd.server.repository.TareaBonitaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class RegistroRecoleccionService {
    @Autowired
    private RegistroRecoleccionRepository registroRecoleccionRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private DetalleRegistroRepository detalleRegistroRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private PagoRepository pagoRepository;

    @Autowired
    private StockMaterialService stockMaterialService;

    @Autowired
    private BonitaState bonitaState;

    @Autowired
    private TareaBonitaRepository tareaBonitaRepository;

    @Transactional
    public RegistroRecoleccion obtenerMiUltimoRegistro()
            throws RegistroPendienteException, RuntimeException, UsuarioInvalidoException {

        Usuario recolector = userService.recuperarUsuario();
        Optional<RegistroRecoleccion> ultimoRegistroOpt = registroRecoleccionRepository
                .findTopByRecolectorOrderByFechaRecoleccionDesc(recolector);

        if (ultimoRegistroOpt.isPresent()) {
            RegistroRecoleccion ultimoRegistro = ultimoRegistroOpt.get();
            if (ultimoRegistro.isCompletado() && !ultimoRegistro.isVerificado()) {
                String centroRecoleccion = Optional.ofNullable(recolector.getCentroRecoleccion())
                        .map(CentroRecoleccion::getNombre)
                        .orElse("sin asignar");

                String mensaje = String.format(
                        "Ya tienes un registro completado sin verificar. Por favor, acércate a tu centro de recolección asignado: %s.",
                        centroRecoleccion);
                System.err.println("Lanzando excepción: " + mensaje);
                throw new RegistroPendienteException(mensaje, "REGISTRO_PENDIENTE");
            } else if (!ultimoRegistro.isCompletado()) {
                return ultimoRegistro;
            }
        }

        RegistroRecoleccion nuevoRegistro = new RegistroRecoleccion();
        nuevoRegistro.setRecolector(recolector);
        nuevoRegistro.setIdCentroRecoleccion(recolector.getCentroRecoleccion().getId());
        nuevoRegistro.setCompletado(false);
        registroRecoleccionRepository.save(nuevoRegistro);

        return nuevoRegistro;

    }

    @Transactional
    public RegistroRecoleccionDTO obtenerRegistro(Long registroId) {
        RegistroRecoleccion registroRecoleccion = registroRecoleccionRepository.findById(registroId).orElseThrow();

        return new RegistroRecoleccionDTO(registroRecoleccion);
    }

    @Transactional
    public RegistroRecoleccionDTO obtenerRegistroSiPertenece(Long registroId) throws UsuarioInvalidoException {
        Usuario recolector = userService.recuperarUsuario();
        RegistroRecoleccion registroRecoleccion = registroRecoleccionRepository.findById(registroId)
                .orElseThrow(() -> new NoSuchElementException("Registro no encontrado"));

        if (!registroRecoleccion.getRecolector().equals(recolector)) {
            throw new SecurityException("El registro no pertenece al usuario actual.");
        }

        return new RegistroRecoleccionDTO(registroRecoleccion);
    }

    @Transactional
    public RegistroRecoleccion completarRegistroRecoleccion(Long id) throws JsonProcessingException, UsuarioInvalidoException {
        RegistroRecoleccion registroRecoleccion = registroRecoleccionRepository.findById(id).orElseThrow();
        this.bonitaState.confirmarRegistroRecoleccionBonita(registroRecoleccion);
        registroRecoleccion.setCompletado(true);
        return registroRecoleccionRepository.save(registroRecoleccion);
    }

    @Transactional
    public void eliminarRegistroRecoleccion(Long id) throws JsonProcessingException, UsuarioInvalidoException {
        RegistroRecoleccion registroRecoleccion = registroRecoleccionRepository.findById(id).get();
        this.bonitaState.eliminarRegistroBonita(registroRecoleccion);
        this.tareaBonitaRepository.deleteByRegistroRecoleccion(registroRecoleccion);
        registroRecoleccionRepository.deleteById(id);

    }

    @Transactional
    public RegistroRecoleccion materialesEntregadosDelRecolector(RegistroRecoleccionDTO registroRecoleccionDTO)
            throws RegistroPendienteException, UsuarioInvalidoException {

        if (registroRecoleccionDTO.getIdRecolector() == null || registroRecoleccionDTO.getIdRecolector() <= 0) {
            throw new IllegalArgumentException("Falta indicar el id del recolector");
        }

        RegistroRecoleccion registroRecoleccion = obtenerRegistroSinValidar(registroRecoleccionDTO.getIdRecolector());

        Usuario empleado = userService.recuperarUsuario();
        if (empleado.getCentroRecoleccion() == null ||
                !empleado.getCentroRecoleccion()
                        .equals(registroRecoleccion.getRecolector().getCentroRecoleccion())) {
            throw new UsuarioInvalidoException(
                    "El usuario logueado no pertenece al mismo centro de recolección que el recolector.");
        }

        boolean tieneMaterialValido = registroRecoleccionDTO.getDetalleRegistros().stream()
                .anyMatch(detalle -> detalle.getMaterial() != null && detalle.getMaterial().getId() != null
                        && detalle.getMaterial().getId() > 0);
        if (!tieneMaterialValido) {
            throw new IllegalArgumentException("Materiales no válidos.");
        }

        double totalPago = 0;
        for (DetalleRegistroDTO detalleDTO : registroRecoleccionDTO.getDetalleRegistros()) {

            Long materialId = detalleDTO.getMaterial().getId();

            Material material = materialRepository.findById(materialId)
                    .orElseThrow(
                            () -> new IllegalArgumentException("Material no encontrado para el ID: " + materialId));

            List<DetalleRegistro> detallesCoincidentes = registroRecoleccion.getDetalleRegistros().stream()
                    .filter(detalle -> detalle.getMaterial() != null &&
                            detalle.getMaterial().getId().equals(materialId))
                    .toList();

            this.stockMaterialService.agregarStockMaterial(empleado.getCentroRecoleccion().getId(), materialId,
                    detalleDTO.getCantidadRecibida());

            if (!detallesCoincidentes.isEmpty()) {
                int cantidadRecibida = detalleDTO.getCantidadRecibida();
                for (DetalleRegistro detalleCoincidente : detallesCoincidentes) {
                    if (cantidadRecibida > detalleCoincidente.getCantidadRecolectada()) {
                        detalleCoincidente.setCantidadRecibida(detalleCoincidente.getCantidadRecolectada());
                        cantidadRecibida -= detalleCoincidente.getCantidadRecolectada();
                    } else {
                        detalleCoincidente.setCantidadRecibida(cantidadRecibida);
                        cantidadRecibida = 0;
                        break;
                    }

                }

                if (cantidadRecibida > 0) {
                    DetalleRegistro nuevoDetalle = new DetalleRegistro();
                    nuevoDetalle.setMaterial(material);
                    nuevoDetalle.setCantidadRecibida(cantidadRecibida);
                    registroRecoleccion.getDetalleRegistros().add(nuevoDetalle);
                }
            } else {
                DetalleRegistro nuevoDetalle = new DetalleRegistro();
                nuevoDetalle.setMaterial(material);
                nuevoDetalle.setCantidadRecibida(detalleDTO.getCantidadRecibida());
                registroRecoleccion.getDetalleRegistros().add(nuevoDetalle);
            }

            double pagoDetalle = detalleDTO.getCantidadRecibida() * material.getPrecio();
            totalPago += pagoDetalle;
        }
        registroRecoleccion.setVerificado(true);
        bonitaState.completarActividadRecepcionBonita(registroRecoleccion);

        Pago pago = new Pago();
        pago.setRegistroRecoleccion(registroRecoleccion);
        pago.setMonto(totalPago);
        pago.setRegistroRecoleccion(registroRecoleccion);
        pago.setEstado(Pago.EstadoPago.PENDIENTE);

        pagoRepository.save(pago);

        return registroRecoleccionRepository.save(registroRecoleccion);
}

@Transactional
public RegistroRecoleccion obtenerRegistroSinValidar(Long idRecolector)
        throws RegistroPendienteException {

    Optional<RegistroRecoleccion> registroOpt = registroRecoleccionRepository
            .findTopByRecolectorIdAndCompletadoTrueAndVerificadoFalseOrderByFechaRecoleccionDesc(idRecolector);

    if (!registroOpt.isPresent()) {
        throw new RegistroPendienteException(
                "El recolector no tiene registros pendientes de verificación en el sistema. El recolector debe asegúrese de que se haya creado y enviado el registro de recolección antes de proceder.",
                "NO_REGISTRO_PENDIENTE");
    }

    return registroOpt.get();
}
}
