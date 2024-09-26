package dssd.server.controller;

import dssd.server.service.BonitaService;
import dssd.server.service.RecolectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RecolectorController {

    @Autowired
    private RecolectorService recolectorService;

    @Autowired
    BonitaService bonitaService;

    @GetMapping("/recolectores")
    public ResponseEntity<String> listarRecolectores() {
        this.bonitaService.login();
        return new ResponseEntity<>("Recolectores", HttpStatus.OK);
    }
}
