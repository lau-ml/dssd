package dssd.apiecocycle.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dssd.apiecocycle.DTO.OrdenDTO;
import dssd.apiecocycle.model.EstadoOrden;
import dssd.apiecocycle.model.Orden;
import dssd.apiecocycle.service.OrdenService;
import dssd.apiecocycle.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/ordenes")
public class OrdenController {

    @Autowired
    private OrdenService ordenService;

    @Autowired
    private PedidoService pedidoService;

    @PreAuthorize("hasAuthority('CONSULTAR_ORDEN')")
    @GetMapping("/{id}")
    @Operation(summary = "Obtener orden por ID", security = @SecurityRequirement(name = "bearerAuth"), description = "Este endpoint devuelve una orden específica utilizando su ID.", responses = {
            @ApiResponse(responseCode = "200", description = "Orden encontrada", content = @Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = OrdenDTO.class), examples = @ExampleObject(value = "{\n"
                    +
                    "  \"id\": 1,\n" +
                    "  \"materialDTO\": {\n" +
                    "    \"id\": 1,\n" +
                    "    \"nombre\": \"Papel\",\n" +
                    "    \"descripcion\": \"Material reciclable derivado de productos como periódicos, revistas, y documentos impresos.\"\n"
                    +
                    "  },\n" +
                    "  \"cantidad\": 100,\n" +
                    "  \"centroDeRecepcion\": {\n" +
                    "    \"id\": 1,\n" +
                    "    \"email\": \"mailCentro1@ecocycle.com\",\n" +
                    "    \"telefono\": \"221-22224\",\n" +
                    "    \"direccion\": \"Calle falsa 123\"\n" +
                    "  },\n" +
                    "  \"pedidoId\": 1,\n" +
                    "  \"estadoOrden\": \"PEDNDIENTE\"\n" +
                    "}"))),
            @ApiResponse(responseCode = "401", description = "Debe iniciar sesión", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "{\"message\": \"No está autenticado. Por favor, inicie sesión.\"}"))),
            @ApiResponse(responseCode="403", description="No tiene permisos para acceder a este recurso", content=@Content(mediaType="text/plain", examples=@ExampleObject(value="No tiene permisos para acceder a este recurso"))),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Orden no encontrada"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Error: [mensaje del error]")))
    })
    public ResponseEntity<?> getOrdenById(@PathVariable Long id) {
        try {
            Orden orden = ordenService.getOrdenById(id);
            if (orden != null) {
                return ResponseEntity.ok(new OrdenDTO(orden));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Orden no encontrada");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // ROL DEPOSITO
    @PreAuthorize("hasAuthority('ENTREGAR_ORDEN')")
    @PutMapping("/{id}/entregado")
    @Operation(summary = "Entregar orden", description = "Este endpoint permite marcar una orden como entregada utilizando su ID.", responses = {
            @ApiResponse(responseCode = "200", description = "Orden entregada con éxito", content = @Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = OrdenDTO.class), examples = @ExampleObject(value = "{\n"
                    +
                    "  \"id\": 1,\n" +
                    "  \"material\": {\n" +
                    "    \"id\": 1,\n" +
                    "    \"nombre\": \"Papel\",\n" +
                    "    \"descripcion\": \"Material reciclable derivado de productos como periódicos, revistas, y documentos impresos.\"\n"
                    +
                    "  },\n" +
                    "  \"cantidad\": 20,\n" +
                    "  \"centroDeRecepcion\": {\n" +
                    "    \"id\": 2,\n" +
                    "    \"email\": \"mailCentro2@ecocycle.com\",\n" +
                    "    \"telefono\": \"221-11114\",\n" +
                    "    \"direccion\": \"Calle verdadera 123\"\n" +
                    "  },\n" +
                    "  \"pedidoId\": 1,\n" +
                    "  \"estadoOrden\": \"ENTREGADO\"\n" +
                    "}"))),
            @ApiResponse(responseCode = "400", description = "La orden ya ha sido entregada o rechazada", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "La orden ya ha sido entregada o rechazada"))),
            @ApiResponse(responseCode = "401", description = "Debe iniciar sesión", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "{\"message\": \"No está autenticado. Por favor, inicie sesión.\"}"))),
            @ApiResponse(responseCode="403", description="No tiene permisos para acceder a este recurso", content=@Content(mediaType="text/plain", examples=@ExampleObject(value="No tiene permisos para acceder a este recurso"))),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Orden no encontrada"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Error: [mensaje del error]")))
    })
    public ResponseEntity<?> entregarOrden(@PathVariable Long id) {
        try {
            Orden orden = ordenService.getOrdenById(id);
            if (orden != null) {
                if (orden.getEstado().equals(EstadoOrden.PEDNDIENTE)) {
                    orden.setEstado(EstadoOrden.ENTREGADO);
                    ordenService.updateOrden(orden);
                    pedidoService.updateCantSupplied(orden.getPedido(), orden.getCantidad());
                    return ResponseEntity.ok(new OrdenDTO(orden));
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("La orden ya ha sido entregada o rechazada");
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Orden no encontrada");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // ROL DEPOSITO
    @PreAuthorize("hasAuthority('RECHAZAR_ORDEN')")
    @PutMapping("/{id}/rechazar")
    @Operation(summary = "Rechazar orden",security = @SecurityRequirement(name = "bearerAuth"), description = "Este endpoint permite marcar una orden como rechazada utilizando su ID.", responses = {
            @ApiResponse(responseCode = "200", description = "Orden rechazada con éxito", content = @Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = OrdenDTO.class), examples = @ExampleObject(value = "{\n"
                    +
                    "  \"id\": 1,\n" +
                    "  \"material\": {\n" +
                    "    \"id\": 1,\n" +
                    "    \"nombre\": \"Papel\",\n" +
                    "    \"descripcion\": \"Material reciclable derivado de productos como periódicos, revistas, y documentos impresos.\"\n"
                    +
                    "  },\n" +
                    "  \"cantidad\": 20,\n" +
                    "  \"centroDeRecepcion\": {\n" +
                    "    \"id\": 2,\n" +
                    "    \"email\": \"mailCentro2@ecocycle.com\",\n" +
                    "    \"telefono\": \"221-11114\",\n" +
                    "    \"direccion\": \"Calle verdadera 123\"\n" +
                    "  },\n" +
                    "  \"pedidoId\": 1,\n" +
                    "  \"estadoOrden\": \"RECHAZADO\"\n" +
                    "}"))),
            @ApiResponse(responseCode = "400", description = "La orden ya ha sido entregada o rechazada", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "La orden ya ha sido entregada o rechazada"))),
            @ApiResponse(responseCode = "401", description = "Debe iniciar sesión", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "{\"message\": \"No está autenticado. Por favor, inicie sesión.\"}"))),
            @ApiResponse(responseCode="403", description="No tiene permisos para acceder a este recurso", content=@Content(mediaType="text/plain", examples=@ExampleObject(value="No tiene permisos para acceder a este recurso"))),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Orden no encontrada"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Error: [mensaje del error]")))
    })
    public ResponseEntity<?> rechazarOrden(@PathVariable Long id) {
        try {
            Orden orden = ordenService.getOrdenById(id);
            if (orden != null) {
                if (orden.getEstado().equals(EstadoOrden.PEDNDIENTE)) {
                    orden.setEstado(EstadoOrden.RECHAZADO);
                    ordenService.updateOrden(orden);
                    return ResponseEntity.ok(new OrdenDTO(orden));
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("La orden ya ha sido entregada o rechazada");
                }
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Orden no encontrada");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
