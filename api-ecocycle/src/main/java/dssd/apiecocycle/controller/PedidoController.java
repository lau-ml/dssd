package dssd.apiecocycle.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dssd.apiecocycle.DTO.PedidoDTO;
import dssd.apiecocycle.model.Material;
import dssd.apiecocycle.model.Pedido;
import dssd.apiecocycle.service.MaterialService;
import dssd.apiecocycle.service.PedidoService;

@RestController
@RequestMapping("/api/pedido")
public class PedidoController {
    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private MaterialService materialService;

    @GetMapping("/{id}")
    public ResponseEntity<?> getPedidoById(@PathVariable Long id) {
        try {
            Pedido pedido = pedidoService.getPedidoById(id);
            if (pedido != null) {
                PedidoDTO pedidoDTO = new PedidoDTO(pedido);
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
}
