package dssd.server.controller;

import org.springframework.web.bind.annotation.RestController;

import dssd.server.DTO.OrdenDeDistribucionDTO;
import dssd.server.DTO.PaginatedResponseDTO;
import dssd.server.model.OrdenDeDistribucion;
import dssd.server.model.Usuario;
import dssd.server.service.OrdenDeDistribucionService;
import dssd.server.service.UserService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
@RequestMapping("/api/ordenes")
@CrossOrigin(origins = "http://localhost:4200")
public class OrdenDeDistribucionController {

    @Autowired
    private OrdenDeDistribucionService ordenDeDistribucionService;

    @Autowired
    private UserService userService;

    @PreAuthorize("hasAuthority('PERMISO_VER_ORDENES_DISTRIBUCION')")
    @GetMapping
    public ResponseEntity<?> obtenerOrdenesPaginadas(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String ordenColumna,
            @RequestParam(defaultValue = "true") boolean ordenAscendente) {
        try {
            Usuario usuarioLogueado = userService.recuperarUsuario();
            if (usuarioLogueado.getCentroRecoleccion() == null) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("El usuario no tiene asignado un centro de recolección.");
            }

            Sort sort = Sort.unsorted();
            if (ordenColumna != null && !ordenColumna.isEmpty()) {
                String[] ordenColumnaSplit = ordenColumna.split(",");
                if (ordenColumnaSplit.length == 2) {
                    String campo = ordenColumnaSplit[0];
                    String direccion = ordenColumnaSplit[1];

                    Sort.Direction direction = "asc".equalsIgnoreCase(direccion) ? Sort.Direction.ASC
                            : Sort.Direction.DESC;
                    sort = Sort.by(direction, campo);
                }
            }
            Pageable pageable = PageRequest.of(page, size, sort);

            PaginatedResponseDTO<OrdenDeDistribucionDTO> ordenes = ordenDeDistribucionService
                    .obtenerOrdenesPaginadasYFiltradas(pageable, search, estado,
                            usuarioLogueado.getCentroRecoleccion().getId());

            return ResponseEntity.ok(ordenes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('PERMISO_EDITAR_ORDENES_DISTRIBUCION')")
    @PutMapping("/{id}/estado")
    public ResponseEntity<Void> cambiarEstado(@PathVariable Long id, @RequestBody Map<String, String> body) {
        try {
            String estadoStr = body.get("estado");
            OrdenDeDistribucion.EstadoOrden nuevoEstado = OrdenDeDistribucion.EstadoOrden.valueOf(estadoStr);

            ordenDeDistribucionService.cambiarEstado(id, nuevoEstado);

            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
