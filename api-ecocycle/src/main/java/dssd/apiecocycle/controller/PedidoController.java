package dssd.apiecocycle.controller;

import dssd.apiecocycle.DTO.CreatePedidoDTO;
import dssd.apiecocycle.DTO.OrdenDTO;
import dssd.apiecocycle.DTO.PedidoDTO;
import dssd.apiecocycle.exceptions.CantidadException;
import dssd.apiecocycle.exceptions.CentroInvalidoException;
import dssd.apiecocycle.model.EstadoOrden;
import dssd.apiecocycle.model.Pedido;
import dssd.apiecocycle.response.MessageResponse;
import dssd.apiecocycle.service.OrdenService;
import dssd.apiecocycle.service.PedidoService;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/pedido")
public class PedidoController {
    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private OrdenService ordenService;

    // ROL AMBOS
    @PreAuthorize("hasAuthority('CONSULTAR_TODOS_PEDIDOS') or hasAuthority('CONSULTAR_PEDIDO_PROPIO')")
    @GetMapping("/{id}")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"), summary = "Obtener pedido por ID", description = "Este endpoint devuelve un pedido específico utilizando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoDTO.class), examples = @ExampleObject(value = "{\"id\": 1, \"material\": {\"id\": 1, \"nombre\": \"Papel\", \"descripcion\": \"Material reciclable derivado de productos como periódicos, revistas, y documentos impresos.\"}, \"fecha\": \"2024-10-12\", \"cantidad\": 100, \"depositoGlobalId\": 4, \"lastUpdate\": \"2024-10-12T12:00:00\"}"))),
            @ApiResponse(responseCode = "401", description = "Debe iniciar sesión", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "{\"message\": \"No está autenticado. Por favor, inicie sesión.\"}"))),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para acceder a este recurso", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "{\"message\": \"No tiene permisos para acceder a este recurso.\"}"))),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "{\"message\": \"Pedido no encontrado\"}"))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Error interno del servidor.\"}")
                    )
            )
    })
    public ResponseEntity<?> getPedidoById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(new PedidoDTO(pedidoService.obtenerPedido(id)));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MessageResponse.builder().message("Pedido no encontrado").build());
        } catch (CentroInvalidoException e) {
            throw new RuntimeException(e);
        }
    }


    // ROL DEPOSITO
    @PreAuthorize("hasAuthority('GENERAR_PEDIDO')")
    @PostMapping("/create")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"), summary = "Crear un nuevo pedido", description = "Este endpoint permite crear un nuevo pedido para un material específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido creado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida",
                    content = @Content(mediaType = "text/plain",
                            examples = {
                                    @ExampleObject(name = "Cantidad menor o igual a cero", value = "{\"message\": \"La cantidad del pedido debe ser mayor a cero\"}")
                            }))
            ,
            @ApiResponse(responseCode = "401", description = "Debe iniciar sesión", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "{\"message\": \"No está autenticado. Por favor, inicie sesión.\"}"))),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para acceder a este recurso", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "{\"message\": \"No tiene permisos para acceder a este recurso.\"}"))),
            @ApiResponse(responseCode = "404", description = "Material o depósito global no encontrado", content = @Content(mediaType = "text/plain")),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Error interno del servidor.\"}")
                    )
            )
    })
    public ResponseEntity<?> createPedido(@RequestBody CreatePedidoDTO createPedidoDTO) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(new PedidoDTO(pedidoService.crearPedido(createPedidoDTO)));
        } catch (CentroInvalidoException e) {
            throw new RuntimeException(e);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MessageResponse.builder().message(e.getMessage()).build());
        } catch (CantidadException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(MessageResponse.builder().message(e.getMessage()).build());
        }

    }

    // ROL DEPOSITO
    @PreAuthorize("hasAuthority('CONSULTAR_ORDENES_PEDIDO')")
    @GetMapping("/{id}/ordenes")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"), summary = "Obtener órdenes por ID de pedido", description = "Este endpoint permite obtener todas las órdenes asociadas a un pedido específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Órdenes encontradas", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrdenDTO[].class))),
            @ApiResponse(responseCode = "401", description = "Debe iniciar sesión", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"message\": \"No está autenticado. Por favor, inicie sesión.\"}"))),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para acceder a este recurso", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"message\": \"No tiene permisos para acceder a este recurso.\"}"))),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"message\": \"Pedido no encontrado\"}"))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Error interno del servidor.\"}")
                    )
            )})
    @Parameters({
      @Parameter(name = "cantidad", description = "Cantidad de la orden", example = "100", required = false),
      @Parameter(name = "materialName", description = "Nombre del material a filtrar", example = "Papel", required = false),
      @Parameter(name = "estado", description = "Estado de la orden", example = "PENDIENTE", required = false),
      @Parameter(name = "fechaOrden", description = "Fecha de la orden en formato ISO (yyyy-MM-dd)", example = "2024-10-12", required = false),
      @Parameter(name = "lastUpdate", description = "Fecha de última actualización de la orden en formato ISO (yyyy-MM-dd'T'HH:mm:ss)", example = "2024-10-12T12:00:00", required = false),
      @Parameter(name = "page", description = "Número de página (inicia en 1)", example = "1", required = false),
      @Parameter(name = "pageSize", description = "Cantidad de elementos por página", example = "10", required = false)
    })
    public ResponseEntity<?> getOrdenesPorPedidoId(@PathVariable Long id,
                                                   @RequestParam(required = false) Integer cantidad,
                                                   @RequestParam(defaultValue = "" ,required = false) String materialName,
                                                   @RequestParam(required = false) EstadoOrden estado,
                                                   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaOrden,
                                                   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastUpdate,
                                                   @RequestParam(defaultValue = "1", required = false) int page,
                                                   @RequestParam(defaultValue = "10", required = false) int pageSize
    ) {
        try {
            return ResponseEntity.ok(ordenService
                    .getAllOrdersByPedidoIdAndArgs(id, cantidad, materialName, estado, fechaOrden, lastUpdate, page - 1, pageSize)
                    .stream()
                    .map(OrdenDTO::new)
                    .toList());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MessageResponse.builder().message("Pedido no encontrado").build());
        }
    }

    // ROL CENTRO

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('CONSULTAR_TODOS_PEDIDOS')")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedidos encontrados", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoDTO.class), examples = @ExampleObject(value = "[{\"id\": 1, \"material\": {\"id\": 1, \"nombre\": \"Papel\", \"descripcion\": \"Material reciclable...\"}, \"fecha\": \"2024-10-12\", \"cantidad\": 100, \"depositoGlobalId\": 4}, {\"id\": 4, \"material\": {\"id\": 1, \"nombre\": \"Papel\", \"descripcion\": \"Material reciclable...\"}, \"fecha\": \"2024-10-12\", \"cantidad\": 79, \"depositoGlobalId\": 5, \"lastUpdate\": \"2024-10-12T12:00:00\"}]"))),
            @ApiResponse(responseCode = "401", description = "Debe iniciar sesión", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"message\": \"No está autenticado. Por favor, inicie sesión.\"}"))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado - El usuario no tiene los permisos necesarios",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Acceso denegado. No tienes permisos para realizar esta acción.\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Error interno del servidor.\"}")
                    )
            )
    })
    @Parameters({
            @Parameter(name = "materialNombre", description = "Nombre del material a filtrar", example = "Papel", required = false),
            @Parameter(name = "abastecido", description = "Filtrar pedidos abastecidos (true) o no abastecidos (false)", example = "true", required = false),
            @Parameter(name = "fechaPedido", description = "Fecha específica del pedido a filtrar en formato ISO (yyyy-MM-dd)", example = "2024-10-12", required = false),
            @Parameter(name = "lastUpdate", description = "Fecha de última actualización del pedido en formato ISO (yyyy-MM-dd'T'HH:mm:ss)", example = "2024-10-12T12:00:00", required = false),
            @Parameter(name = "cantidad", description = "Cantidad  del pedido para filtrar", example = "100", required = false),
            @Parameter(name = "page", description = "Número de página (inicia en 1)", example = "1", required = false),
            @Parameter(name = "pageSize", description = "Cantidad de elementos por página", example = "10", required = false)

    })
    public ResponseEntity<?> getAllPedidos(@RequestParam(defaultValue = "", required = false) String materialNombre,
                                           @RequestParam(required = false) Boolean abastecido,
                                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate fechaPedido,
                                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastUpdate,
                                           @RequestParam(required = false) Integer cantidad,
                                           @RequestParam(defaultValue = "1", required = false) int page,
                                           @RequestParam(defaultValue = "" + Integer.MAX_VALUE, required = false) int pageSize

    ) {
        Page<Pedido> pedidos = pedidoService.getAllPedidos(page - 1, pageSize, materialNombre, abastecido, fechaPedido,lastUpdate, cantidad);
        return ResponseEntity.ok(pedidos
                .stream()
                .map(PedidoDTO::new)
                .toList());
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedidos encontrados", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoDTO.class), examples = @ExampleObject(value = "[{\"id\": 1, \"material\": {\"id\": 1, \"nombre\": \"Papel\", \"descripcion\": \"Material reciclable...\"}, \"fecha\": \"2024-10-12\", \"cantidad\": 100, \"depositoGlobalId\": 4}, {\"id\": 4, \"material\": {\"id\": 1, \"nombre\": \"Papel\", \"descripcion\": \"Material reciclable...\"}, \"fecha\": \"2024-10-12\", \"cantidad\": 79, \"depositoGlobalId\": 5, \"lastUpdate\": \"2024-10-12T12:00:00\" }]"))),
            @ApiResponse(responseCode = "401", description = "Debe iniciar sesión", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"message\": \"No está autenticado. Por favor, inicie sesión.\"}"))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado - El usuario no tiene los permisos necesarios",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Acceso denegado. No tienes permisos para realizar esta acción.\"}")
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Error interno del servidor.\"}")
                    )
            )
    })
    @Parameters({
            @Parameter(name = "materialNombre", description = "Nombre del material a filtrar", example = "Papel", required = false),
            @Parameter(name = "abastecido", description = "Filtrar pedidos abastecidos (true) o no abastecidos (false)", example = "true", required = false),
            @Parameter(name = "fechaPedido", description = "Fecha específica del pedido a filtrar en formato ISO (yyyy-MM-dd)", example = "2024-10-12", required = false),
            @Parameter(name = "lastUpdate", description = "Fecha de última actualización del pedido en formato ISO (yyyy-MM-dd'T'HH:mm:ss)", example = "2024-10-12T12:00:00", required = false),
            @Parameter(name = "cantidad", description = "Cantidad del pedido para filtrar", example = "100", required = false),
            @Parameter(name = "page", description = "Número de página (inicia en 1)", example = "1", required = false),
            @Parameter(name = "pageSize", description = "Cantidad de elementos por página", example = "10", required = false)

    })
    @GetMapping("/mis-pedidos")
    @PreAuthorize("hasAuthority('CONSULTAR_PEDIDO_PROPIO')")
    public ResponseEntity<?> getMisPedidos(@RequestParam(defaultValue = "", required = false) String materialNombre,
                                           @RequestParam(required = false) Boolean abastecido,
                                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate fechaPedido,
                                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime lastUpdate,
                                           @RequestParam(required = false) Integer cantidad,
                                           @RequestParam(defaultValue = "1", required = false) int page,
                                           @RequestParam(defaultValue = "" + Integer.MAX_VALUE, required = false) int pageSize

    ) throws CentroInvalidoException {

        Page<Pedido> pedidos = pedidoService.getMisPedidos(page - 1, pageSize, materialNombre, abastecido, fechaPedido,lastUpdate, cantidad);
        return ResponseEntity.ok(pedidos
                .stream()
                .map(PedidoDTO::new)
                .toList());
    }

}
