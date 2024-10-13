package dssd.apiecocycle.controller;

import java.util.List;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dssd.apiecocycle.DTO.CentroDTO;
import dssd.apiecocycle.model.CentroDeRecepcion;
import dssd.apiecocycle.service.CentroDeRecepcionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/centros")
public class CentroDeRecepcionController {

    @Autowired
    private CentroDeRecepcionService centroDeRecepcionService;

    public CentroDeRecepcionController(CentroDeRecepcionService centroDeRecepcionService) {
        this.centroDeRecepcionService = centroDeRecepcionService;
    }

    @GetMapping
    @Operation(summary = "Obtener todos los centros de recepción", description = "Este endpoint devuelve una lista de todos los centros de recepción.",
    security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Centros encontrados", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CentroDTO.class), examples = @ExampleObject(value = "[{\"id\": 1, \"email\": \"mailCentro1@ecocycle.com\", \"telefono\": \"221-22224\", \"direccion\": \"Calle falsa 123\"}, {\"id\": 2, \"email\": \"mailCentro2@ecocycle.com\", \"telefono\": \"221-11114\", \"direccion\": \"Calle verdadera 123\"}, {\"id\": 3, \"email\": \"mailCentro3@ecocycle.com\", \"telefono\": \"221-44444\", \"direccion\": \"Calle alguna 123\"}]"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Error: [mensaje del error]")))
    })
    @PreAuthorize("hasAuthority('OBTENER_CENTROS_DE_RECEPCION')")
    public ResponseEntity<?> getAllCentrosDeRecepcion() {
        try {
            List<CentroDeRecepcion> centros = centroDeRecepcionService.getAllCentrosDeRecepcion();
            List<CentroDTO> centroDeRecepcionDTOs = centros.stream()
                    .map(CentroDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(centroDeRecepcionDTOs);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
    @PreAuthorize("hasAuthority('OBTENER_CENTROS_DE_RECEPCION')")
    @GetMapping("/{id}")
    @Operation(summary = "Obtener centro de recepción por ID", description = "Este endpoint devuelve un centro de recepción específico utilizando su ID.",
    security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Centro encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CentroDTO.class), examples = @ExampleObject(value = "{\"id\": 1, \"email\": \"mailCentro1@ecocycle.com\", \"telefono\": \"221-22224\", \"direccion\": \"Calle falsa 123\"}"))),
            @ApiResponse(responseCode = "404", description = "Centro no encontrado", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Centro no encontrado"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Error: [mensaje del error]")))
    })
    public ResponseEntity<?> getCentroDeRecepcionById(@PathVariable Long id) {
        try {
            CentroDeRecepcion centro = centroDeRecepcionService.getCentroDeRecepcionById(id);
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
