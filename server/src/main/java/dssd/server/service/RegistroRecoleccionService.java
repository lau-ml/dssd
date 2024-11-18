package dssd.server.service;

import com.fasterxml.jackson.core.JsonProcessingException;

import dssd.server.DTO.DetalleRegistroDTO;
import dssd.server.DTO.RegistroRecoleccionDTO;
import dssd.server.exception.RegistroPendienteException;
import dssd.server.exception.UsuarioInvalidoException;
import dssd.server.helpers.*;
import dssd.server.model.DetalleRegistro;
import dssd.server.model.Material;
import dssd.server.model.RegistroRecoleccion;
import dssd.server.model.Usuario;
import dssd.server.repository.DetalleRegistroRepository;
import dssd.server.repository.MaterialRepository;
import dssd.server.repository.RegistroRecoleccionRepository;
import dssd.server.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.List;

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
    private BonitaState bonitaState;

    @Transactional
    public RegistroRecoleccion obtenerRegistro()
            throws RegistroPendienteException, RuntimeException, UsuarioInvalidoException {

        Usuario recolector = userService.recuperarUsuario();

        Optional<RegistroRecoleccion> registroNoCompletadoOpt = registroRecoleccionRepository
                .findTopByRecolectorAndCompletadoFalseOrderByFechaRecoleccionDesc(recolector);
        registroRecoleccionRepository.findTopByRecolectorOrderByFechaRecoleccionDesc(recolector).ifPresent(registro -> {
            if (registro.isCompletado() && !registro.isVerificado()) {
                String centroRecoleccion = recolector.getCentroRecoleccion().getNombre();
                String mensaje = String.format(
                        "Ya tienes un registro completado sin verificar. Por favor, acércate a tu centro de recolección asignado: %s.",
                        centroRecoleccion);
                throw new RegistroPendienteException(mensaje,
                        "REGISTRO_PENDIENTE");
            }
        });
        return registroNoCompletadoOpt.get();
    }

    @Transactional

    public RegistroRecoleccion completarRegistroRecoleccion(Long id) throws JsonProcessingException {
        RegistroRecoleccion registroRecoleccion = registroRecoleccionRepository.findById(id).orElseThrow();
        registroRecoleccion.setCompletado(true);
        this.bonitaState.cargarActividadBonita();
        this.bonitaState.set_recoleccion_confirmar();
        this.bonitaState.asignarActividadBonita();
        this.bonitaState.completarActividadBonita();
        return registroRecoleccionRepository.save(registroRecoleccion);
    }

    @Transactional
    public void eliminarRegistroRecoleccion(Long id) throws JsonProcessingException {
        this.bonitaState.cargarActividadBonita();
        this.bonitaState.set_recoleccion_cancelar();
        this.bonitaState.asignarActividadBonita();
        this.bonitaState.completarActividadBonita();
        detalleRegistroRepository.deleteByRegistroRecoleccion(registroRecoleccionRepository.findById(id).get());
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

        for (DetalleRegistroDTO detalleDTO : registroRecoleccionDTO.getDetalleRegistros()) {

            Long materialId = detalleDTO.getMaterial().getId();

            Material material = materialRepository.findById(materialId)
                    .orElseThrow(
                            () -> new IllegalArgumentException("Material no encontrado para el ID: " + materialId));

            List<DetalleRegistro> detallesCoincidentes = registroRecoleccion.getDetalleRegistros().stream()
                    .filter(detalle -> detalle.getMaterial() != null &&
                            detalle.getMaterial().getId().equals(materialId))
                    .collect(Collectors.toList());

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
        }
        registroRecoleccion.setVerificado(true);

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
