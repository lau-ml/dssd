package dssd.server.controller;

import dssd.server.service.EstadisticaService;
import dssd.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/estadistica")
public class EstadisticaController {

    @Autowired
    private EstadisticaService estadisticaService;


    @GetMapping("/primera-vez-15-dias/{email}")
    public ResponseEntity<?> getUsuario15dias(@PathVariable String email) {

        return ResponseEntity.ok(estadisticaService.getUsuariosQueReciclaronPorPrimeraVezEnLosUltimos15Dias(email));
    }

}
