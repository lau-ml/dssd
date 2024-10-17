package dssd.apiecocycle.controller;

import dssd.apiecocycle.DTO.CentroDTO;
import dssd.apiecocycle.DTO.MaterialDTO;
import dssd.apiecocycle.exceptions.CentroInvalidoException;
import dssd.apiecocycle.exceptions.ProveedoresException;
import dssd.apiecocycle.model.Material;
import dssd.apiecocycle.response.MessageResponse;
import dssd.apiecocycle.service.MaterialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
            @ApiResponse(responseCode = "401", description = "Debe iniciar sesión", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"message\": \"No está autenticado. Por favor, inicie sesión.\"}"))),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para acceder a este recurso", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"message\": \"No tiene permisos para acceder a este recurso.\"}"))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Error interno del servidor.\"}")
                    )
            )    })
    public ResponseEntity<?> obtenerMateriales() {
        List<Material> materiales = materialService.getAllMaterials();
        return ResponseEntity.ok(materiales
                .stream()
                .map(MaterialDTO::new)
                .toList());
    }

    @PreAuthorize("hasAuthority('OBTENER_PROVEEDORES_POR_MATERIAL')")
    @GetMapping("/get-proveedores/{materialId}")
    @Operation(summary = "Obtener proveedores por material", description = "Devuelve los centros de recepción que han entregado un material específico.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proveedores encontrados",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CentroDTO.class),
                            examples = {
                                    @ExampleObject(name = "Proveedores encontrados", value = "[{\"id\": 1, \"email\": \"centro1@example.com\", \"telefono\": \"123456789\", \"direccion\": \"Av. Siempreviva 123\"}, {\"id\": 2, \"email\": \"centro2@example.com\", \"telefono\": \"987654321\", \"direccion\": \"Calle Falsa 456\"}]"),
                                    @ExampleObject(name = "Sin proveedores encontrados", value = "[]")
                            })),
            @ApiResponse(responseCode = "401", description = "Debe iniciar sesión", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"message\": \"No está autenticado. Por favor, inicie sesión.\"}"))),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para acceder a este recurso", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"message\": \"No tiene permisos para acceder a este recurso.\"}"))),
            @ApiResponse(responseCode = "404", description = "Material no encontrado", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"message\": \"Material no encontrado\"}"))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Error interno del servidor.\"}")
                    )
            )    })
    public ResponseEntity<?> getProveedoresPorMaterial(@PathVariable Long materialId) {
        try {

            return ResponseEntity.ok(materialService
                    .getProveedoresPorMaterial(materialId)
                    .stream()
                    .map(CentroDTO::new)
                    .toList());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body(MessageResponse.builder().message("Material no encontrado").build());
        }
    }

    @PreAuthorize("hasAuthority('INSCRIBIR_PROVEEDOR')")
    @GetMapping("/inscribir-proveedor/{materialId}")
    @Operation(summary = "Inscribir proveedor", description = "Inscribe un centro de recepción como proveedor de un material específico.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proveedor inscripto correctamente", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"message\": \"Centro de recepción inscripto correctamente como proveedor de material\"}"))),
            @ApiResponse(responseCode = "401", description = "Debe iniciar sesión", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"message\": \"No está autenticado. Por favor, inicie sesión.\"}"))),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para acceder a este recurso", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"message\": \"No tiene permisos para acceder a este recurso.\"}"))),
            @ApiResponse(responseCode = "404", description = "Material o centro de recepción no encontrado",
                    content = @Content(mediaType = "application/json",
                            examples = {
                                    @ExampleObject(name = "Centro de recepción no encontrado", value = "{\"message\": \"Centro de recepción no encontrado\"}"),
                                    @ExampleObject(name = "Material no encontrado", value = "{\"message\": \"Material no encontrado\"}")
                            }))
            ,
            @ApiResponse(responseCode = "409", description = "El centro de recepción ya es proveedor de este material", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"message\": \"El proveedor ya se encuentra asociado al material\"}"))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Error interno del servidor.\"}")
                    )
            )    })
    public ResponseEntity<?> inscribirProveedor(@PathVariable Long materialId) {
        try {
            materialService.agregarProveedor(materialId);
            return ResponseEntity.ok(MessageResponse.builder().message("Centro de recepción inscripto correctamente como proveedor de material").build());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(404).body(MessageResponse.builder().message(e.getMessage()).build());
        } catch (ProveedoresException e) {
            return ResponseEntity.status(409).body(MessageResponse.builder().message(e.getMessage()).build());
        } catch (CentroInvalidoException e) {
            throw new RuntimeException(e);
        }
    }


}
