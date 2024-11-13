package dssd.apisorteokubernetes.responses;

import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class InscripcionDTO {

    Long numeroInscripcion;

    Long numeroSorteo;

    LocalDate fechaInscripcion;

    LocalDate fechaSorteo;

}
