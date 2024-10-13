package dssd.apiecocycle.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dssd.apiecocycle.DTO.CreatePedidoDTO;
import dssd.apiecocycle.DTO.OrdenDTO;
import dssd.apiecocycle.DTO.OrdenDistribucionDTO;
import dssd.apiecocycle.DTO.PedidoDTO;
import dssd.apiecocycle.model.CentroDeRecepcion;
import dssd.apiecocycle.model.DepositoGlobal;
import dssd.apiecocycle.model.Material;
import dssd.apiecocycle.model.Orden;
import dssd.apiecocycle.model.Pedido;
import dssd.apiecocycle.service.CentroDeRecepcionService;
import dssd.apiecocycle.service.DepositoGlobalService;
import dssd.apiecocycle.service.MaterialService;
import dssd.apiecocycle.service.OrdenService;
import dssd.apiecocycle.service.PedidoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/api/pedido")
public class PedidoController {
    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private MaterialService materialService;

    @Autowired
    private CentroDeRecepcionService centroDeRecepcionService;

    @Autowired
    private OrdenService ordenSerive;

    @Autowired
    private DepositoGlobalService depositoGlobalService;

    // ROL AMBOS
    @PreAuthorize("hasAuthority('CONSULTAR_PEDIDO')")
    @GetMapping("/{id}")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"),summary = "Obtener pedido por ID", description = "Este endpoint devuelve un pedido específico utilizando su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedido encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoDTO.class), examples = @ExampleObject(value = "{\"id\": 1, \"material\": {\"id\": 1, \"nombre\": \"Papel\", \"descripcion\": \"Material reciclable derivado de productos como periódicos, revistas, y documentos impresos.\"}, \"fecha\": \"2024-10-12\", \"cantidad\": 100, \"depositoGlobalId\": 4}"))),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Pedido no encontrado"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Error: [mensaje del error]")))
    })
    public ResponseEntity<?> getPedidoById(@PathVariable Long id) {
        try {
            Optional<Pedido> pedido = pedidoService.getPedidoById(id);
            if (pedido.isPresent()) {
                PedidoDTO pedidoDTO = new PedidoDTO(pedido.get());
                return ResponseEntity.ok(pedidoDTO);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido no encontrado");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PreAuthorize("hasAuthority('CONSULTAR_PEDIDO')")
    @GetMapping("/material/nombre/{nameMaterial}")
    @Operation(summary = "Obtener pedidos por nombre de material", security = @SecurityRequirement(name = "bearerAuth"),description = "Este endpoint devuelve una lista de pedidos asociados a un material específico. Requiere loguearse como Centro de Recolección.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pedidos encontrados", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoDTO.class), examples = @ExampleObject(value = "[{\"id\": 1, \"material\": {\"id\": 1, \"nombre\": \"Papel\", \"descripcion\": \"Material reciclable...\"}, \"fecha\": \"2024-10-12\", \"cantidad\": 100, \"depositoGlobalId\": 4}, {\"id\": 4, \"material\": {\"id\": 1, \"nombre\": \"Papel\", \"descripcion\": \"Material reciclable...\"}, \"fecha\": \"2024-10-12\", \"cantidad\": 79, \"depositoGlobalId\": 5}]"))),
            @ApiResponse(responseCode = "401", description = "No autorizado.", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "No autorizado"))),
            @ApiResponse(responseCode = "404", description = "Material no encontrado", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Material no encontrado"))),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "text/plain", examples = @ExampleObject(value = "Error: [mensaje del error]")))
    })
    public ResponseEntity<?> obtenerPedidosPorMaterialNombre(@PathVariable String nameMaterial) {
        try {
            Material material = materialService.getMaterialByName(nameMaterial);
            if (material == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Material no encontrado");
            }

            List<Pedido> pedidos = pedidoService.getOrdersByMaterialAndAbastecido(material, false);

            List<PedidoDTO> pedidosDTO = pedidos.stream()
                    .map(PedidoDTO::new)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(pedidosDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // ROL CENTER
    @PreAuthorize("hasAuthority('GENERAR_ORDEN')")
    @PostMapping("/generate-order")
    @Operation(security = @SecurityRequirement(name = "bearerAuth") ,summary = "Generar una nueva orden de distribución", description = "Este endpoint permite generar una nueva orden de distribución para un pedido específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Orden creada exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrdenDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "404", description = "Centro de recepción o pedido no encontrado", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> generateOrder(@RequestBody OrdenDistribucionDTO ordenDistribucionDTO) {
        try {
            Optional<CentroDeRecepcion> centroDeRecepcion = centroDeRecepcionService
                    .getCentroById(ordenDistribucionDTO.getCentroDeRecepcionId());
            if (!centroDeRecepcion.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Centro de recepción no encontrado");
            }

            Orden orden = pedidoService.generarOrden(
                    ordenDistribucionDTO.getPedidoId(),
                    ordenDistribucionDTO.getMaterialId(),
                    ordenDistribucionDTO.getCantidad(),
                    centroDeRecepcion.get());

            return ResponseEntity.status(HttpStatus.CREATED).body(new OrdenDTO(orden));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // ROL DEPOSITO
    @PreAuthorize("hasAuthority('CONSULTAR_ORDENES_PEDIDO')")
    @GetMapping("/{id}/ordenes")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"), summary = "Obtener órdenes por ID de pedido", description = "Este endpoint permite obtener todas las órdenes asociadas a un pedido específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Órdenes encontradas", content = @Content(mediaType = "application/json", schema = @Schema(implementation = OrdenDTO[].class))),
            @ApiResponse(responseCode = "404", description = "Pedido no encontrado o no hay órdenes asociadas", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> getOrdenesPorPedidoId(@PathVariable Long id) {
        try {
            Optional<Pedido> pedido = pedidoService.getPedidoById(id);
            if (!pedido.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido no encontrado");
            }
            List<Orden> ordenes = ordenSerive.getOrdersByPedido(pedido);
            if (ordenes.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No se encontraron órdenes para el pedido especificado");
            }

            List<OrdenDTO> ordenesDTO = ordenes.stream()
                    .map(OrdenDTO::new)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(ordenesDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    // ROL DEPOSITO
    @PreAuthorize("hasAuthority('GENERAR_PEDIDO')")
    @PostMapping("/create")
    @Operation(security = @SecurityRequirement(name = "bearerAuth"), summary = "Crear un nuevo pedido", description = "Este endpoint permite crear un nuevo pedido para un material específico.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pedido creado exitosamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = PedidoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Error de solicitud: cantidad inválida", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "404", description = "Material o depósito global no encontrado", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "text/plain"))
    })
    public ResponseEntity<?> createPedido(@RequestBody CreatePedidoDTO createPedidoDTO) {
        try {
            Material material = materialService.getMaterialById(createPedidoDTO.getMaterialId());
            if (material == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Material no encontrado");
            }

            // ESTO DEBERIA CAMBIARSE LUEGO CON EL TOKEN
            DepositoGlobal depositoGlobal = createPedidoDTO.getDepositoGlobalId() != null
                    ? depositoGlobalService.getDepositoGlobalById(createPedidoDTO.getDepositoGlobalId())
                    : null;
            if (depositoGlobal == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Depósito global no encontrado");
            }

            if (createPedidoDTO.getCantidad() < 1) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("La cantidad debe ser mayor a 0");
            }

            Pedido newPedido = new Pedido(material, createPedidoDTO.getCantidad(), depositoGlobal);
            pedidoService.savePedido(newPedido);

            return ResponseEntity.status(HttpStatus.CREATED).body(new PedidoDTO(newPedido));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

}
