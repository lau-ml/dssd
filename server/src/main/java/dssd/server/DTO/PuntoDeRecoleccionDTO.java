package dssd.server.DTO;

import dssd.server.model.PuntoDeRecoleccion;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PuntoDeRecoleccionDTO {
    // Getters y setters
    private Long id;
    private String nombreEstablecimiento;
    private String direccion;
    private String numeroContacto;

    public PuntoDeRecoleccionDTO() {

    }

    public PuntoDeRecoleccionDTO(PuntoDeRecoleccion puntoDeRecoleccion) {
        this.id = puntoDeRecoleccion.getId();
        this.nombreEstablecimiento = puntoDeRecoleccion.getNombreEstablecimiento();
        this.direccion = puntoDeRecoleccion.getDireccion();
        this.numeroContacto = puntoDeRecoleccion.getNumeroContacto();
    }

}
