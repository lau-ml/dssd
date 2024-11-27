package dssd.server.requests;

import dssd.server.model.OrdenDeDistribucion;
import lombok.Data;

@Data
public class OrdenRequest {
    private String deposito;
    private int cantidad;
    private Long materialId;
    private String centroRecoleccionEmail;
    private Long pedido;

}
