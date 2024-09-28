package dssd.server.controller;

// import java.util.List;

import org.springframework.web.bind.annotation.RestController;
// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

// import dssd.server.service.CentroRecoleccionService;

@RestController
@RequestMapping("/api/centros")
public class CentroRecoleccionController {

    // @Autowired
    // private CentroRecoleccionService centroRecoleccionService;

    @GetMapping("/{centroId}/recolectores")
    public ResponseEntity<?> getRecolectoresByCentro(@PathVariable Long centroId) {
        // return centroRecoleccionService.getRecolectoresByCentro(centroId);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
