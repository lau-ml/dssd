package dssd.apiecocycle.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dssd.apiecocycle.DTO.CentroDTO;
import dssd.apiecocycle.model.CentroDeRecepcion;
import dssd.apiecocycle.service.CentroDeRecepcionService;

@RestController
@RequestMapping("/api/centros")
public class CentroDeRecepcionController {

    private final CentroDeRecepcionService centroDeRecepcionService;

    public CentroDeRecepcionController(CentroDeRecepcionService centroDeRecepcionService) {
        this.centroDeRecepcionService = centroDeRecepcionService;
    }

    @GetMapping
    public ResponseEntity<?> getAllCentrosDeRecepcion() {
        try {
            List<CentroDeRecepcion> centros = centroDeRecepcionService.getAllCentrosDeRecepcion();
            List<CentroDTO> centroDeRecepcionDTOs = centros.stream()
                    .map(CentroDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(centroDeRecepcionDTOs);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
