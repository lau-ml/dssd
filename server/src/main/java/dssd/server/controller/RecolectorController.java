package dssd.server.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/recolector")
public class RecolectorController {

    @GetMapping("/recolectores")
    public String listarRecolectores() {
        return "Lista de recolectores";
    }
}
