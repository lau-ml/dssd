package dssd.apiecocycle.DTO;

import dssd.apiecocycle.model.Orden;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrdenDTO {
    private MaterialDTO materialDTO;
    private int cantidad;
    private CentroDTO centroDeRecepcion;
    private Long pedidoId;

    public OrdenDTO(Orden orden) {
        this.materialDTO = new MaterialDTO(orden.getMaterial());
        this.cantidad = orden.getCantidad();
        this.centroDeRecepcion = new CentroDTO(orden.getCentroDeRecepcion());
        this.pedidoId = orden.getPedido().getId();
    }

}
