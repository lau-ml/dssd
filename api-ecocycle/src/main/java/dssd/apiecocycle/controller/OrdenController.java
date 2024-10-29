package dssd.apiecocycle.controller;

import dssd.apiecocycle.DTO.OrdenDTO;
import dssd.apiecocycle.requests.OrdenDistribucionRequest;
import dssd.apiecocycle.exceptions.CantidadException;
import dssd.apiecocycle.exceptions.CentroInvalidoException;
import dssd.apiecocycle.exceptions.EstadoOrdenException;
import dssd.apiecocycle.model.EstadoOrden;
import dssd.apiecocycle.model.Orden;
import dssd.apiecocycle.requests.RegisterRequest;
import dssd.apiecocycle.response.ErrorResponse;
import dssd.apiecocycle.service.OrdenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/ordenes")
public class OrdenController {

    @Autowired
    private OrdenService ordenService;


    @PreAuthorize("hasAuthority('CONSULTAR_ORDEN_PROVEEDOR')" +
            " or hasAuthority('CONSULTAR_ORDEN_DEPOSITO')")
    @GetMapping("/{id}")
    @Operation(summary = "Obtener orden por ID",
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "ID del pedido",
                            required = true,
                            examples = {
                                    @ExampleObject(name = "Id existente", value = "1"),
                                    @ExampleObject(name = "Id no existente", value = "1000"),
                            }
                    )
            },
            security = @SecurityRequirement(name = "bearerAuth"), description = "Este endpoint devuelve una orden específica utilizando su ID.", responses = {
            @ApiResponse(responseCode = "200", description = "Orden encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = OrdenDTO.class),
                            examples = @ExampleObject(value = "{\n" +
                                    "  \"id\": 1,\n" +
                                    "  \"materialDTO\": {\n" +
                                    "    \"id\": 1,\n" +
                                    "    \"nombre\": \"Papel\",\n" +
                                    "    \"descripcion\": \"Material reciclable derivado de productos como periódicos, revistas, y documentos impresos.\"\n" +
                                    "  },\n" +
                                    "  \"cantidad\": 100,\n" +
                                    "  \"centroDeRecepcion\": {\n" +
                                    "    \"id\": 1,\n" +
                                    "    \"email\": \"mailcentro1@ecocycle.com\",\n" +
                                    "    \"telefono\": \"2211234567\",\n" +
                                    "    \"direccion\": \"Calle falsa 123\"\n" +
                                    "  },\n" +
                                    "  \"pedidoId\": 1,\n" +
                                    "  \"estadoOrden\": \"PENDIENTE\",\n" +
                                    "  \"fecha\": \"2023-10-23\",\n" +
                                    "  \"globalId\": 4,\n" +
                                    "  \"cantidadAceptada\": 0,\n" +
                                    "  \"lastUpdate\": \"2024-10-23T13:13:04.207762\"\n" +
                                    "}"))
            ),
            @ApiResponse(responseCode = "401", description = "Debe iniciar sesión", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"No está autenticado. Por favor, inicie sesión.\"}"))),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para acceder a este recurso", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"No tiene permisos para acceder a este recurso.\"}"))),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Orden no encontrada\"}"))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"Error interno del servidor.\"}")
                    )
            )})

    public ResponseEntity<?> getOrdenById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(new OrdenDTO(ordenService.getOrdenById(id)));
        } catch (AccessDeniedException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorResponse.builder().error(e.getMessage()).build());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.builder().error("Orden no encontrada").build());
        } catch (CentroInvalidoException e) {
            throw new RuntimeException(e);
        }
    }

    @PreAuthorize("hasAuthority('CONSULTAR_ORDEN_PROVEEDOR')")
    @GetMapping("/my-orders")
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
                                                            "    \"email\": \"mailcentro1@ecocycle.com\",\n" +
                                                            "    \"telefono\": \"2211234567\",\n" +
                                                            "    \"direccion\": \"Calle falsa 123\"\n" +
                                                            "  },\n" +
                                                            "  \"pedidoId\": 1,\n" +
                                                            "  \"estadoOrden\": \"PENDIENTE\",\n" +
                                                            "  \"fecha\": \"2023-10-23\",\n" +
                                                            "  \"globalId\": 4,\n" +
                                                            "  \"cantidadAceptada\": 0,\n" +
                                                            "  \"lastUpdate\": \"2024-10-23T13:13:04.207762\"\n" +
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
                                                            "    \"email\": \"mailcentro2@ecocycle.com\",\n" +
                                                            "    \"telefono\": \"2217654321\",\n" +
                                                            "    \"direccion\": \"Avenida siempre viva 456\"\n" +
                                                            "  },\n" +
                                                            "  \"pedidoId\": 2,\n" +
                                                            "  \"estadoOrden\": \"RECHAZADO\",\n" +
                                                            "  \"fecha\": \"2023-10-23\",\n" +
                                                            "  \"globalId\": 4,\n" +
                                                            "  \"cantidadAceptada\": 0,\n" +
                                                            "  \"lastUpdate\": \"2024-10-23T13:13:04.207762\"\n" +

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
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "{\"error\": \"No está autenticado. Por favor, inicie sesión.\"}")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "403",
                            description = "No tiene permisos para acceder a este recurso",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "{\"error\": \"No tiene permisos para acceder a este recurso.\"}")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Error interno del servidor",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(value = "{\"error\": \"Error interno del servidor.\"}")
                            )
                    )
            }
    )
    @Parameters({
            @Parameter(name = "cantidad", description = "Cantidad de la orden", examples = {
                    @ExampleObject(name = "Cantidad coincidente", value = "100"),
                    @ExampleObject(name = "Cantidad no coincidente", value = "1000")
            }, required = false),
            @Parameter(name = "globalId", description = "Id del depósito global", examples = {
                    @ExampleObject(name = "Id coincidente", value = "1"),
                    @ExampleObject(name = "Id no coincidente", value = "1000")
            }, required = false),
            @Parameter(name = "materialName", description = "Nombre del material a filtrar", examples = {
                    @ExampleObject(name = "Material coincidente", value = "Papel"),
                    @ExampleObject(name = "Material no coincidente", value = "Cartón")
            }, required = false),

            @Parameter(name = "estado", description = "Estado de la orden", examples = {
                    @ExampleObject(name = "Estado de orden", value = "PENDIENTE"),
            }
                    , required = false),

            @Parameter(name = "fechaOrden", description = "Fecha de la orden en formato ISO (yyyy-MM-dd)", examples = {
                    @ExampleObject(name = "Caso de fecha coincidente", value = "2024-10-25"),
                    @ExampleObject(name = "Caso de fecha no coincidente", value = "2024-10-12")
            }, required = false),
            @Parameter(name = "lastUpdate", description = "Fecha de última actualización de la orden en formato ISO (yyyy-MM-dd)", examples = {
                    @ExampleObject(name = "Caso de fecha coincidente", value = "2024-10-25"),
                    @ExampleObject(name = "Caso de fecha no coincidente", value = "2024-10-12")
            }, required = false),

            @Parameter(name = "page", description = "Número de página", required = false, examples = {
                    @ExampleObject(name = "Caso de página existente", value = "1"),
                    @ExampleObject(name = "Caso de página no existente", value = "999")
            }),
            @Parameter(name = "pageSize", description = "Tamaño de la página", required = false, examples = {
                    @ExampleObject(name = "Caso de tamaño válido", value = "10"),
                    @ExampleObject(name = "Caso de tamaño inválido", value = "0")
            })
    })
    public ResponseEntity<?> getMyOrders(

            @RequestParam(required = false) Integer cantidad,
            @RequestParam(required = false) Long globalId,
            @RequestParam(defaultValue = "", required = false) String materialName,
            @RequestParam(required = false) EstadoOrden estado,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaOrden,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate lastUpdate,
            @RequestParam(defaultValue = "1", required = false) int page,
            @RequestParam(defaultValue = "10", required = false) int pageSize

    ) throws CentroInvalidoException {
        return ResponseEntity.ok(ordenService
                .getMyOrders(
                        cantidad,
                        globalId,
                        materialName,
                        estado,
                        fechaOrden,
                        lastUpdate,
                        page - 1,
                        pageSize)

                .stream()
                .map(OrdenDTO::new)
                .toList());
    }

    // ROL CENTER
    @PreAuthorize("hasAuthority('GENERAR_ORDEN')")
    @PostMapping("/generate-order")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"), summary = "Generar una nueva orden de distribución", description = "Este endpoint permite generar una nueva orden de distribución para un pedido específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Orden creada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrdenDTO.class), examples = @ExampleObject(value = "{\n" +
                    "  \"id\": 1,\n" +
                    "  \"materialDTO\": {\n" +
                    "    \"id\": 1,\n" +
                    "    \"nombre\": \"Papel\",\n" +
                    "    \"descripcion\": \"Material reciclable derivado de productos como periódicos, revistas, y documentos impresos.\"\n" +
                    "  },\n" +
                    "  \"cantidad\": 100,\n" +
                    "  \"centroDeRecepcion\": {\n" +
                    "    \"id\": 1,\n" +
                    "    \"email\": \"mailcentro1@ecocycle.com\",\n" +
                    "    \"telefono\": \"2211234567\",\n" +
                    "    \"direccion\": \"Calle falsa 123\"\n" +
                    "  },\n" +
                    "  \"pedidoId\": 1,\n" +
                    "  \"estadoOrden\": \"PENDIENTE\",\n" +
                    "  \"fecha\": \"2023-10-23\",\n" +
                    "  \"globalId\": 4,\n" +
                    "  \"cantidadAceptada\": 0,\n" +
                    "  \"lastUpdate\": \"2024-10-23T13:13:04.207762\"\n" +
                    "}"))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida",
                    content = @Content(mediaType = "application/json",
                            examples = {
                                    @ExampleObject(name = "Cantidad menor o igual a cero", value = "{\"error\": \"La cantidad de la orden debe ser mayor a cero.\"}"),
                                    @ExampleObject(name = "Cantidad mayor que la cantidad faltante", value = "{\"error\": \"La cantidad de la orden no puede ser mayor que la cantidad faltante.\"}")
                            }))
            ,
            @ApiResponse(responseCode = "401", description = "Debe iniciar sesión", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"No está autenticado. Por favor, inicie sesión.\"}"))),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para acceder a este recurso", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"No tiene permisos para acceder a este recurso.\"}"))),
            @ApiResponse(responseCode = "404", description = "Material o centro de recepción no encontrado",
                    content = @Content(mediaType = "application/json",
                            examples = {
                                    @ExampleObject(name = "Centro de recepción no encontrado", value = "{\"error\": \"Centro de recepción no encontrado\"}"),
                                    @ExampleObject(name = "Material no encontrado", value = "{\"error\": \"Material no encontrado\"}")
                            }))
            ,
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"Error interno del servidor.\"}")
                    )
            )})
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos necesarios para registrar una nueva orden probar con mailcentro1@ecocycle.com",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RegisterRequest.class),
                    examples = {
                            @ExampleObject(name = "Caso de orden exitosa", value = "{\n" +
                                    "  \"materialId\": \"2\",\n" +
                                    "  \"pedidoId\": \"2\",\n" +
                                    "  \"cantidad\": \"1\"\n" +
                                    "}"),
                            @ExampleObject(name = "Caso de pedido fallido por pendiente", value = "{\n" +
                                    "  \"materialId\": \"1\",\n" +
                                    "  \"pedidoId\": \"1\",\n" +
                                    "  \"cantidad\": \"10\"\n" +

                                    "}"),
                    }
            )
    )
    public ResponseEntity<?> generateOrder(@Valid @RequestBody OrdenDistribucionRequest ordenDistribucionRequest) {
        try {
            Orden orden = ordenService.generarOrden(ordenDistribucionRequest);
            return ResponseEntity.status(HttpStatus.CREATED).body(new OrdenDTO(orden));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.builder().error(e.getMessage()).build());
        } catch (CantidadException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.builder().error(e.getMessage()).build());
        } catch (CentroInvalidoException e) {
            throw new RuntimeException(e);
        }
    }

    // ROL DEPOSITO
    @PreAuthorize("hasAuthority('ENTREGAR_ORDEN')")
    @PatchMapping("/{id}/entregado")
    @Operation(summary = "Entregar orden", parameters = {
            @Parameter(
                    name = "id",
                    description = "ID del pedido",
                    required = true,
                    examples = {
                            @ExampleObject(name = "Id orden enviada para entregar", value = "9"),
                            @ExampleObject(name = "Id orden no lista para entregar", value = "1"),
                    }
            )
    },
            description = "Este endpoint permite marcar una orden como entregada utilizando su ID.", responses = {
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
                    "    \"email\": \"mailcentro2@ecocycle.com\",\n" +
                    "    \"telefono\": \"2217654321\",\n" +
                    "    \"direccion\": \"Calle verdadera 123\"\n" +
                    "  },\n" +
                    "  \"pedidoId\": 1,\n" +
                    "  \"estadoOrden\": \"ENTREGADO\",\n" +
                    "  \"fecha\": \"2023-10-23\",\n" +
                    "  \"globalId\": 4,\n" +
                    "  \"cantidadAceptada\": 4,\n" +
                    "  \"lastUpdate\": \"2024-10-23T13:13:04.207762\"\n" +
                    "}"))),
            @ApiResponse(responseCode = "400", description = "La orden ya ha sido entregada o rechazada", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"No se puede entregar la orden\"}"))),
            @ApiResponse(responseCode = "401", description = "Debe iniciar sesión", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"No está autenticado. Por favor, inicie sesión.\"}"))),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para acceder a este recurso", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"No tiene permisos para acceder a este recurso.\"}"))),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Orden no encontrada\"}"))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"Error interno del servidor.\"}")
                    )
            )})
    public ResponseEntity<?> entregarOrden(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(new OrdenDTO(ordenService.entregarOrden(id)));
        } catch (EstadoOrdenException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.builder().error(e.getMessage()).build());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.builder().error("Orden no encontrada").build());
        } catch (CentroInvalidoException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorResponse.builder().error(e.getMessage()).build());
        }
    }

    // ROL DEPOSITO
    @PreAuthorize("hasAuthority('RECHAZAR_ORDEN')")
    @PatchMapping("/{id}/rechazar")
    @Operation(summary = "Rechazar orden", parameters = {
            @Parameter(
                    name = "id",
                    description = "ID del pedido",
                    required = true,
                    examples = {
                            @ExampleObject(name = "Id orden pendiente para rechazar", value = "4"),
                            @ExampleObject(name = "Id orden no pendiente sin poder rechazar", value = "9"),
                    }
            )
    },
            security = @SecurityRequirement(name = "bearerAuth"), description = "Este endpoint permite marcar una orden como rechazada utilizando su ID.", responses = {
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
                    "    \"email\": \"mailcentro2@ecocycle.com\",\n" +
                    "    \"telefono\": \"2217654321\",\n" +
                    "    \"direccion\": \"Calle verdadera 123\"\n" +
                    "  },\n" +
                    "  \"pedidoId\": 1,\n" +
                    "  \"estadoOrden\": \"RECHAZADO\",\n" +
                    "  \"fecha\": \"2023-10-23\",\n" +
                    "  \"globalId\": 4,\n" +
                    "  \"cantidadAceptada\": 0,\n" +
                    "  \"lastUpdate\": \"2024-10-23T13:13:04.207762\"\n" +
                    "}"))),
            @ApiResponse(responseCode = "400", description = "La orden ya ha sido entregada o rechazada", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"La orden no puede ser rechazada\"}"))),
            @ApiResponse(responseCode = "401", description = "Debe iniciar sesión", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"No está autenticado. Por favor, inicie sesión.\"}"))),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para acceder a este recurso", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"No tiene permisos para acceder a este recurso.\"}"))),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Orden no encontrada\"}"))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"Error interno del servidor.\"}")
                    )
            )})
    public ResponseEntity<?> rechazarOrden(@PathVariable Long id) throws CentroInvalidoException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(new OrdenDTO(ordenService.rechazarOrden(id)));
        } catch (EstadoOrdenException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.builder().error(e.getMessage()).build());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.builder().error("Orden no encontrada").build());
        }
    }

    // ROL DEPOSITO
    @PreAuthorize("hasAuthority('ACEPTAR_ORDEN')")
    @PatchMapping("/{id}/aceptar")
    @Operation(summary = "Aceptar orden", parameters = {
            @Parameter(
                    name = "id",
                    description = "ID del pedido",
                    required = true,
                    examples = {
                            @ExampleObject(name = "Id orden pendiente para aceptar", value = "3"),
                            @ExampleObject(name = "Id orden no pendiente sin poder aceptar", value = "9"),
                    }
            )
    },
            security = @SecurityRequirement(name = "bearerAuth"), description = "Este endpoint permite marcar una orden como aceptada utilizando su ID.", responses = {
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
                    "    \"email\": \"mailcentro2@ecocycle.com\",\n" +
                    "    \"telefono\": \"2217654321\",\n" +
                    "    \"direccion\": \"Calle verdadera 123\"\n" +
                    "  },\n" +
                    "  \"pedidoId\": 1,\n" +
                    "  \"estadoOrden\": \"ACEPTADO\",\n" +
                    "  \"fecha\": \"2023-10-23\",\n" +
                    "  \"globalId\": 4,\n" +
                    "  \"cantidadAceptada\": 4,\n" +
                    "  \"lastUpdate\": \"2024-10-23T13:13:04.207762\"\n" +
                    "}"))),
            @ApiResponse(responseCode = "400", description = "La orden no puede ser aceptada debido a un error de validación",
                    content = @Content(mediaType = "application/json",
                            examples = {
                                    @ExampleObject(name = "Cantidad mayor que la orden", value = "{\"error\": \"La cantidad aceptada no puede ser mayor que la cantidad de la orden\"}"),
                                    @ExampleObject(name = "Cantidad menor o igual a cero", value = "{\"error\": \"La cantidad aceptada debe ser mayor a cero\"}"),
                                    @ExampleObject(name = "Cantidad mayor que la cantidad faltante", value = "{\"error\": \"La cantidad aceptada no puede ser mayor que la cantidad faltante\"}"),
                                    @ExampleObject(name = "Orden inaceptable", value = "{\"error\": \"No se puede aceptar la orden\"}")
                            }
                    )
            )
            ,
            @ApiResponse(responseCode = "401", description = "Debe iniciar sesión", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"No está autenticado. Por favor, inicie sesión.\"}"))),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para acceder a este recurso", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"No tiene permisos para acceder a este recurso.\"}"))),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Orden no encontrada\"}"))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"Error interno del servidor.\"}")
                    )
            )})
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos necesarios para aceptar una orden",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    examples = {
                            @ExampleObject(name = "Caso de orden aceptada para id 1", value = "{\n" +
                                    "  \"cantidad\": \"4\"\n" +
                                    "}"),
                            @ExampleObject(name = "Caso de orden aceptada fallida por cantidad", value = "{\n" +
                                    "  \"cantidad\": \"0\"\n" +
                                    "}")
                    }
            )
    )
    public ResponseEntity<?> aceptarOrden(@PathVariable Long id,
                                          @RequestParam(required = true) Long cantidad) throws CentroInvalidoException{
        try {
            return ResponseEntity.ok(new OrdenDTO(ordenService.aceptarOrden(id, cantidad)));
        } catch (CantidadException | EstadoOrdenException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.builder().error(e.getMessage()).build());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.builder().error("Orden no encontrada").build());
        }
    }


    @PreAuthorize("hasAuthority('PREPARAR_ORDEN')")
    @PatchMapping("/{id}/preparar")
    @Operation(summary = "Preparar orden",parameters = {
            @Parameter(
                    name = "id",
                    description = "ID del pedido",
                    required = true,
                    examples = {
                            @ExampleObject(name = "Id orden aceptada para preparar", value = "6"),
                            @ExampleObject(name = "Id orden no aceptada sin poder aceptar", value = "10"),
                    }
            )
    }, security = @SecurityRequirement(name = "bearerAuth"), description = "Este endpoint permite marcar una orden en preparación utilizando su ID.", responses = {
            @ApiResponse(responseCode = "200", description = "Orden puesta a preparar con éxito", content = @Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = OrdenDTO.class), examples = @ExampleObject(value = "{\n"
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
                    "    \"email\": \"mailcentro2@ecocycle.com\",\n" +
                    "    \"telefono\": \"2217654321\",\n" +
                    "    \"direccion\": \"Calle verdadera 123\"\n" +
                    "  },\n" +
                    "  \"pedidoId\": 1,\n" +
                    "  \"estadoOrden\": \"PREPARANDO\",\n" +
                    "  \"fecha\": \"2023-10-23\",\n" +
                    "  \"globalId\": 4,\n" +
                    "  \"cantidadAceptada\": 4,\n" +
                    "  \"lastUpdate\": \"2024-10-23T13:13:04.207762\"\n" +
                    "}"))),
            @ApiResponse(responseCode = "400", description = "Error en la preparación", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"No se puede preparar la orden\"}"))),
            @ApiResponse(responseCode = "401", description = "Debe iniciar sesión", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"No está autenticado. Por favor, inicie sesión.\"}"))),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para acceder a este recurso", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"No tiene permisos para acceder a este recurso.\"}"))),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Orden no encontrada\"}"))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"Error interno del servidor.\"}")
                    )
            )})
    public ResponseEntity<?> prepararOrden(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(new OrdenDTO(ordenService.prepararOrden(id)));
        } catch (EstadoOrdenException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.builder().error(e.getMessage()).build());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.builder().error("Orden no encontrada").build());
        }
    }

    @PreAuthorize("hasAuthority('PREPARAR_ORDEN')")
    @PatchMapping("/{id}/preparada")
    @Operation(summary = "Orden preparada", parameters = {
            @Parameter(
                    name = "id",
                    description = "ID del pedido",
                    required = true,
                    examples = {
                            @ExampleObject(name = "Id orden en preparación para marcar como preparada", value = "7"),
                            @ExampleObject(name = "Id orden sin en el estado de preparación", value = "10"),
                    }
            )
    },security = @SecurityRequirement(name = "bearerAuth"), description = "Este endpoint permite marcar una orden como preparada utilizando su ID.", responses = {
            @ApiResponse(responseCode = "200", description = "Orden marcada como preparada con éxito", content = @Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = OrdenDTO.class), examples = @ExampleObject(value = "{\n"
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
                    "    \"email\": \"mailcentro2@ecocycle.com\",\n" +
                    "    \"telefono\": \"2217654321\",\n" +
                    "    \"direccion\": \"Calle verdadera 123\"\n" +
                    "  },\n" +
                    "  \"pedidoId\": 1,\n" +
                    "  \"estadoOrden\": \"PREPARADA\",\n" +
                    "  \"fecha\": \"2023-10-23\",\n" +
                    "  \"globalId\": 4,\n" +
                    "  \"cantidadAceptada\": 4,\n" +
                    "  \"lastUpdate\": \"2024-10-23T13:13:04.207762\"\n" +
                    "}"))),
            @ApiResponse(responseCode = "400", description = "Error en la preparación", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"No se puede marcar la orden como preparada\"}"))),
            @ApiResponse(responseCode = "401", description = "Debe iniciar sesión", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"No está autenticado. Por favor, inicie sesión.\"}"))),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para acceder a este recurso", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"No tiene permisos para acceder a este recurso.\"}"))),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Orden no encontrada\"}"))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"Error interno del servidor.\"}")
                    )
            )})
    public ResponseEntity<?> ordenPreparada(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(new OrdenDTO(ordenService.ordenPreparada(id)));
        } catch (EstadoOrdenException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.builder().error(e.getMessage()).build());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.builder().error("Orden no encontrada").build());
        }
    }

    @PreAuthorize("hasAuthority('ENVIAR_ORDEN')")
    @PatchMapping("/{id}/enviar")
    @Operation(summary = "Enviar orden", security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "ID del pedido",
                            required = true,
                            examples = {
                                    @ExampleObject(name = "Id orden ya preparada para enviarla", value = "8"),
                                    @ExampleObject(name = "Id orden no preparada sin poder enviarla", value = "10"),
                            }
                    )
            },
            description = "Este endpoint permite marcar una orden como enviada utilizando su ID.", responses = {
            @ApiResponse(responseCode = "200", description = "Orden enviada con éxito", content = @Content(mediaType = "application/json", schema = @io.swagger.v3.oas.annotations.media.Schema(implementation = OrdenDTO.class), examples = @ExampleObject(value = "{\n"
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
                    "    \"email\": \"mailcentro2@ecocycle.com\",\n" +
                    "    \"telefono\": \"2217654321\",\n" +
                    "    \"direccion\": \"Calle verdadera 123\"\n" +
                    "  },\n" +
                    "  \"pedidoId\": 1,\n" +
                    "  \"estadoOrden\": \"ENVIADA\",\n" +
                    "  \"fecha\": \"2023-10-23\",\n" +
                    "  \"globalId\": 4,\n" +
                    "  \"cantidadAceptada\": 4,\n" +
                    "  \"lastUpdate\": \"2024-10-23T13:13:04.207762\"\n" +
                    "}"))),
            @ApiResponse(responseCode = "400", description = "Error de envío", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"No se puede enviar la orden\"}"))),
            @ApiResponse(responseCode = "401", description = "Debe iniciar sesión", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"No está autenticado. Por favor, inicie sesión.\"}"))),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para acceder a este recurso", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"No tiene permisos para acceder a este recurso.\"}"))),
            @ApiResponse(responseCode = "404", description = "Orden no encontrada", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Orden no encontrada\"}"))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"Error interno del servidor.\"}")
                    )
            )})
    public ResponseEntity<?> enviarOrden(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(new OrdenDTO(ordenService.enviarOrden(id)));
        } catch (EstadoOrdenException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponse.builder().error(e.getMessage()).build());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.builder().error("Orden no encontrada").build());
        }
    }

}
