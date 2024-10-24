package dssd.apiecocycle.DTO;

import dssd.apiecocycle.model.EstadoOrden;
import dssd.apiecocycle.model.Orden;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class OrdenDTO {
    private Long id;
    private MaterialDTO material;
    private int cantidad;
    private CentroDTO centroDeRecepcion;
    private Long pedidoId;
    private EstadoOrden estadoOrden;
    private LocalDate fecha;
    private Long globalId;
    private int cantidadAceptada;
    private LocalDate lastUpdate;
    public OrdenDTO(Orden orden) {
        this.id = orden.getId();
        this.material = new MaterialDTO(orden.getMaterial());
        this.cantidad = orden.getCantidad();
        this.centroDeRecepcion = new CentroDTO(orden.getCentroDeRecepcion());
        this.pedidoId = orden.getPedido().getId();
        this.estadoOrden = orden.getEstado();
        this.fecha = orden.getFecha();
        this.globalId = orden.getPedido().getDepositoGlobal().getId();
        this.cantidadAceptada = orden.getCantidadAceptada();
        this.lastUpdate = orden.getLastUpdate();
    }

}
