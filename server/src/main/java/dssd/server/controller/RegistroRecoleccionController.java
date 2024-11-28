package dssd.server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import dssd.server.exception.UsuarioInvalidoException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import dssd.server.DTO.RegistroRecoleccionDTO;
import dssd.server.model.RegistroRecoleccion;
import dssd.server.model.Usuario;
import dssd.server.service.RegistroRecoleccionService;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/collection-record")
@CrossOrigin(origins = "http://localhost:4200")
public class RegistroRecoleccionController {

    @Autowired
    private RegistroRecoleccionService registroRecoleccionService;

    @PreAuthorize("hasAuthority('PERMISO_VER_MI_REGISTROS_RECOLECCION')")
    @GetMapping("/collector/{collectorId}")
    public ResponseEntity<?> obtenerMiUltimoRegistro(@PathVariable Long collectorId) {
        try {
            RegistroRecoleccion registroRecoleccion = registroRecoleccionService.obtenerMiUltimoRegistro();
            return ResponseEntity.ok(new RegistroRecoleccionDTO(registroRecoleccion));
        } catch (UsuarioInvalidoException e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/{id}/complete")
    @PreAuthorize("hasAuthority('PERMISO_EDITAR_REGISTROS_RECOLECCION')")
    public ResponseEntity<?> completarRegistroRecoleccion(@PathVariable Long id) {
        try {

            return ResponseEntity.ok(registroRecoleccionService.completarRegistroRecoleccion(id));
        } catch (RuntimeException | JsonProcessingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('PERMISO_CANCELAR_REGISTROS_RECOLECCION')")
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

    @PostMapping
    @PreAuthorize("hasAuthority('PERMISO_REGISTRAR_MATERIALES_ENTREGADOS')")
    public ResponseEntity<?> materialesEntregadosDelRecolector(
            @RequestBody RegistroRecoleccionDTO registroRecoleccionDTO) {
        try {
            registroRecoleccionService.materialesEntregadosDelRecolector(registroRecoleccionDTO);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (UsuarioInvalidoException e) {
            throw new RuntimeException(e);
        }
    }

    @PreAuthorize("hasAuthority('PERMISO_VER_REGISTROS_RECOLECCION')")
    @GetMapping("/get-by-id/{registroId}/admin")
    public ResponseEntity<?> obtenerRegistro(@PathVariable Long registroId) {
        try {
            RegistroRecoleccionDTO registroRecoleccion = registroRecoleccionService.obtenerRegistro(registroId);
            return ResponseEntity.ok(registroRecoleccion);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PreAuthorize("hasAuthority('PERMISO_VER_MI_REGISTROS_RECOLECCION')")
    @GetMapping("/get-by-id/{registroId}")
    public ResponseEntity<?> obtenerRegistroSiPertenece(@PathVariable Long registroId) {
        try {

            RegistroRecoleccionDTO registroRecoleccion = registroRecoleccionService
                    .obtenerRegistroSiPertenece(registroId);

            return ResponseEntity.ok(registroRecoleccion);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Registro no encontrado.");
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No tienes permiso para acceder a este registro.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error interno del servidor.");
        }
    }
}
