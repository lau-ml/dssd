package dssd.server.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import dssd.server.DTO.PaginatedResponseDTO;
import dssd.server.DTO.PuntoDeRecoleccionDTO;
import dssd.server.DTO.UsuarioDTO;
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
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String ordenColumna,
            @RequestParam(defaultValue = "true") boolean ordenAscendente) {
        try {
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

    @PreAuthorize("hasAuthority('PERMISO_VER_PUNTOS_RECOLECCIONES')")
    @GetMapping("/all-points/paginated")
    public ResponseEntity<?> obtenerTodosPuntosDeRecoleccion(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String ordenColumna,
            @RequestParam(defaultValue = "true") boolean ordenAscendente) {
        try {

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

            PaginatedResponseDTO<PuntoDeRecoleccionDTO> puntosDeRecoleccionPaginados;

            if (search != null && !search.trim().isEmpty()) {
                puntosDeRecoleccionPaginados = puntoDeRecoleccionService
                        .obtenerTodosPuntosDeRecoleccionFiltrados(pageable, search);
            } else {
                puntosDeRecoleccionPaginados = puntoDeRecoleccionService
                        .obtenerTodosPuntosDeRecoleccionPaginados(pageable);
            }

            return ResponseEntity.ok(puntosDeRecoleccionPaginados);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('PERMISO_VER_PUNTOS_RECOLECCIONES')")
    @GetMapping("/{id}")
    public ResponseEntity<?> obtenerPuntoDeRecoleccionPorId(@PathVariable Long id) {
        try {
            PuntoDeRecoleccion puntoDeRecoleccion = puntoDeRecoleccionService.obtenerPorId(id);
            PuntoDeRecoleccionDTO puntoDeRecoleccionDTO = new PuntoDeRecoleccionDTO(puntoDeRecoleccion);
            return ResponseEntity.ok(puntoDeRecoleccionDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('PERMISO_EDITAR_PUNTO_RECOLECCION')")
    @PutMapping("/{id}")
    public ResponseEntity<?> editarPuntoDeRecoleccion(
            @PathVariable Long id,
            @RequestBody PuntoDeRecoleccionDTO puntoDeRecoleccionDTO) {
        PuntoDeRecoleccion puntoDeRecoleccion = puntoDeRecoleccionService.editarMaterial(id, puntoDeRecoleccionDTO);
        return ResponseEntity.ok(new PuntoDeRecoleccionDTO(puntoDeRecoleccion));
    }

    @PreAuthorize("hasAuthority('PERMISO_CREAR_PUNTO_RECOLECCION')")
    @PostMapping("/create-point")
    public ResponseEntity<?> crearPuntoDeRecoleccion(@RequestBody PuntoDeRecoleccionDTO puntoDeRecoleccionDTO) {
        PuntoDeRecoleccionDTO puntoCreado = puntoDeRecoleccionService
                .crearPuntoDeRecoleccion(puntoDeRecoleccionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(puntoCreado);
    }

    @PreAuthorize("hasAuthority('PERMISO_ELIMINAR_PUNTO_RECOLECCION')")
    @DeleteMapping("/delete-point/{id}")
    public ResponseEntity<?> eliminarPuntoDeRecoleccion(@PathVariable Long id) {
        try {
            puntoDeRecoleccionService.eliminarPuntoDeRecoleccion(id);

            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('PERMISO_VER_RECOLECTORES_DE_PUNTO')")
    @GetMapping("/{id}/recolectores")
    public ResponseEntity<?> obtenerRecolectoresDePuntoDeRecoleccion(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            PaginatedResponseDTO<UsuarioDTO> recolectoresPaginados;

            if (search != null && !search.trim().isEmpty()) {
                recolectoresPaginados = puntoDeRecoleccionService.obtenerRecolectoresDePuntoFiltrados(id, pageable,
                        search);
            } else {
                recolectoresPaginados = puntoDeRecoleccionService.obtenerRecolectoresDePuntoPaginados(id, pageable);
            }

            return ResponseEntity.ok(recolectoresPaginados);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('PERMISO_EDITAR_PUNTO_RECOLECCION')")
    @GetMapping("/{id}/recolectores-no-asociados")
    public ResponseEntity<?> obtenerRecolectoresNoAsociadosAPuntoDeRecoleccion(
            @PathVariable Long id,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String search) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            PaginatedResponseDTO<UsuarioDTO> recolectoresPaginados;

            if (search != null && !search.trim().isEmpty()) {
                recolectoresPaginados = puntoDeRecoleccionService
                        .obtenerRecolectoresNoAsociadosAPuntoFiltrados(id, pageable, search);
            } else {
                recolectoresPaginados = puntoDeRecoleccionService
                        .obtenerRecolectoresNoAsociadosAPunto(id, pageable);
            }

            return ResponseEntity.ok(recolectoresPaginados);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping("/{id}/vincular-recolector")
    public ResponseEntity<?> vincularRecolectorAPuntoDeRecoleccion(
            @PathVariable Long id,
            @RequestBody Map<String, Long> requestBody) {
        try {
            Long recolectorId = requestBody.get("recolectorId");
            puntoDeRecoleccionService.vincularRecolector(id, recolectorId);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('PERMISO_EDITAR_PUNTO_RECOLECCION')")
    @DeleteMapping("/{id}/desvincular-recolector/{recolectorId}")
    public ResponseEntity<?> desvincularRecolectorDePuntoDeRecoleccion(
            @PathVariable Long id,
            @PathVariable Long recolectorId) {
        try {
            puntoDeRecoleccionService.desvincularRecolector(id, recolectorId);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
