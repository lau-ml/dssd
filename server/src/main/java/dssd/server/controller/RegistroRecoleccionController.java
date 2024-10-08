package dssd.server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import dssd.server.exception.UsuarioInvalidoException;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import dssd.server.DTO.RegistroRecoleccionDTO;
import dssd.server.exception.RegistroPendienteException;
import dssd.server.model.RegistroRecoleccion;
import dssd.server.service.RegistroRecoleccionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/collection-record")
@CrossOrigin(origins = "http://localhost:4200")
@Secured("ROLE_recolector")
public class RegistroRecoleccionController {

    @Autowired
    private RegistroRecoleccionService registroRecoleccionService;

    @GetMapping("/collector/{collectorId}")
    public ResponseEntity<?> obtenerRegistro(@PathVariable Long collectorId) {
        try {
            RegistroRecoleccion registroRecoleccion = registroRecoleccionService.obtenerRegistro();
            return ResponseEntity.ok(new RegistroRecoleccionDTO(registroRecoleccion));
        } catch (RegistroPendienteException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (UsuarioInvalidoException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<?> completarRegistroRecoleccion(@PathVariable Long id) {
        try {

            return ResponseEntity.ok(registroRecoleccionService.completarRegistroRecoleccion(id));
        } catch (RuntimeException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarRegistroRecoleccion(@PathVariable Long id) {
        try {
            registroRecoleccionService.eliminarRegistroRecoleccion(id);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
