package dssd.apiecocycle.controller;

import dssd.apiecocycle.DTO.CentroDTO;
import dssd.apiecocycle.model.DepositoGlobal;
import dssd.apiecocycle.service.DepositoGlobalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/depositos")
public class DepositoGlobalController {

    @Autowired
    private DepositoGlobalService depositoGlobalService;

    public DepositoGlobalController(DepositoGlobalService depositoGlobalService) {
        this.depositoGlobalService = depositoGlobalService;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los depósitos globales", description = "Este endpoint devuelve una lista de todos los depósitos globales.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Depósitos encontrados", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CentroDTO.class), examples = @ExampleObject(value = "[{\"id\": 4, \"email\": \"global1@ecocycle.com\", \"telefono\": \"123-4567\", \"direccion\": \"Av. Siempreviva 742\"}, {\"id\": 5, \"email\": \"global2@ecocycle.com\", \"telefono\": \"123-8901\", \"direccion\": \"Av. Las Rosas 100\"}, {\"id\": 6, \"email\": \"global3@ecocycle.com\", \"telefono\": \"987-6543\", \"direccion\": \"Calle Los Álamos 333\"}]"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Error: [mensaje del error]")))
    })
    public ResponseEntity<?> getAllDepositosGlobales() {
        try {
            List<DepositoGlobal> centros = depositoGlobalService.getAllDepositosGlobales();
            List<CentroDTO> centroDTOs = centros.stream()
                    .map(CentroDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(centroDTOs);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener depósito global por ID", description = "Este endpoint devuelve un depósito global específico utilizando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Depósito encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CentroDTO.class), examples = @ExampleObject(value = "{\"id\": 4, \"email\": \"global1@ecocycle.com\", \"telefono\": \"123-4567\", \"direccion\": \"Av. Siempreviva 742\"}"))),
            @ApiResponse(responseCode = "404", description = "Depósito no encontrado", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Depósito no encontrado"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Error: [mensaje del error]")))
    })
    public ResponseEntity<?> getDepositoGlobalById(@PathVariable Long id) {
        try {
            DepositoGlobal centro = depositoGlobalService.getDepositoGlobalById(id);
            if (centro != null) {
                CentroDTO centroDTO = new CentroDTO(centro);
                return ResponseEntity.ok(centroDTO);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}