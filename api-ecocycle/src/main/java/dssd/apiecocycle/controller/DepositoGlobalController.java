package dssd.apiecocycle.controller;

import dssd.apiecocycle.model.DepositoGlobal;
import dssd.apiecocycle.service.DepositoGlobalService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/depositos")
public class DepositoGlobalController {

    @Autowired
    private DepositoGlobalService depositoGlobalService;

    public DepositoGlobalController(DepositoGlobalService depositoGlobalService) {
        this.depositoGlobalService = depositoGlobalService;
    }

    @GetMapping
    public ResponseEntity<List<DepositoGlobal>> getAllDepositosGlobales() {
        List<DepositoGlobal> depositos = depositoGlobalService.getAllDepositosGlobales();
        return ResponseEntity.ok(depositos);
    }
}