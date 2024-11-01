package dssd.apiecocycle.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrdenDistribucionRequest {
    @Positive(message = "La cantidad debe ser mayor a 0")
    @NotNull(message = "La cantidad no puede ser nula")
    private Long cantidad;
    @Positive(message = "El id del pedido debe ser mayor a 0")
    @NotNull(message = "El id del pedido no puede ser nulo")
    private Long pedidoId;
}
