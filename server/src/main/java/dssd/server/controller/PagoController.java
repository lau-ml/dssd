package dssd.server.controller;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;

import dssd.server.DTO.PaginatedResponseDTO;
import dssd.server.DTO.PagoDTO;
import dssd.server.exception.UsuarioInvalidoException;
import dssd.server.service.PagoService;

@RestController
@RequestMapping("/api/pagos")
@CrossOrigin(origins = "http://localhost:4200")
public class PagoController {

    @Autowired
    private PagoService pagoService;

    @PreAuthorize("hasAuthority('PERMISO_VER_PAGOS_RECOLECTORES')")
    @GetMapping("/get-collector/paginated")
    public ResponseEntity<?> listarPagosPorRecolector(
            @RequestParam Long recolectorId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String orden,
            @RequestParam(defaultValue = "true") boolean asc,
            @RequestParam(defaultValue = "fechaEmision") String columnaFecha,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta) {
        try {
            Sort sort = Sort.unsorted();
            if (orden != null && !orden.isEmpty()) {
                Sort.Direction direction = asc ? Sort.Direction.ASC : Sort.Direction.DESC;
                sort = Sort.by(direction, orden);
            }

            Pageable pageable = PageRequest.of(page, size, sort);

            PaginatedResponseDTO<PagoDTO> pagos = pagoService.listarPagosPorRecolector(
                    recolectorId, pageable, estado, columnaFecha, fechaDesde, fechaHasta);

            return ResponseEntity.ok(pagos);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('PERMISO_EMPLEADO_VER_PAGOS_RECOLECTORES')")
    @GetMapping("/centro/recolectores/pagos/paginado")
    public ResponseEntity<?> listarPagosRecolectoresDelCentro(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String orden,
            @RequestParam(defaultValue = "true") boolean asc,
            @RequestParam(defaultValue = "fechaEmision") String columnaFecha,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta,
            @RequestParam(required = false) String username) {
        try {
            Sort sort = Sort.unsorted();
            if (orden != null && !orden.isEmpty()) {
                Sort.Direction direction = asc ? Sort.Direction.ASC : Sort.Direction.DESC;
                if (orden.equals("recolector.username")) {
                    sort = Sort.by(direction, "registroRecoleccion.recolector.username");
                } else {
                    sort = Sort.by(direction, orden);
                }
            }

            Pageable pageable = PageRequest.of(page, size, sort);

            PaginatedResponseDTO<PagoDTO> pagos = pagoService.listarPagosPorCentro(pageable, estado, columnaFecha,
                    fechaDesde, fechaHasta, username);

            return ResponseEntity.ok(pagos);
        } catch (JsonProcessingException | UsuarioInvalidoException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('PERMISO_EMPLEADO_EDITAR_PAGOS_RECOLECTORES')")
    @PostMapping("/realizar-pago")
    public ResponseEntity<?> realizarPago(@RequestBody Map<String, Long> body) {
        try {
            Long recolectorId = body.get("recolectorId");
            System.out.println("aca estoy");
            PagoDTO pagoDTO = pagoService.realizarPago(recolectorId);
            System.out.println("termine");
            return ResponseEntity.ok(pagoDTO);
        } catch (Exception e) {
            System.err.println("salgo por error");
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
