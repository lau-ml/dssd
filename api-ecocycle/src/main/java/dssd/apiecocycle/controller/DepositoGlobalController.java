package dssd.apiecocycle.controller;

import dssd.apiecocycle.DTO.CentroDTO;
import dssd.apiecocycle.model.DepositoGlobal;
import dssd.apiecocycle.service.DepositoGlobalService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/depositos")
public class DepositoGlobalController {

    @Autowired
    private DepositoGlobalService depositoGlobalService;

    public DepositoGlobalController(DepositoGlobalService depositoGlobalService) {
        this.depositoGlobalService = depositoGlobalService;
    }

    @GetMapping
    public ResponseEntity<?> getAllDepositosGlobales() {
        try {
            List<DepositoGlobal> centros = depositoGlobalService.getAllDepositosGlobales();
            List<CentroDTO> centroDTOs = centros.stream()
                    .map(CentroDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(centroDTOs);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDepositoGlobalById(@PathVariable Long id) {
        try {
            DepositoGlobal centro = depositoGlobalService.getDepositoGlobalById(id);
            if (centro != null) {
                CentroDTO centroDTO = new CentroDTO(centro);
                return ResponseEntity.ok(centroDTO);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}