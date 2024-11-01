package dssd.server.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dssd.server.DTO.MaterialDTO;
import dssd.server.model.Material;
import dssd.server.service.MaterialService;

@RestController
@RequestMapping("/api/material")
@CrossOrigin(origins = "http://localhost:4200")
public class MaterialController {

    @Autowired
    private MaterialService materialService;

    @GetMapping("/get-materials")
    public ResponseEntity<?> obtenerMateriales() {
        try {
            List<Material> materiales = materialService.obtenerMateriales();
            List<MaterialDTO> materialesDTO = materiales.stream()
                    .map(MaterialDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(materialesDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
