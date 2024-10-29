package dssd.apiecocycle.controller;

import dssd.apiecocycle.DTO.CreatePedidoDTO;
import dssd.apiecocycle.DTO.OrdenDTO;
import dssd.apiecocycle.DTO.PedidoDTO;
import dssd.apiecocycle.exceptions.CantidadException;
import dssd.apiecocycle.exceptions.CentroInvalidoException;
import dssd.apiecocycle.model.EstadoOrden;
import dssd.apiecocycle.model.Pedido;
import dssd.apiecocycle.requests.RegisterRequest;
import dssd.apiecocycle.response.ErrorResponse;
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
    @Operation(
            summary = "Obtener pedido por ID",
            description = "Este endpoint permite obtener un pedido específico por su ID.",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "ID del pedido",
                            required = true,
                            examples = {
                                    @ExampleObject(name = "Id existente de pedido", value = "1"),
                                    @ExampleObject(name = "Id no existente", value = "1000"),
                            }
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoDTO.class), examples = @ExampleObject(value = "{\"id\": 1, \"material\": {\"id\": 1, \"nombre\": \"Papel\", \"descripcion\": \"Material reciclable derivado de productos como periódicos, revistas, y documentos impresos.\"}, \"fecha\": \"2024-10-12\", \"cantidad\": 100, \"depositoGlobalId\": 4, \"lastUpdate\": \"2024-10-12T12:00:00\"}"))),
            @ApiResponse(responseCode = "401", description = "Debe iniciar sesión", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "{\"error\": \"No está autenticado. Por favor, inicie sesión.\"}"))),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para acceder a este recurso", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "{\"error\": \"No tiene permisos para acceder a este recurso.\"}"))),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "{\"error\": \"Pedido no encontrado\"}"))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"Error interno del servidor.\"}")
                    )
            )
    })
    public ResponseEntity<?> getPedidoById(@PathVariable Long id) throws CentroInvalidoException{
        try {
            return ResponseEntity.ok(new PedidoDTO(pedidoService.obtenerPedido(id)));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MessageResponse.builder().message("Pedido no encontrado").build());
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
                                    @ExampleObject(name = "Cantidad menor o igual a cero", value = "{\"error\": \"La cantidad del pedido debe ser mayor a cero\"}")
                            }))
            ,
            @ApiResponse(responseCode = "401", description = "Debe iniciar sesión", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "{\"error\": \"No está autenticado. Por favor, inicie sesión.\"}"))),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para acceder a este recurso", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "{\"error\": \"No tiene permisos para acceder a este recurso.\"}"))),
            @ApiResponse(responseCode = "404", description = "Material o depósito global no encontrado", content = @Content(mediaType = "text/plain")),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"Error interno del servidor.\"}")
                    )
            )
    })
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Datos necesarios para registrar un nuevo pedido",
            required = true,
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = RegisterRequest.class),
                    examples = {
                            @ExampleObject(name = "Caso de pedido Exitoso", value = "{\n" +
                                    "  \"materialId\": \"1\",\n" +
                                    "  \"cantidad\": \"1\"\n" +
                                    "}"),
                            @ExampleObject(name = "Caso de pedido fallido", value = "{\n" +
                                    "  \"materialId\": \"1000\",\n" +
                                    "  \"cantidad\": \"10000\"\n" +

                                    "}"),
                    }
            )
    )
    public ResponseEntity<?> createPedido(@RequestBody CreatePedidoDTO createPedidoDTO) throws CentroInvalidoException {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(new PedidoDTO(pedidoService.crearPedido(createPedidoDTO)));
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MessageResponse.builder().message(e.getMessage()).build());
        } catch (CantidadException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(MessageResponse.builder().message(e.getMessage()).build());
        }

    }

    // ROL DEPOSITO
    @PreAuthorize("hasAuthority('CONSULTAR_ORDENES_PEDIDO')")
    @GetMapping("/{id}/ordenes")
    @Operation(
            summary = "Obtener órdenes según pedido por ID",
            description = "Este endpoint permite obtener las órdenes de un pedido específico por su ID.",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(
                            name = "id",
                            description = "ID del pedido del cual se quiere obtener sus órdens",
                            required = true,
                            examples = {
                                    @ExampleObject(name = "Id existente de pedido con órdenes", value = "1"),
                                    @ExampleObject(name = "Id existente sin órdenes", value = "5"),
                                    @ExampleObject(name = "Id no existente", value = "1000"),
                            }
                    )
            }
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Órdenes encontradas", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrdenDTO[].class))),
            @ApiResponse(responseCode = "401", description = "Debe iniciar sesión", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"No está autenticado. Por favor, inicie sesión.\"}"))),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para acceder a este recurso", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"No tiene permisos para acceder a este recurso.\"}"))),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"Pedido no encontrado\"}"))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"Error interno del servidor.\"}")
                    )
            )})
    @Parameters({
            @Parameter(name = "cantidad", description = "Cantidad de la orden", examples = {
                    @ExampleObject(name = "Cantidad coincidente", value = "100"),
                    @ExampleObject(name = "Cantidad no coincidente", value = "1000")
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
    public ResponseEntity<?> getOrdenesPorPedidoId(@PathVariable Long id,
                                                   @RequestParam(required = false) Integer cantidad,
                                                   @RequestParam(defaultValue = "", required = false) String materialName,
                                                   @RequestParam(required = false) EstadoOrden estado,
                                                   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaOrden,
                                                   @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate lastUpdate,
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.builder().error("Pedido no encontrado").build());
        }
    }

    // ROL CENTRO

    @GetMapping("/all")
    @PreAuthorize("hasAuthority('CONSULTAR_TODOS_PEDIDOS')")
    @Operation(
            summary = "Obtener todos los pedidos",
            description = "Este endpoint permite obtener todos los pedidos.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedidos encontrados", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoDTO.class), examples = @ExampleObject(value = "[{\"id\": 1, \"material\": {\"id\": 1, \"nombre\": \"Papel\", \"descripcion\": \"Material reciclable derivado de productos como periódicos, revistas, y documentos impresos.\"}, \"fecha\": \"2024-10-12\", \"cantidad\": 100, \"depositoGlobalId\": 4}, {\"id\": 4, \"material\": {\"id\": 1, \"nombre\": \"Papel\", \"descripcion\": \"Material reciclable derivado de productos como periódicos, revistas, y documentos impresos.\"}, \"fecha\": \"2024-10-12\", \"cantidad\": 79, \"depositoGlobalId\": 5, \"lastUpdate\": \"2024-10-12T12:00:00\"}]"))),
            @ApiResponse(responseCode = "401", description = "Debe iniciar sesión", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"No está autenticado. Por favor, inicie sesión.\"}"))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado - El usuario no tiene los permisos necesarios",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"Acceso denegado. No tienes permisos para realizar esta acción.\"}")
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
    })
    @Parameters({
            @Parameter(name = "materialNombre", description = "Nombre del material a filtrar", required = false, examples = {
                    @ExampleObject(name = "Caso de material existente", value = "Papel"),
                    @ExampleObject(name = "Caso de material no existente", value = "VidrioInexistente")
            }),
            @Parameter(name = "abastecido", description = "Filtrar pedidos abastecidos (true) o no abastecidos (false)", required = false, examples = {
                    @ExampleObject(name = "Caso de pedidos abastecidos", value = "true"),
                    @ExampleObject(name = "Caso de pedidos no abastecidos", value = "false")
            }),
            @Parameter(name = "fechaPedido", description = "Fecha específica del pedido a filtrar en formato ISO (yyyy-MM-dd)", required = false, examples = {
                    @ExampleObject(name = "Caso de fecha existente", value = "2024-10-25"),
                    @ExampleObject(name = "Caso de fecha no existente", value = "2024-01-01")
            }),
            @Parameter(name = "lastUpdate", description = "Fecha de última actualización del pedido en formato ISO (yyyy-MM-dd)", required = false, examples = {
                    @ExampleObject(name = "Caso de última actualización existente", value = "2024-10-12"),
                    @ExampleObject(name = "Caso de última actualización no existente", value = "2024-01-01")
            }),
            @Parameter(name = "cantidad", description = "Cantidad del pedido para filtrar", required = false, examples = {
                    @ExampleObject(name = "Caso de cantidad válida", value = "100"),
                    @ExampleObject(name = "Caso de cantidad inválida", value = "-10") // Cantidad no válida
            }),
            @Parameter(name = "page", description = "Número de página (inicia en 1)", required = false, examples = {
                    @ExampleObject(name = "Caso de página existente", value = "1"),
                    @ExampleObject(name = "Caso de página no existente", value = "999") // Supone que no hay tantas páginas
            }),
            @Parameter(name = "pageSize", description = "Cantidad de elementos por página", required = false, examples = {
                    @ExampleObject(name = "Caso de tamaño válido", value = "10"),
                    @ExampleObject(name = "Caso de tamaño inválido", value = "0") // Tamaño no válido, ya que debe ser mayor que cero
            })
    })
    public ResponseEntity<?> getAllPedidos(@RequestParam(defaultValue = "", required = false) String materialNombre,
                                           @RequestParam(required = false) Boolean abastecido,
                                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaPedido,
                                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate lastUpdate,
                                           @RequestParam(required = false) Integer cantidad,
                                           @RequestParam(defaultValue = "1", required = false) int page,
                                           @RequestParam(defaultValue = "" + Integer.MAX_VALUE, required = false) int pageSize

    ) {
        Page<Pedido> pedidos = pedidoService.getAllPedidos(page - 1, pageSize, materialNombre, abastecido, fechaPedido, lastUpdate, cantidad);
        return ResponseEntity.ok(pedidos
                .stream()
                .map(PedidoDTO::new)
                .toList());
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedidos encontrados", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoDTO.class), examples = @ExampleObject(value = "[{\"id\": 1, \"material\": {\"id\": 1, \"nombre\": \"Papel\", \"descripcion\": \"Material reciclable derivado de productos como periódicos, revistas, y documentos impresos.\"}, \"fecha\": \"2024-10-12\", \"cantidad\": 100, \"depositoGlobalId\": 4}, {\"id\": 4, \"material\": {\"id\": 1, \"nombre\": \"Papel\", \"descripcion\": \"Material reciclable derivado de productos como periódicos, revistas, y documentos impresos.\"}, \"fecha\": \"2024-10-12\", \"cantidad\": 79, \"depositoGlobalId\": 5, \"lastUpdate\": \"2024-10-12T12:00:00\" }]"))),
            @ApiResponse(responseCode = "401", description = "Debe iniciar sesión", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"error\": \"No está autenticado. Por favor, inicie sesión.\"}"))),
            @ApiResponse(responseCode = "403", description = "Acceso denegado - El usuario no tiene los permisos necesarios",
                    content = @Content(mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"error\": \"Acceso denegado. No tienes permisos para realizar esta acción.\"}")
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
    })
    @Parameters({
            @Parameter(name = "materialNombre", description = "Nombre del material a filtrar", required = false, examples = {
                    @ExampleObject(name = "Caso de material existente", value = "Papel"),
                    @ExampleObject(name = "Caso de material no existente", value = "VidrioInexistente")
            }),
            @Parameter(name = "abastecido", description = "Filtrar pedidos abastecidos (true) o no abastecidos (false)", required = false, examples = {
                    @ExampleObject(name = "Caso de pedidos abastecidos", value = "true"),
                    @ExampleObject(name = "Caso de pedidos no abastecidos", value = "false")
            }),
            @Parameter(name = "fechaPedido", description = "Fecha específica del pedido a filtrar en formato ISO (yyyy-MM-dd)", required = false, examples = {
                    @ExampleObject(name = "Caso de fecha existente", value = "2024-10-25"),
                    @ExampleObject(name = "Caso de fecha no existente", value = "2024-01-01")
            }),
            @Parameter(name = "lastUpdate", description = "Fecha de última actualización del pedido en formato ISO (yyyy-MM-dd)", required = false, examples = {
                    @ExampleObject(name = "Caso de última actualización existente", value = "2024-10-12"),
                    @ExampleObject(name = "Caso de última actualización no existente", value = "2024-01-01")
            }),
            @Parameter(name = "cantidad", description = "Cantidad del pedido para filtrar", required = false, examples = {
                    @ExampleObject(name = "Caso de cantidad válida", value = "100"),
                    @ExampleObject(name = "Caso de cantidad inválida", value = "-10") // Cantidad no válida
            }),
            @Parameter(name = "page", description = "Número de página (inicia en 1)", required = false, examples = {
                    @ExampleObject(name = "Caso de página existente", value = "1"),
                    @ExampleObject(name = "Caso de página no existente", value = "999") // Supone que no hay tantas páginas
            }),
            @Parameter(name = "pageSize", description = "Cantidad de elementos por página", required = false, examples = {
                    @ExampleObject(name = "Caso de tamaño válido", value = "10"),
                    @ExampleObject(name = "Caso de tamaño inválido", value = "0") // Tamaño no válido, ya que debe ser mayor que cero
            })
    })
    @Operation(
            summary = "Obtener mis pedidos",
            description = "Este endpoint permite obtener los pedidos del usuario autenticado.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/mis-pedidos")
    @PreAuthorize("hasAuthority('CONSULTAR_PEDIDO_PROPIO')")
    public ResponseEntity<?> getMisPedidos(@RequestParam(defaultValue = "", required = false) String materialNombre,
                                           @RequestParam(required = false) Boolean abastecido,
                                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaPedido,
                                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate lastUpdate,
                                           @RequestParam(required = false) Integer cantidad,
                                           @RequestParam(defaultValue = "1", required = false) int page,
                                           @RequestParam(defaultValue = "" + Integer.MAX_VALUE, required = false) int pageSize

    ) throws CentroInvalidoException {

        Page<Pedido> pedidos = pedidoService.getMisPedidos(page - 1, pageSize, materialNombre, abastecido, fechaPedido, lastUpdate, cantidad);
        return ResponseEntity.ok(pedidos
                .stream()
                .map(PedidoDTO::new)
                .toList());
    }

}
