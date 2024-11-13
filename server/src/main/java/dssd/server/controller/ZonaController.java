package dssd.server.controller;

import dssd.server.service.ZonaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/zones")
public class ZonaController {

    @Autowired
    private ZonaService zonaService;

    @GetMapping("/get-all")
    public ResponseEntity<?> getZonas() {
        return ResponseEntity.ok(zonaService.getAll());
    }
}
