package dssd.server.controller;

import dssd.server.response.ResponseEstadistica;
import dssd.server.service.EstadisticaService;
import dssd.server.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/estadistica")
public class EstadisticaController {

    @Autowired
    private EstadisticaService estadisticaService;


    @GetMapping("/primera-vez-15-dias/{email}")
    public ResponseEntity<?> getTuvoEstadisticaMitadMes(@PathVariable String email) {
        Boolean tuvoEstadistica = estadisticaService.getTuvoEstadisticaMitadMesUsuario(email);
        return new ResponseEntity<>(ResponseEstadistica.builder().esPrimeraVezDias(tuvoEstadistica).build(), tuvoEstadistica ? org.springframework.http.HttpStatus.OK : org.springframework.http.HttpStatus.NOT_FOUND);
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crearEstadistica(@RequestBody String email) {
        estadisticaService.crearEstadistica(email);
        return new ResponseEntity<>(org.springframework.http.HttpStatus.OK);
    }

}
