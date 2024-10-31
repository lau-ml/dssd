package dssd.server.controller;

import dssd.server.DTO.UsuarioDTO;
import dssd.server.exception.UsuarioInvalidoException;
import dssd.server.service.BonitaService;
import dssd.server.service.RecolectorService;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
@RequestMapping("/api/collector")
@CrossOrigin(origins = "http://localhost:4200")
public class RecolectorController {

    @Autowired
    private RecolectorService recolectorService;

    @Autowired
    BonitaService bonitaService;

    @PreAuthorize("hasAuthority('PERMISO_VER_RECOLECTORES')")
    @GetMapping("/collectors")
    public ResponseEntity<?> listarRecolectores() {
        try {
            List<UsuarioDTO> recolectores = recolectorService.obtenerRecolectoresDelCentroDelUsuarioActual();

            return ResponseEntity.ok(recolectores);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (JsonProcessingException | UsuarioInvalidoException e) {
            throw new RuntimeException(e);
        }
    }
}
