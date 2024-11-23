package dssd.server.controller;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dssd.server.DTO.PaginatedResponseDTO;
import dssd.server.DTO.PagoDTO;
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
}
