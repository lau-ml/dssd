package dssd.apisorteokubernetes.requests;

import jakarta.validation.constraints.Positive;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CentroDTO {
    @Positive(message = "El id del centro debe ser un número positivo")
    Long idCentro;
}
