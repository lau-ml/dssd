package dssd.apiecocycle.controller;

import dssd.apiecocycle.DTO.CentroDTO;
import dssd.apiecocycle.DTO.MaterialDTO;
import dssd.apiecocycle.exceptions.CentroInvalidoException;
import dssd.apiecocycle.exceptions.ProveedoresException;
import dssd.apiecocycle.model.Material;
import dssd.apiecocycle.response.ErrorResponse;
import dssd.apiecocycle.response.MessageResponse;
import dssd.apiecocycle.service.MaterialService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/material")
public class MaterialController {

    @Autowired
    private MaterialService materialService;


    @PreAuthorize("hasAuthority('OBTENER_MATERIALES')")
    @GetMapping("/get-materials")
    @Operation(summary = "Obtener materiales", description = "Este endpoint devuelve una lista de todos los materiales reciclables.",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Materiales encontrados",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MaterialDTO.class),
                            examples = {
                                    @ExampleObject(name = "Materiales encontrados", value = "[{\"id\": 1, \"nombre\": \"Papel\", \"descripcion\": \"Material reciclable derivado de productos como periódicos, revistas, y documentos impresos.\"}, {\"id\": 2, \"nombre\": \"Plástico PET\", \"descripcion\": \"Comúnmente usado en botellas de bebidas, es un plástico transparente y ligero que se recicla para fabricar nuevas botellas o fibras textiles.\"}, {\"id\": 3, \"nombre\": \"Vidrio\", \"descripcion\": \"Incluye botellas y frascos. El vidrio reciclado puede reutilizarse indefinidamente sin pérdida de calidad.\"}]"),
                                    @ExampleObject(name = "Sin materiales encontrados", value = "[]")
                            }))
            ,
            @ApiResponse(responseCode = "401", description = "Debe iniciar sesión", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"No está autenticado. Por favor, inicie sesión.\"}"))),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para acceder a este recurso", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"No tiene permisos para acceder a este recurso.\"}"))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"Error interno del servidor.\"}")
                    )
            )    })
    @Parameters({
            @Parameter(name = "nombre", description = "Nombre del material", required = false, examples = {
                    @ExampleObject(name = "Caso de nombre válido", value = "Papel"),
                    @ExampleObject(name = "Caso de nombre no válido", value = "Baterías")
            }),
            @Parameter(name = "descripcion", description = "Descripción del material", required = false, examples = {
                    @ExampleObject(name = "Caso de descripción válida", value = "Material reciclable derivado de productos como periódicos, revistas, y documentos impresos."),
                    @ExampleObject(name = "Caso de descripción no válida", value = "1234")
            }),@Parameter(name = "page", description = "Número de página", required = false, examples = {
                    @ExampleObject(name = "Caso de página válida", value = "1"),
                    @ExampleObject(name = "Caso de página no existente", value = "999")
            }),
            @Parameter(name = "pageSize", description = "Tamaño de la página", required = false, examples = {
                    @ExampleObject(name = "Caso de tamaño válido", value = "10"),
                    @ExampleObject(name = "Caso de tamaño inválido", value = "0")
            })
    })
    public ResponseEntity<?> obtenerMateriales(
            @RequestParam(defaultValue = "", required = false) String nombre,
            @RequestParam(defaultValue = "", required = false) String descripcion,
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int pageSize
    ) {
        Page<Material> materiales = materialService.getAllMaterials(
                nombre,
                descripcion,
                page - 1,
                pageSize
        );
        return ResponseEntity.ok(materiales
                .stream()
                .map(MaterialDTO::new)
                .toList());
    }

    @PreAuthorize("hasAuthority('OBTENER_PROVEEDORES_POR_MATERIAL')")
    @GetMapping("/get-proveedores/{materialId}")
    @Operation(
            summary = "Obtener proveedores por ID de material",
            description = "Este endpoint devuelve los proveedores del material proporcionado",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(
                            name = "materialId",
                            description = "ID del material a buscar",
                            required = true,
                            examples = {
                                    @ExampleObject(name = "Caso de ID existente con proveedores", value = "1"),
                                    @ExampleObject(name = "Caso de ID existente sin proveedores", value = "5"),
                                    @ExampleObject(name = "Caso de ID no encontrado", value = "1000")
                            }
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proveedores encontrados",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CentroDTO.class),
                            examples = {
                                    @ExampleObject(name = "Proveedores encontrados", value = "[{\"id\": 1, \"email\": \"mailcentro1@ecocycle.com\", \"telefono\": \"2211234567\", \"direccion\": \"Calle falsa 123\"}]"),
                                    @ExampleObject(name = "Sin proveedores encontrados", value = "[]")
                            })),
            @ApiResponse(responseCode = "401", description = "Debe iniciar sesión", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"No está autenticado. Por favor, inicie sesión.\"}"))),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para acceder a este recurso", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"No tiene permisos para acceder a este recurso.\"}"))),
            @ApiResponse(responseCode = "404", description = "Material no encontrado", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Material no encontrado\"}"))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"Error interno del servidor.\"}")
                    )
            )    })
    @Parameters({
            @Parameter(name = "email", description = "Email del centro de recepción (default: vacío)", required = false, examples = {
                    @ExampleObject(name = "Caso de email existente", value = "mailcentro1@ecocycle.com"),
                    @ExampleObject(name = "Caso de email no existente", value = "correo_invalido@ecocycle.com")
            }),
            @Parameter(name = "telefono", description = "Teléfono del centro de recepción (default: vacío)", required = false, examples = {
                    @ExampleObject(name = "Caso de teléfono existente", value = "2211234567"),
                    @ExampleObject(name = "Caso de teléfono no existente", value = "0000000000")
            }),
            @Parameter(name = "direccion", description = "Dirección del centro de recepción (default: vacío)", required = false, examples = {
                    @ExampleObject(name = "Caso de dirección existente", value = "Calle falsa 123"),
                    @ExampleObject(name = "Caso de dirección no existente", value = "Calle inexistente 999")
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
    public ResponseEntity<?> getProveedoresPorMaterial(@PathVariable Long materialId,
                                                         @RequestParam(defaultValue = "", required = false) String email,
                                                            @RequestParam(defaultValue = "", required = false) String telefono,
                                                            @RequestParam(defaultValue = "", required = false) String direccion,
                                                       @RequestParam(defaultValue = "1", required = false) int page,
                                                       @RequestParam(defaultValue = "10", required = false) int pageSize) {
        try {

            return ResponseEntity.ok(materialService
                    .getProveedoresPorMaterial(materialId,
                            email,
                            telefono,
                            direccion,
                            page - 1,
                            pageSize)
                    .stream()
                    .map(CentroDTO::new)
                    .toList());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body(ErrorResponse.builder().error("Material no encontrado").build());
        }
    }

    @PreAuthorize("hasAuthority('INSCRIBIR_PROVEEDOR')")
    @GetMapping("/inscribir-proveedor/{materialId}")
    @Operation(
            summary = "Incribir proveedor por logueado(el control del material ya enviado corre por parte del centro de recolección/rececpión)",
            description = "Este endpoint permite inscribir al proveedor logueado según el material proporcionado",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(
                            name = "materialId",
                            description = "Probar con mailcentro1@ecocycle.com logueado",
                            required = true,
                            examples = {
                                    @ExampleObject(name = "Caso de inscripción exitosa", value = "5"),
                                    @ExampleObject(name = "Caso de inscripción fallida ", value = "1"),
                                    @ExampleObject(name = "Caso de ID no encontrado de material", value = "1000")
                            }
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proveedor inscripto correctamente", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"message\": \"Centro de recepción inscripto correctamente como proveedor de material\"}"))),
            @ApiResponse(responseCode = "401", description = "Debe iniciar sesión", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"No está autenticado. Por favor, inicie sesión.\"}"))),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para acceder a este recurso", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"No tiene permisos para acceder a este recurso.\"}"))),
            @ApiResponse(responseCode = "404", description = "Material o centro de recepción no encontrado",
                    content = @Content(mediaType = "application/json",
                            examples = {
                                    @ExampleObject(name = "Centro de recepción no encontrado", value = "{\"error\": \"Centro de recepción no encontrado\"}"),
                                    @ExampleObject(name = "Material no encontrado", value = "{\"error\": \"Material no encontrado\"}")
                            }))
            ,
            @ApiResponse(responseCode = "409", description = "El centro de recepción ya es proveedor de este material", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"El proveedor ya se encuentra asociado al material\"}"))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"Error interno del servidor.\"}")
                    )
            )    })
    public ResponseEntity<?> inscribirProveedor(@PathVariable Long materialId) {
        try {
            materialService.agregarProveedor(materialId);
            return ResponseEntity.ok(MessageResponse.builder().message("Centro de recepción inscripto correctamente como proveedor de material").build());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body(ErrorResponse.builder().error(e.getMessage()).build());
        } catch (ProveedoresException e) {
            return ResponseEntity.status(409).body(ErrorResponse.builder().error(e.getMessage()).build());
        } catch (CentroInvalidoException e) {
            throw new RuntimeException(e);
        }
    }


}
