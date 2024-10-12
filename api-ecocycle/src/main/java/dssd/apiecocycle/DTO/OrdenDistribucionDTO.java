package dssd.apiecocycle.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrdenDistribucionDTO {
    private Long materialId;
    private int cantidad;
    private Long centroDeRecepcionId;
    private Long pedidoId;
}
