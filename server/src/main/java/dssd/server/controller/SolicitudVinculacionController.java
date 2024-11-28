package dssd.server.controller;

import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import dssd.server.exception.UsuarioInvalidoException;
import dssd.server.model.SolicitudVinculacionPuntoRecoleccion;
import dssd.server.service.SolicitudVinculacionPuntoRecoleccionService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/solicitudes-vinculacion")
public class SolicitudVinculacionController {

    @Autowired
    private SolicitudVinculacionPuntoRecoleccionService solicitudService;

    @PostMapping
    public ResponseEntity<?> crearSolicitud(
            @RequestParam Long puntoDeRecoleccionId) {
        try {
            solicitudService.crearSolicitud(puntoDeRecoleccionId);

            Map<String, String> response = new HashMap<>();
            response.put("mensaje", "Solicitud enviada con exito.");
            response.put("codigoError", "SOLICITUD_EXITOSA");

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (UsuarioInvalidoException e) {
            throw new RuntimeException(e);
        }
    }

    @GetMapping("/pendientes")
    public ResponseEntity<List<SolicitudVinculacionPuntoRecoleccion>> obtenerSolicitudesPendientes() {
        List<SolicitudVinculacionPuntoRecoleccion> solicitudes = solicitudService.obtenerSolicitudesPendientes();
        return ResponseEntity.ok(solicitudes);
    }

    // Gestionar una solicitud (aceptar o rechazar)
    @PutMapping("/{id}")
    public ResponseEntity<SolicitudVinculacionPuntoRecoleccion> gestionarSolicitud(
            @PathVariable Long id,
            @RequestParam SolicitudVinculacionPuntoRecoleccion.EstadoSolicitud estado) {
        SolicitudVinculacionPuntoRecoleccion solicitud = solicitudService.gestionarSolicitud(id, estado);
        return ResponseEntity.ok(solicitud);
    }

    // Obtener solicitudes pendientes de un recolector
    @GetMapping("/pendientes/recolector/{recolectorId}")
    public ResponseEntity<List<SolicitudVinculacionPuntoRecoleccion>> obtenerSolicitudesPendientesPorRecolector(
            @PathVariable Long recolectorId) {
        List<SolicitudVinculacionPuntoRecoleccion> solicitudes = solicitudService
                .obtenerSolicitudesPendientesPorRecolector(recolectorId);
        return ResponseEntity.ok(solicitudes);
    }
}
