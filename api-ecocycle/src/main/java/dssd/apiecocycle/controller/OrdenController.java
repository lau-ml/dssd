package dssd.apiecocycle.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

@RestController
@RequestMapping("/api/ordenes")
public class OrdenController {

    @Autowired
    private OrdenService ordenService;

    @Autowired
    private PedidoService pedidoService;

    @GetMapping("/{id}")
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
    @PutMapping("/{id}/entregado")
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
    @PutMapping("/{id}/rechazar")
    public ResponseEntity<?> rechazarOrden(@PathVariable Long id) {
        try {
            Orden orden = ordenService.getOrdenById(id);
            if (orden != null) {
                orden.setEstado(EstadoOrden.RECHAZADO);
                ordenService.updateOrden(orden);
                return ResponseEntity.ok(new OrdenDTO(orden));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Orden no encontrada");
            }
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
