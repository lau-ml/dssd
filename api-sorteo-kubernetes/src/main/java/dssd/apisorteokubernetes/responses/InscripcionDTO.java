package dssd.apisorteokubernetes.responses;

import lombok.*;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class InscripcionDTO {

    Long numeroInscripcionSorteo;

    Long numeroSorteo;

    LocalDate fechaInscripcion;

    LocalDate fechaSorteo;

}
