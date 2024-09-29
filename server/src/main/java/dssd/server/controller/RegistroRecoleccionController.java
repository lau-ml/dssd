package dssd.server.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dssd.server.DTO.RegistroRecoleccionDTO;
import dssd.server.exception.RegistroPendienteException;
import dssd.server.model.RegistroRecoleccion;
import dssd.server.service.RegistroRecoleccionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/collection-record")
@CrossOrigin(origins = "http://localhost:4200")
public class RegistroRecoleccionController {

    @Autowired
    private RegistroRecoleccionService registroRecoleccionService;

    @GetMapping("/collector/{collectorId}")
    public ResponseEntity<?> obtenerOcrearRegistro(@PathVariable Long collectorId) {
        try {
            RegistroRecoleccion registroRecoleccion = registroRecoleccionService.obtenerOcrearRegistro(collectorId);
            return ResponseEntity.ok(new RegistroRecoleccionDTO(registroRecoleccion));
        } catch (RegistroPendienteException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<?> completarRegistroRecoleccion(@PathVariable Long id) {
        try {
            registroRecoleccionService.completarRegistroRecoleccion(id);
            return ResponseEntity.ok("Registro de recolección completado con éxito");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
