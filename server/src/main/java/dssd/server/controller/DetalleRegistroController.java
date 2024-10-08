package dssd.server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import dssd.server.exception.UsuarioInvalidoException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dssd.server.DTO.DetalleRegistroDTO;
import dssd.server.DTO.RegistroRecoleccionDTO;
import dssd.server.service.DetalleRegistroService;

@RestController
@RequestMapping("/api/record-details")
@CrossOrigin(origins = "http://localhost:4200")

public class DetalleRegistroController {
    @Autowired
    private DetalleRegistroService detalleRegistroService;

    @PreAuthorize("hasAuthority('PERMISO_CREAR_DETALLES_REGISTROS')")
    @PostMapping("/add-new-material")
    public ResponseEntity<?> agregarDetalleRegistro(@RequestBody DetalleRegistroDTO detalleRegistroDTO) {
        try {
            RegistroRecoleccionDTO registroRecoleccionDTO = detalleRegistroService
                    .agregarDetalleRegistro(detalleRegistroDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(registroRecoleccionDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (JsonProcessingException | UsuarioInvalidoException e) {
            throw new RuntimeException(e);
        }
    }
}
