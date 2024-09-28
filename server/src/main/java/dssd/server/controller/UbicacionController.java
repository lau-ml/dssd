package dssd.server.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dssd.server.DTO.UbicacionDTO;
import dssd.server.model.Ubicacion;
import dssd.server.service.UbicacionService;

@RestController
@RequestMapping("/api/location")
public class UbicacionController {
    @Autowired
    private UbicacionService ubicacionService;

    @GetMapping("/get-locations")
    public ResponseEntity<?> obtenerUbicaciones() {
        try {
            List<Ubicacion> ubicaciones = ubicacionService.obtenerUbicaciones();
            List<UbicacionDTO> ubicacionesDTO = ubicaciones.stream()
                    .map(UbicacionDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(ubicacionesDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
