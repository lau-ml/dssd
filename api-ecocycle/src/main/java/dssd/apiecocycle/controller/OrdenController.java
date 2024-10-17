package dssd.apiecocycle.controller;

import dssd.apiecocycle.DTO.OrdenDTO;
import dssd.apiecocycle.DTO.OrdenDistribucionDTO;
import dssd.apiecocycle.exceptions.CantidadException;
import dssd.apiecocycle.exceptions.CentroInvalidoException;
import dssd.apiecocycle.exceptions.EstadoOrdenException;
import dssd.apiecocycle.model.Orden;
import dssd.apiecocycle.response.MessageResponse;
import dssd.apiecocycle.service.OrdenService;
import dssd.apiecocycle.service.PedidoService;
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
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ordenes")
public class OrdenController {

    @Autowired
    private OrdenService ordenService;


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
            @ApiResponse(responseCode = "403", description = "No tiene permisos para acceder a este recurso", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "{\"message\": \"No tiene permisos para acceder a este recurso.\"}"))),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Orden no encontrada"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "{\"message\": \"Error interno del servidor\\\"}\"")))
    })

    public ResponseEntity<?> getOrdenById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(new OrdenDTO(ordenService.getOrdenById(id)));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(MessageResponse.builder().message(e.getMessage()).build());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MessageResponse.builder().message("Orden no encontrada").build());
        } catch (CentroInvalidoException e) {
            throw new RuntimeException(e);
        }
    }

    @Operation(
            summary = "Obtener mis órdenes",
            security = @SecurityRequirement(name = "bearerAuth"),
            description = "Este endpoint devuelve una lista de todas las órdenes del usuario autenticado.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Órdenes encontradas",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = OrdenDTO.class),
                                    examples = {
                                            @ExampleObject(
                                                    name = "Ejemplo 1",
                                                    value = "{\n" +
                                                            "  \"id\": 1,\n" +
                                                            "  \"materialDTO\": {\n" +
                                                            "    \"id\": 1,\n" +
                                                            "    \"nombre\": \"Papel\",\n" +
                                                            "    \"descripcion\": \"Material reciclable derivado de productos como periódicos, revistas, y documentos impresos.\"\n" +
                                                            "  },\n" +
                                                            "  \"cantidad\": 100,\n" +
                                                            "  \"centroDeRecepcion\": {\n" +
                                                            "    \"id\": 1,\n" +
                                                            "    \"email\": \"mailCentro1@ecocycle.com\",\n" +
                                                            "    \"telefono\": \"221-22224\",\n" +
                                                            "    \"direccion\": \"Calle falsa 123\"\n" +
                                                            "  },\n" +
                                                            "  \"pedidoId\": 1,\n" +
                                                            "  \"estadoOrden\": \"PENDIENTE\"\n" +
                                                            "}"
                                            ),
                                            @ExampleObject(
                                                    name = "Ejemplo 2",
                                                    value = "{\n" +
                                                            "  \"id\": 2,\n" +
                                                            "  \"materialDTO\": {\n" +
                                                            "    \"id\": 2,\n" +
                                                            "    \"nombre\": \"Vidrio\",\n" +
                                                            "    \"descripcion\": \"Incluye botellas y frascos. El vidrio reciclado puede reutilizarse indefinidamente sin pérdida de calidad.\"\n" +
                                                            "  },\n" +
                                                            "  \"cantidad\": 50,\n" +
                                                            "  \"centroDeRecepcion\": {\n" +
                                                            "    \"id\": 2,\n" +
                                                            "    \"email\": \"mailCentro2@ecocycle.com\",\n" +
                                                            "    \"telefono\": \"223-33456\",\n" +
                                                            "    \"direccion\": \"Avenida siempre viva 456\"\n" +
                                                            "  },\n" +
                                                            "  \"pedidoId\": 2,\n" +
                                                            "  \"estadoOrden\": \"RECHAZADO\"\n" +
                                                            "}"
                                            )
                                    }
                            )
                    )
                    ,
                    @ApiResponse(
                            responseCode = "401",
                            description = "Debe iniciar sesión",
                            content = @Content(
                                    mediaType = "text/plain",
                                    examples = @ExampleObject(value = "{\"message\": \"No está autenticado. Por favor, inicie sesión.\"}")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "No tiene permisos para acceder a este recurso",
                            content = @Content(
                                    mediaType = "text/plain",
                                    examples = @ExampleObject(value = "{\"message\": \"No tiene permisos para acceder a este recurso.\"}")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error interno del servidor",
                            content = @Content(
                                    mediaType = "text/plain",
                                    examples = @ExampleObject(value = "{\"message\": \"Error interno del servidor.\"}")
                            )
                    )
            }
    )
    public ResponseEntity<?> getMyOrders() {
        try {
            return ResponseEntity.ok(ordenService
                    .getMyOrders()
                    .stream()
                    .map(OrdenDTO::new)
                    .collect(Collectors.toList()));
        } catch (CentroInvalidoException e) {
            throw new RuntimeException(e);
        }
    }

    // ROL CENTER
    @PreAuthorize("hasAuthority('GENERAR_ORDEN')")
    @PostMapping("/generate-order")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"), summary = "Generar una nueva orden de distribución", description = "Este endpoint permite generar una nueva orden de distribución para un pedido específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Orden creada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrdenDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida",
                    content = @Content(mediaType = "text/plain",
                            examples = {
                                    @ExampleObject(name = "Cantidad menor o igual a cero", value = "{\"message\": \"La cantidad de la orden debe ser mayor a cero.\"}"),
                                    @ExampleObject(name = "Cantidad mayor que la cantidad faltante", value = "{\"message\": \"La cantidad de la orden no puede ser mayor que la cantidad faltante.\"}")
                            }))
            ,
            @ApiResponse(responseCode = "401", description = "Debe iniciar sesión", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "{\"message\": \"No está autenticado. Por favor, inicie sesión.\"}"))),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para acceder a este recurso", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "{\"message\": \"No tiene permisos para acceder a este recurso.\"}"))),
            @ApiResponse(responseCode = "404", description = "Material o centro de recepción no encontrado",
                    content = @Content(mediaType = "text/plain",
                            examples = {
                                    @ExampleObject(name = "Centro de recepción no encontrado", value = "{\"message\": \"Centro de recepción no encontrado\"}"),
                                    @ExampleObject(name = "Material no encontrado", value = "{\"message\": \"Material no encontrado\"}")
                            }))
            ,
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "{\"message\": \"Error interno del servidor\\\"}\"")))
    })
    public ResponseEntity<?> generateOrder(@RequestBody OrdenDistribucionDTO ordenDistribucionDTO) {
        try {
            Orden orden = ordenService.generarOrden(ordenDistribucionDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(new OrdenDTO(orden));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MessageResponse.builder().message(e.getMessage()).build());
        }
        catch (CantidadException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(MessageResponse.builder().message(e.getMessage()).build());
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
            @ApiResponse(responseCode = "400", description = "La orden ya ha sido entregada o rechazada", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "{\"message\": \"No se puede entregar la orden\"}"))),
            @ApiResponse(responseCode = "401", description = "Debe iniciar sesión", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "{\"message\": \"No está autenticado. Por favor, inicie sesión.\"}"))),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para acceder a este recurso", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "{\"message\": \"No tiene permisos para acceder a este recurso.\"}"))),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "{\"message\": \"Orden no encontrada\"}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "{\"message\": \"Error interno del servidor\\\"}\"")))
    })
    public ResponseEntity<?> entregarOrden(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(new OrdenDTO(ordenService.entregarOrden(id)));
        } catch (EstadoOrdenException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(MessageResponse.builder().message(e.getMessage()).build());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MessageResponse.builder().message("Orden no encontrada").build());
        } catch (CentroInvalidoException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(MessageResponse.builder().message(e.getMessage()).build());
        }
    }

    // ROL DEPOSITO
    @PreAuthorize("hasAuthority('RECHAZAR_ORDEN')")
    @PutMapping("/{id}/rechazar")
    @Operation(summary = "Rechazar orden", security = @SecurityRequirement(name = "bearerAuth"), description = "Este endpoint permite marcar una orden como rechazada utilizando su ID.", responses = {
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
            @ApiResponse(responseCode = "400", description = "La orden ya ha sido entregada o rechazada", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "{\"message\": \"La orden no puede ser rechazada\"}"))),
            @ApiResponse(responseCode = "401", description = "Debe iniciar sesión", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "{\"message\": \"No está autenticado. Por favor, inicie sesión.\"}"))),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para acceder a este recurso", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "{\"message\": \"No tiene permisos para acceder a este recurso.\"}"))),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "{\"message\": \"Orden no encontrada\"}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "{\"message\": \"Error interno del servidor\\\"}\"")))
    })
    public ResponseEntity<?> rechazarOrden(@PathVariable Long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(new OrdenDTO(ordenService.rechazarOrden(id)));
        } catch (EstadoOrdenException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(MessageResponse.builder().message(e.getMessage()).build());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MessageResponse.builder().message("Orden no encontrada").build());
        } catch (CentroInvalidoException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(MessageResponse.builder().message(e.getMessage()).build());
        }
    }

    // ROL DEPOSITO
    @PreAuthorize("hasAuthority('ACEPTAR_ORDEN')")
    @PutMapping("/{id}/aceptar")
    @Operation(summary = "Aceptar orden", security = @SecurityRequirement(name = "bearerAuth"), description = "Este endpoint permite marcar una orden como aceptada utilizando su ID.", responses = {
            @ApiResponse(responseCode = "200", description = "Orden aceptada con éxito", content = @Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = OrdenDTO.class), examples = @ExampleObject(value = "{\n"
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
                    "  \"estadoOrden\": \"ACEPTADO\"\n" +
                    "}"))),
            @ApiResponse(responseCode = "400", description = "La orden ya ha sido entregada o aceptada", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value ="{\"message\": \"No se puede aceptar la orden\"}"))),
            @ApiResponse(responseCode = "401", description = "Debe iniciar sesión", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "{\"message\": \"No está autenticado. Por favor, inicie sesión.\"}"))),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para acceder a este recurso", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "{\"message\": \"No tiene permisos para acceder a este recurso.\"}"))),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "{\"message\": \"Orden no encontrada\"}"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "{\"message\": \"Error interno del servidor\\\"}\"")))
    })
    public ResponseEntity<?> aceptarOrden(@PathVariable Long id) {
        try {

            return ResponseEntity.ok(new OrdenDTO(ordenService.aceptarOrden(id)));
        } catch (EstadoOrdenException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(MessageResponse.builder().message(e.getMessage()).build());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MessageResponse.builder().message("Orden no encontrada").build());
        } catch (CentroInvalidoException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(MessageResponse.builder().message(e.getMessage()).build());
        }
    }
}
