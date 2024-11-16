package dssd.server.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import dssd.server.DTO.PuntoDeRecoleccionDTO;
import dssd.server.exception.UsuarioInvalidoException;
import dssd.server.model.PuntoDeRecoleccion;
import dssd.server.service.PuntoDeRecoleccionService;

@RestController
@RequestMapping("/api/location")
@CrossOrigin(origins = "http://localhost:4200")
public class PuntoDeRecoleccionController {
    @Autowired
    private PuntoDeRecoleccionService puntoDeRecoleccionService;

    @PreAuthorize("hasAuthority('PERMISO_VER_MIS_PUNTOS_RECOLECCIONES')")
    @GetMapping("/get-locations")
    public ResponseEntity<?> obtenerPuntosDeRecoleccionRecolector() {
        try {
            List<PuntoDeRecoleccion> puntosDeRecoleccions = puntoDeRecoleccionService
                    .obtenerPuntosDeRecoleccionPorRecolector();
            List<PuntoDeRecoleccionDTO> puntoDeRecoleccionDTOs = puntosDeRecoleccions.stream()
                    .map(PuntoDeRecoleccionDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(puntoDeRecoleccionDTOs);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (JsonProcessingException | UsuarioInvalidoException e) {
            throw new RuntimeException(e);
        }
    }
}
