package dssd.apiecocycle.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreatePedidoRequest {
    @NotNull(message = "El id del material no puede ser nulo")
    @Positive(message = "El id del material debe ser mayor a 0")
    private Long materialId;
    @NotNull(message = "La cantidad no puede ser nula")
    @Positive(message = "La cantidad debe ser mayor a 0")
    private Long cantidad;

    public CreatePedidoRequest() {
    }
}
