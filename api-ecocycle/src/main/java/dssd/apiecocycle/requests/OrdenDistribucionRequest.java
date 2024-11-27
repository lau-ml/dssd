package dssd.apiecocycle.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrdenDistribucionRequest {
    @Positive(message = "La cantidad debe ser mayor a 0")

    private Long cantidad;
    @Positive(message = "El id del pedido debe ser mayor a 0")

    private Long pedidoId;
}
