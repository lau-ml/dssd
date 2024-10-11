package dssd.apiecocycle.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<List<CentroDeRecepcion>> getAllCentrosDeRecepcion() {
        List<CentroDeRecepcion> centros = centroDeRecepcionService.getAllCentrosDeRecepcion();
        return ResponseEntity.ok(centros);
    }
}
