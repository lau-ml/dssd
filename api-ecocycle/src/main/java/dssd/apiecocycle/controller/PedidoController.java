package dssd.apiecocycle.controller;

import dssd.apiecocycle.DTO.CreatePedidoDTO;
import dssd.apiecocycle.DTO.OrdenDTO;
import dssd.apiecocycle.DTO.PedidoDTO;
import dssd.apiecocycle.exceptions.CantidadException;
import dssd.apiecocycle.exceptions.CentroInvalidoException;
import dssd.apiecocycle.model.Pedido;
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

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/pedido")
public class PedidoController {
    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private OrdenService ordenService;

    // ROL AMBOS
    @PreAuthorize("hasAuthority('CONSULTAR_PEDIDO')")
    @GetMapping("/{id}")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"), summary = "Obtener pedido por ID", description = "Este endpoint devuelve un pedido específico utilizando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoDTO.class), examples = @ExampleObject(value = "{\"id\": 1, \"material\": {\"id\": 1, \"nombre\": \"Papel\", \"descripcion\": \"Material reciclable derivado de productos como periódicos, revistas, y documentos impresos.\"}, \"fecha\": \"2024-10-12\", \"cantidad\": 100, \"depositoGlobalId\": 4}"))),
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

    @PreAuthorize("hasAuthority('CONSULTAR_PEDIDO')")
    @GetMapping("/material/nombre/{nameMaterial}")
    @Operation(summary = "Obtener pedidos por nombre de material", security = @SecurityRequirement(name = "bearerAuth"), description = "Este endpoint devuelve una lista de pedidos asociados a un material específico. Requiere loguearse como Centro de Recolección.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedidos encontrados", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoDTO.class), examples = @ExampleObject(value = "[{\"id\": 1, \"material\": {\"id\": 1, \"nombre\": \"Papel\", \"descripcion\": \"Material reciclable...\"}, \"fecha\": \"2024-10-12\", \"cantidad\": 100, \"depositoGlobalId\": 4}, {\"id\": 4, \"material\": {\"id\": 1, \"nombre\": \"Papel\", \"descripcion\": \"Material reciclable...\"}, \"fecha\": \"2024-10-12\", \"cantidad\": 79, \"depositoGlobalId\": 5}]"))),
            @ApiResponse(responseCode = "401", description = "Debe iniciar sesión", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "{\"message\": \"No está autenticado. Por favor, inicie sesión.\"}"))),
            @ApiResponse(responseCode = "403", description = "No tiene permisos para acceder a este recurso", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "{\"message\": \"No tiene permisos para acceder a este recurso.\"}"))),
            @ApiResponse(responseCode = "404", description = "Material no encontrado", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "{\"message\": \"Material no encontrado\"}"))),
            @ApiResponse(
                    responseCode = "500",
                    description = "Error interno del servidor",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "{\"message\": \"Error interno del servidor.\"}")
                    )
            )
    })
    public ResponseEntity<?> obtenerPedidosPorMaterialNombre(@PathVariable String nameMaterial) {
        try {
            List<Pedido> pedidos = pedidoService.getPedidosByMaterialName(nameMaterial);
            return ResponseEntity.ok(pedidos
                    .stream()
                    .map(PedidoDTO::new)
                    .toList());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MessageResponse.builder().message("Material no encontrado").build());
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
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MessageResponse.builder().message(e.getMessage()).build());
        }
        catch(CantidadException e) {
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
            )    })
    public ResponseEntity<?> getOrdenesPorPedidoId(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(ordenService
                    .getAllOrdersByPedidoId(id)
                    .stream()
                    .map(OrdenDTO::new)
                    .toList());
        }
        catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(MessageResponse.builder().message("Pedido no encontrado").build());
        }
    }

}
