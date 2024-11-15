package dssd.server.DTO;

import dssd.server.model.Zona;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@Data
public class ZonaRegistroDTO {
    private Long id;
    private String nombre;


    public ZonaRegistroDTO(Zona zona) {
        this.id = zona.getId();
        this.nombre = zona.getNombre();
    }

}
