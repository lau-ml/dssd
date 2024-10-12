package dssd.apiecocycle.DTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePedidoDTO {
    private Long materialId;
    private int cantidad;
    private Long depositoGlobalId;

    public CreatePedidoDTO() {
    }
}
