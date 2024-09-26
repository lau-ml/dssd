package dssd.server.controller;

import dssd.server.service.BonitaLoginService;
import dssd.server.service.RecolectorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.function.EntityResponse;

@RestController
public class RecolectorController {

    @Autowired
    private RecolectorService recolectorService;

    @Autowired
    BonitaLoginService bonitaLoginService;

    @GetMapping("/recolectores")
    public ResponseEntity<String> listarRecolectores() {
        this.bonitaLoginService.login();
        return new ResponseEntity<>("Recolectores", HttpStatus.OK);
    }
}
