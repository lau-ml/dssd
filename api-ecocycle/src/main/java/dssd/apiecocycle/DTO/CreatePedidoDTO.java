package dssd.apiecocycle.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePedidoDTO {
    @NotNull(message = "El id del material no puede ser nulo")
    private Long materialId;
    @NotNull(message = "La cantidad no puede ser nula")
    private Long cantidad;

    public CreatePedidoDTO() {
    }
}
