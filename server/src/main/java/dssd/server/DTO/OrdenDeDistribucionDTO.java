package dssd.server.DTO;

import java.time.LocalDateTime;

import dssd.server.model.OrdenDeDistribucion;
import lombok.Getter;

@Getter
public class OrdenDeDistribucionDTO {

    private Long id;
    private String deposito;
    private int cantidad;
    private MaterialDTO material;
    private OrdenDeDistribucion.EstadoOrden estado;
    private LocalDateTime fechaCreacion;

    public OrdenDeDistribucionDTO() {
    }

    public OrdenDeDistribucionDTO(OrdenDeDistribucion ordenDeDistribucion) {
        this.id = ordenDeDistribucion.getId();
        this.deposito = ordenDeDistribucion.getDeposito();
        this.cantidad = ordenDeDistribucion.getCantidad();
        this.material = new MaterialDTO(ordenDeDistribucion.getMaterial());
        this.estado = ordenDeDistribucion.getEstado();
        this.fechaCreacion = ordenDeDistribucion.getFechaCreacion();
    }

}
