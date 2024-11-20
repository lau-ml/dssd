package dssd.server.controller;

// import java.util.List;

import dssd.server.DTO.CentroRegistroDTO;
import dssd.server.service.CentroRecoleccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

// import dssd.server.service.CentroRecoleccionService;

@RestController
@RequestMapping("/api/centers")
public class CentroRecoleccionController {

    @Autowired
    private CentroRecoleccionService centroRecoleccionService;

    @GetMapping("/{centerId}/collectors")
    public ResponseEntity<?> getRecolectoresByCentro(@PathVariable Long centerId) {
        // return centroRecoleccionService.getRecolectoresByCentro(centerId);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAll() {
        return new ResponseEntity<>(centroRecoleccionService
                .getAll()
                .stream()
                .map(CentroRegistroDTO::new)
                , HttpStatus.OK);
    }

}
