package dssd.apiecocycle.DTO;

import dssd.apiecocycle.model.EstadoOrden;
import dssd.apiecocycle.model.Orden;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrdenDTO {
    private Long id;
    private MaterialDTO materialDTO;
    private int cantidad;
    private CentroDTO centroDeRecepcion;
    private Long pedidoId;
    private EstadoOrden estadoOrden;

    public OrdenDTO(Orden orden) {
        this.id = orden.getId();
        this.materialDTO = new MaterialDTO(orden.getMaterial());
        this.cantidad = orden.getCantidad();
        this.centroDeRecepcion = new CentroDTO(orden.getCentroDeRecepcion());
        this.pedidoId = orden.getPedido().getId();
        this.estadoOrden = orden.getEstado();
    }

}
