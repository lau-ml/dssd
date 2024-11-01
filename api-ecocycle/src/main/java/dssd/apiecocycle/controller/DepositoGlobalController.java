package dssd.apiecocycle.controller;

import dssd.apiecocycle.DTO.CentroDTO;
import dssd.apiecocycle.model.DepositoGlobal;
import dssd.apiecocycle.response.ErrorResponse;
import dssd.apiecocycle.response.MessageResponse;
import dssd.apiecocycle.service.DepositoGlobalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/depositos")
public class DepositoGlobalController {

    @Autowired
    private DepositoGlobalService depositoGlobalService;

    public DepositoGlobalController(DepositoGlobalService depositoGlobalService) {
        this.depositoGlobalService = depositoGlobalService;
    }

    @PreAuthorize("hasAuthority('OBTENER_DEPOSITOS_GLOBALES')")
    @GetMapping
    @Operation(summary = "Obtener todos los depósitos globales", description = "Este endpoint devuelve una lista de todos los depósitos globales.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Depósitos encontrados", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CentroDTO.class), examples = @ExampleObject(value = "[{\"id\": 4, \"email\": \"global1@ecocycle.com\", \"telefono\": \"2212222222\", \"direccion\": \"Av. Siempreviva 742\"}, {\"id\": 5, \"email\": \"global2@ecocycle.com\", \"telefono\": \"2213333333\", \"direccion\": \"Av. Las Rosas 100\"}, {\"id\": 6, \"email\": \"global3@ecocycle.com\", \"telefono\": \"2214444444\", \"direccion\": \"Calle Los Álamos 333\"}]"))),
            @ApiResponse(responseCode = "401", description = "Debe iniciar sesión", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "{\"error\": \"No está autenticado. Por favor, inicie sesión.\"}"))),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para acceder a este recurso", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "{\"error\": \"No tiene permisos para acceder a este recurso.\"}"))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"Error interno del servidor.\"}")
                    )
            )    })
    @Parameters({
            @Parameter(name = "email", description = "Email del depósito global (default: vacío)", required = false, examples = {
                    @ExampleObject(name = "Caso de email existente", value = "global1@ecocycle.com"),
                    @ExampleObject(name = "Caso de email no existente", value = "invalido@ecocycle.com")
            }),
            @Parameter(name = "telefono", description = "Teléfono del depósito global (default: vacío)", required = false, examples = {
                    @ExampleObject(name = "Caso de teléfono existente", value = "2212222222"),
                    @ExampleObject(name = "Caso de teléfono no existente", value = "0000000000")
            }),
            @Parameter(name = "direccion", description = "Dirección del depósito global (default: vacío)", required = false, examples = {
                    @ExampleObject(name = "Caso de dirección existente", value = "Av. Siempreviva 742"),
                    @ExampleObject(name = "Caso de dirección no existente", value = "Calle Inexistente 999")
            }),
            @Parameter(name = "page", description = "Número de página", required = false, examples = {
                    @ExampleObject(name = "Caso de página existente", value = "1"),
                    @ExampleObject(name = "Caso de página no existente", value = "999")
            }),
            @Parameter(name = "pageSize", description = "Tamaño de la página", required = false, examples = {
                    @ExampleObject(name = "Caso de tamaño válido", value = "10"),
                    @ExampleObject(name = "Caso de tamaño inválido", value = "0")
            })
    })

    public ResponseEntity<?> getAllDepositosGlobales(
            @RequestParam(defaultValue = "global1@ecocycle.com", required = false) String email,
            @RequestParam(defaultValue = "2212222222", required = false) String telefono,
            @RequestParam(defaultValue = "Av. Siempreviva 742", required = false) String direccion,
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int pageSize
    ) {

            Page<DepositoGlobal> centros = depositoGlobalService.getAllDepositosGlobales(
                    email, telefono, direccion, page -1, pageSize
            );
            return ResponseEntity.ok(centros
                    .stream()
                    .map(CentroDTO::new)
                    .toList());
    }

    @PreAuthorize("hasAuthority('OBTENER_DEPOSITOS_GLOBALES')")
    @GetMapping("/{id}")
    @Operation(
            summary = "Obtener depósito global por ID",
            description = "Este endpoint devuelve un depósito global específico utilizando su ID.",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "ID del depósito global a buscar",
                            required = true,
                            examples = {
                                    @ExampleObject(name = "Caso de ID existente", value = "4"),
                                    @ExampleObject(name = "Caso de ID no encontrado", value = "1000")
                            }
                    )
            }
    )

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Depósito encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CentroDTO.class), examples = @ExampleObject(value = "{\"id\": 4, \"email\": \"global1@ecocycle.com\", \"telefono\": \"2212222222\", \"direccion\": \"Av. Siempreviva 742\"}"))),
            @ApiResponse(responseCode = "401", description = "Debe iniciar sesión", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "{\"error\": \"No está autenticado. Por favor, inicie sesión.\"}"))),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para acceder a este recurso", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "{\"error\": \"No tiene permisos para acceder a este recurso.\"}"))),
            @ApiResponse(responseCode = "404", description = "Depósito no encontrado", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "{\"error\": \"Depósito no encontrado\"}"))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"Error interno del servidor.\"}")
                    )
            )    })
    public ResponseEntity<?> getDepositoGlobalById(@PathVariable Long id) {
        try {
            DepositoGlobal centro = depositoGlobalService.getDepositoGlobalById(id);
            return ResponseEntity.ok(new CentroDTO(centro));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.builder().error("Depósito no encontrado").build());
        }
    }
}
