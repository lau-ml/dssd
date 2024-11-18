package dssd.server.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import dssd.server.DTO.PaginatedResponseDTO;
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
    @GetMapping("/my-points")
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

    @PreAuthorize("hasAuthority('PERMISO_VER_MIS_PUNTOS_RECOLECCIONES')")
    @GetMapping("/my-points/paginated")
    public ResponseEntity<?> obtenerPuntosDeRecoleccionPaginados(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            PaginatedResponseDTO<PuntoDeRecoleccionDTO> puntosDeRecoleccionPaginados;

            if (search != null && !search.trim().isEmpty()) {
                puntosDeRecoleccionPaginados = puntoDeRecoleccionService.obtenerPuntosDeRecoleccionFiltrados(pageable,
                        search);
            } else {
                puntosDeRecoleccionPaginados = puntoDeRecoleccionService.obtenerPuntosDeRecoleccionPaginados(pageable);
            }

            return ResponseEntity.ok(puntosDeRecoleccionPaginados);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (JsonProcessingException | UsuarioInvalidoException e) {
            throw new RuntimeException(e);
        }
    }

    @PreAuthorize("hasAuthority('PERMISO_ELIMINAR_MI_PUNTO_RECOLECCION')")
    @DeleteMapping("/my-points/{id}")
    public ResponseEntity<?> desvincularPuntoDeRecoleccion(@PathVariable Long id) {
        try {
            puntoDeRecoleccionService.desvincularPuntoDeRecoleccion(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (UsuarioInvalidoException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('PERMISO_VER_MIS_PUNTOS_RECOLECCIONES')")
    @GetMapping("/my-points/not-linked/paginated")
    public ResponseEntity<?> obtenerPuntosDeRecoleccionNoVinculadosFiltrados(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            PaginatedResponseDTO<PuntoDeRecoleccionDTO> puntosDeRecoleccionNoVinculados;

            if (search != null && !search.trim().isEmpty()) {
                puntosDeRecoleccionNoVinculados = puntoDeRecoleccionService
                        .obtenerPuntosDeRecoleccionNoVinculadosFiltrados(pageable, search);
            } else {
                puntosDeRecoleccionNoVinculados = puntoDeRecoleccionService
                        .obtenerPuntosDeRecoleccionNoVinculadosPaginados(pageable);
            }

            return ResponseEntity.ok(puntosDeRecoleccionNoVinculados);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        } catch (JsonProcessingException | UsuarioInvalidoException e) {
            throw new RuntimeException(e);
        }
    }
}
