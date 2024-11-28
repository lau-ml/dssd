package dssd.server.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Builder
@Getter
public class ResponseEstadistica {

    private Boolean esPrimeraVezDias;
}
