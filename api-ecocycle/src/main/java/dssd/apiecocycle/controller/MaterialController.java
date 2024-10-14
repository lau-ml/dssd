package dssd.apiecocycle.controller;

import dssd.apiecocycle.DTO.CentroDTO;
import dssd.apiecocycle.DTO.MaterialDTO;
import dssd.apiecocycle.model.Material;
import dssd.apiecocycle.service.MaterialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
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
            @ApiResponse(responseCode = "200", description = "Materiales encontrados", content = @Content(mediaType = "application/json", schema = @Schema(implementation = MaterialDTO.class), examples = @ExampleObject(value = "[{\"id\": 1, \"nombre\": \"Papel\", \"descripcion\": \"Material reciclable derivado de productos como periódicos, revistas, y documentos impresos.\"}, {\"id\": 2, \"nombre\": \"Plástico PET\", \"descripcion\": \"Comúnmente usado en botellas de bebidas, es un plástico transparente y ligero que se recicla para fabricar nuevas botellas o fibras textiles.\"}, {\"id\": 3, \"nombre\": \"Vidrio\", \"descripcion\": \"Incluye botellas y frascos. El vidrio reciclado puede reutilizarse indefinidamente sin pérdida de calidad.\"}]"))),
            @ApiResponse(responseCode = "401", description = "Debe iniciar sesión", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "{\"message\": \"No está autenticado. Por favor, inicie sesión.\"}"))),
            @ApiResponse(responseCode="403", description="No tiene permisos para acceder a este recurso", content=@Content(mediaType="text/plain", examples=@ExampleObject(value="{\"message\": \"No tiene permisos para acceder a este recurso.\"}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Error: [mensaje del error]")))
    })
    public ResponseEntity<?> obtenerMateriales() {
        try {
            List<Material> materiales = materialService.getAllMaterials();
            List<MaterialDTO> materialesDTO = materiales.stream()
                    .map(MaterialDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(materialesDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('OBTENER_PROVEEDORES_POR_MATERIAL')")
    @GetMapping("/get-proveedores/{materialId}")
    @Operation(summary = "Obtener proveedores por material", description = "Devuelve los centros de recepción que han entregado un material específico.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Proveedores encontrados", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CentroDTO.class), examples = @ExampleObject(value = "[{\"id\": 1, \"email\": \"centro1@example.com\", \"telefono\": \"123456789\", \"direccion\": \"Av. Siempreviva 123\"}, {\"id\": 2, \"email\": \"centro2@example.com\", \"telefono\": \"987654321\", \"direccion\": \"Calle Falsa 456\"}]"))),
            @ApiResponse(responseCode = "401", description = "Debe iniciar sesión", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "{\"message\": \"No está autenticado. Por favor, inicie sesión.\"}"))),
            @ApiResponse(responseCode="403", description="No tiene permisos para acceder a este recurso", content=@Content(mediaType="text/plain", examples=@ExampleObject(value="{\"message\": \"No tiene permisos para acceder a este recurso.\"}"))),
            @ApiResponse(responseCode = "404", description = "Material no encontrado", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Error: Material no encontrado"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Error: [mensaje del error]")))
    })
    public ResponseEntity<?> getProveedoresPorMaterial(@PathVariable Long materialId) {
        try {
            Material material = materialService.getMaterialById(materialId);
            if (material == null) {
                return ResponseEntity.status(404).body("Error: Material no encontrado");
            }

            List<CentroDTO> centroDeRecepcionDTOs = materialService.getProveedoresPorMaterial(material).stream()
                    .map(CentroDTO::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(centroDeRecepcionDTOs);
        } catch (RuntimeException e) {
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }
}
