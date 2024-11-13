package dssd.apisorteokubernetes.controllers;

import dssd.apisorteokubernetes.services.InscripcionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sorteo")
public class InscripcionController {

    @Autowired
    private InscripcionService inscripcionService;

    @PostMapping("/inscribirse")
    public ResponseEntity<?> inscribirseSorteo(@RequestBody Long idCentro) {
        return ResponseEntity.ok(this.inscripcionService.inscribirse(idCentro));
    }

}
