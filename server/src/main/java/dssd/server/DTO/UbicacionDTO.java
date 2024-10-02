package dssd.server.DTO;

import dssd.server.model.Ubicacion;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UbicacionDTO {
    // Getters y setters
    private Long id;
    private String nombreEstablecimiento;

    public UbicacionDTO() {

    }

    public UbicacionDTO(Ubicacion ubicacion) {
        this.id = ubicacion.getId();
        this.nombreEstablecimiento = ubicacion.getNombreEstablecimiento();
    }

}
