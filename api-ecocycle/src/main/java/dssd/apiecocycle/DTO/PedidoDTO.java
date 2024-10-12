package dssd.apiecocycle.DTO;

import java.time.LocalDate;

import dssd.apiecocycle.model.Pedido;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PedidoDTO {
    private Long id;
    private MaterialDTO material;
    private LocalDate fecha;
    private int cantidad;
    private Long depositoGlobalId;

    public PedidoDTO() {
    }

    public PedidoDTO(Pedido pedido) {
        this.id = pedido.getId();
        this.material = new MaterialDTO(pedido.getMaterial());
        this.fecha = pedido.getFecha();
        this.cantidad = pedido.getCantidad() - pedido.getCantidadAbastecida();
        this.depositoGlobalId = pedido.getDepositoGlobal().getId();
    }
}
