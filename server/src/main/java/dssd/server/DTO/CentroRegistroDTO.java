package dssd.server.DTO;

import dssd.server.model.CentroRecoleccion;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Data
@Setter
@Getter
public class CentroRegistroDTO {
    private String nombre;
    private Long id;

    public CentroRegistroDTO(CentroRecoleccion centroRecoleccion) {
        this.nombre = centroRecoleccion.getNombre();
        this.id = centroRecoleccion.getId();
    }
}
