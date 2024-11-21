package dssd.server.controller;

import dssd.server.DTO.PaginatedResponseDTO;
import dssd.server.DTO.UsuarioDTO;
import dssd.server.exception.UsuarioInvalidoException;
import dssd.server.service.BonitaService;
import dssd.server.service.RecolectorService;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
@RequestMapping("/api/collector")
@CrossOrigin(origins = "http://localhost:4200")
public class RecolectorController {

    @Autowired
    private RecolectorService recolectorService;

    @Autowired
    BonitaService bonitaService;

    @PreAuthorize("hasAuthority('PERMISO_VER_RECOLECTORES_DEL_CENTRO')")
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

    @PreAuthorize("hasAuthority('PERMISO_VER_RECOLECTORES')")
    @GetMapping("/all-collectors")
    public ResponseEntity<?> listarRecolectores(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            PaginatedResponseDTO<UsuarioDTO> recolectoresPaginados;

            if (search != null && !search.trim().isEmpty()) {
                recolectoresPaginados = recolectorService.obtenerRecolectoresPaginadosYFiltrados(pageable, search);
            } else {
                recolectoresPaginados = recolectorService.obtenerRecolectoresPaginadosYFiltrados(pageable, null);
            }

            return ResponseEntity.ok(recolectoresPaginados);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    /*
     * @PreAuthorize("hasAuthority('PERMISO_CREAR_RECOLECTORES')")
     * 
     * @GetMapping("/create-collector")
     * public ResponseEntity<?> crearRecolector(@RequestBody UsuarioDTO recolector)
     * {
     * try {
     * recolectorService.crearRecolector(recolector);
     * return ResponseEntity.ok(recolector);
     * } catch (RuntimeException e) {
     * return
     * ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
     * } catch (JsonProcessingException | UsuarioInvalidoException e) {
     * throw new RuntimeException(e);
     * }
     * }
     * 
     * @PreAuthorize("hasAuthority('PERMISO_ELIMINAR_RECOLECTORES')")
     * 
     * @DeleteMapping("{id}/delete-collector")
     * public ResponseEntity<?> eliminarRecolector(@PathVariable Long id) {
     * try {
     * UsuarioDTO recolector = recolectorService.eliminarRecolector(id);
     * return ResponseEntity.ok(recolector);
     * } catch (RuntimeException e) {
     * return
     * ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
     * } catch (JsonProcessingException | UsuarioInvalidoException e) {
     * throw new RuntimeException(e);
     * }
     * }
     * 
     * @PreAuthorize("hasAuthority('PERMISO_MODIFICAR_RECOLECTORES')")
     * 
     * @PutMapping("{id}/modify-collector")
     * public ResponseEntity<?> modificarRecolector(@PathVariable Long id) {
     * try {
     * UsuarioDTO recolector = recolectorService.modificarRecolector(id);
     * return ResponseEntity.ok(recolector);
     * } catch (RuntimeException e) {
     * return
     * ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
     * } catch (JsonProcessingException | UsuarioInvalidoException e) {
     * throw new RuntimeException(e);
     * }
     * }
     * 
     * @PreAuthorize("hasAuthority('PERMISO_VER_TODOS_RECOLECTORES')")
     * 
     * @GetMapping("/all-collectors")
     * public ResponseEntity<?> listarTodosRecolectores() {
     * try {
     * List<UsuarioDTO> recolectores = recolectorService.obtenerTodosRecolectores();
     * return ResponseEntity.ok(recolectores);
     * } catch (RuntimeException e) {
     * return
     * ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
     * }
     * }
     */

}
