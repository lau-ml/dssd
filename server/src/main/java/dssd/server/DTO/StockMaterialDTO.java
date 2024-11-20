package dssd.server.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StockMaterialDTO {

    private Long id;
    private Integer cantidad;
    private Long centroRecoleccionId;
    private Long materialId;

}
