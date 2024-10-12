package dssd.apiecocycle.controller;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dssd.apiecocycle.DTO.OrdenDTO;
import dssd.apiecocycle.DTO.OrdenDistribucionDTO;
import dssd.apiecocycle.DTO.PedidoDTO;
import dssd.apiecocycle.model.CentroDeRecepcion;
import dssd.apiecocycle.model.Material;
import dssd.apiecocycle.model.Orden;
import dssd.apiecocycle.model.Pedido;
import dssd.apiecocycle.service.CentroDeRecepcionService;
import dssd.apiecocycle.service.MaterialService;
import dssd.apiecocycle.service.OrdenSerive;
import dssd.apiecocycle.service.PedidoService;

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
    private OrdenSerive ordenSerive;

    @GetMapping("/{id}")
    public ResponseEntity<?> getPedidoById(@PathVariable Long id) {
        try {
            Optional<Pedido> pedido = pedidoService.getPedidoById(id);
            if (pedido.isPresent()) {
                PedidoDTO pedidoDTO = new PedidoDTO(pedido.get());
                return ResponseEntity.ok(pedidoDTO);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/material/nombre/{nameMaterial}")
    public ResponseEntity<?> obtenerPedidosPorMaterialNombre(@PathVariable String nameMaterial) {
        try {
            Material material = materialService.getMaterialByName(nameMaterial);
            if (material == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Material no encontrado");
            }

            List<Pedido> pedidos = pedidoService.getOrdersByMaterial(material);

            List<PedidoDTO> pedidosDTO = pedidos.stream()
                    .map(PedidoDTO::new)
                    .collect(Collectors.toList());

            return ResponseEntity.ok(pedidosDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @PostMapping("/generate-order")
    public ResponseEntity<?> generateOrder(@RequestBody OrdenDistribucionDTO ordenDistribucionDTO) {
        try {
            Optional<CentroDeRecepcion> centroDeRecepcion = centroDeRecepcionService
                    .getCentroById(ordenDistribucionDTO.getCentroDeRecepcionId());
            if (!centroDeRecepcion.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Centro de recepción no encontrado");
            }

            pedidoService.generarOrden(
                    ordenDistribucionDTO.getPedidoId(),
                    ordenDistribucionDTO.getMaterialId(),
                    ordenDistribucionDTO.getCantidad(),
                    centroDeRecepcion.get());

            return ResponseEntity.status(HttpStatus.CREATED).body("Orden generada con éxito");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{id}/ordenes")
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
}
