package dssd.apisorteokubernetes.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ReadyController {
    @GetMapping("/healthz")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Application is running");
    }

    @GetMapping("/readiness")
    public ResponseEntity<String> readinessCheck() {
        // Aquí podrías verificar si la base de datos o otros servicios están listos
        return ResponseEntity.ok("Application is ready");
    }


}
