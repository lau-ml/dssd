package dssd.apisorteokubernetes.controllers;

import dssd.apisorteokubernetes.exceptions.SorteoExceptions;
import dssd.apisorteokubernetes.models.InscripcionModel;
import dssd.apisorteokubernetes.requests.CentroDTO;
import dssd.apisorteokubernetes.responses.ErrorResponse;
import dssd.apisorteokubernetes.responses.InscripcionDTO;
import dssd.apisorteokubernetes.services.InscripcionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/sorteo")
public class InscripcionController {

    @Autowired
    private InscripcionService inscripcionService;

    @PostMapping("/inscribirse")
    public ResponseEntity<?> inscribirseSorteo(@RequestBody @Valid  CentroDTO centroDTO) {
        try {

            InscripcionModel inscripcion = this.inscripcionService.inscribirse(centroDTO);
            return ResponseEntity.ok().body(
                    InscripcionDTO.builder()
                            .fechaInscripcion(inscripcion.getFechaInscripcion())
                            .numeroInscripcionSorteo(inscripcion.getNumeroInscripcionSorteo())
                            .numeroSorteo(inscripcion.getSorteo().getId())
                            .fechaSorteo(inscripcion.getSorteo().getFechaSorteo())
                            .build()

            );
        } catch (SorteoExceptions e) {
            return ResponseEntity.badRequest().body(ErrorResponse.builder().error(e.getMessage()).build());
        }
    }

    @GetMapping("/ganador")
    public ResponseEntity<?> getGanador() {
        try {
            InscripcionModel inscripcion = this.inscripcionService.getGanador();
            return ResponseEntity.ok().body(
                    InscripcionDTO.builder()
                            .fechaInscripcion(inscripcion.getFechaInscripcion())
                            .numeroInscripcionSorteo(inscripcion.getNumeroInscripcionSorteo())
                            .numeroSorteo(inscripcion.getSorteo().getId())
                            .fechaSorteo(inscripcion.getSorteo().getFechaSorteo())
                            .build()

            );
        } catch (SorteoExceptions e) {
            return ResponseEntity.badRequest().body(ErrorResponse.builder().error(e.getMessage()).build());
        }
    }
}
