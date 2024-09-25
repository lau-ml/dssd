package dssd.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RecolectorController {

    @GetMapping("/recolectores")
    public String listarRecolectores() {
        return "Lista de recolectores";
    }
}
