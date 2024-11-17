package dssd.server.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

import dssd.server.exception.SolicitudVinculacionPuntoRecoleccionException;
import dssd.server.exception.UsuarioInvalidoException;
import dssd.server.model.PuntoDeRecoleccion;
import dssd.server.model.SolicitudVinculacionPuntoRecoleccion;
import dssd.server.model.Usuario;
import dssd.server.repository.PuntoDeRecoleccionRepository;
import dssd.server.repository.SolicitudVinculacionPuntoRecoleccionRepository;
import dssd.server.repository.UsuarioRepository;

@Service
public class SolicitudVinculacionPuntoRecoleccionService {

    @Autowired
    private SolicitudVinculacionPuntoRecoleccionRepository solicitudRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PuntoDeRecoleccionRepository puntoDeRecoleccionRepository;

    @Autowired
    private UserService userService;

    public SolicitudVinculacionPuntoRecoleccion crearSolicitud(Long puntoDeRecoleccionId)
            throws UsuarioInvalidoException {
        Usuario recolector = userService.recuperarUsuario();

        PuntoDeRecoleccion puntoDeRecoleccion = puntoDeRecoleccionRepository.findById(puntoDeRecoleccionId)
                .orElseThrow(() -> new SolicitudVinculacionPuntoRecoleccionException(
                        "El punto de recolección no existe.", "INVALID_DATA"));

        Optional<SolicitudVinculacionPuntoRecoleccion> existingSolicitud = solicitudRepository
                .findByRecolectorAndPuntoDeRecoleccion(recolector, puntoDeRecoleccion);

        if (existingSolicitud.isPresent()) {
            throw new SolicitudVinculacionPuntoRecoleccionException(
                    "El recolector ya ha solicitado vincularse a este punto de recolección.", "REQUEST_ALREADY_EXISTS");
        }

        SolicitudVinculacionPuntoRecoleccion solicitud = new SolicitudVinculacionPuntoRecoleccion(puntoDeRecoleccion,
                recolector, SolicitudVinculacionPuntoRecoleccion.EstadoSolicitud.PENDIENTE);

        return solicitudRepository.save(solicitud);
    }

    public List<SolicitudVinculacionPuntoRecoleccion> obtenerSolicitudesPendientes() {
        return solicitudRepository.findByEstado(SolicitudVinculacionPuntoRecoleccion.EstadoSolicitud.PENDIENTE);
    }

    public SolicitudVinculacionPuntoRecoleccion gestionarSolicitud(Long solicitudId,
            SolicitudVinculacionPuntoRecoleccion.EstadoSolicitud estado) {
        Optional<SolicitudVinculacionPuntoRecoleccion> solicitudOpt = solicitudRepository.findById(solicitudId);

        if (solicitudOpt.isEmpty()) {
            throw new IllegalArgumentException("Solicitud no encontrada");
        }

        SolicitudVinculacionPuntoRecoleccion solicitud = solicitudOpt.get();
        solicitud.setEstado(estado);

        return solicitudRepository.save(solicitud);
    }

    public List<SolicitudVinculacionPuntoRecoleccion> obtenerSolicitudesPendientesPorRecolector(Long recolectorId) {
        return solicitudRepository.findByRecolector_IdAndEstado(recolectorId,
                SolicitudVinculacionPuntoRecoleccion.EstadoSolicitud.PENDIENTE);
    }
}
