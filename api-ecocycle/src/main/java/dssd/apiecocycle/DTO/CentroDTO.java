package dssd.apiecocycle.DTO;

import dssd.apiecocycle.model.Centro;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CentroDTO {
    private Long id;
    private String email;
    private String telefono;
    private String direccion;

    public CentroDTO() {
    }

    public CentroDTO(Centro centroDeRecepcion) {
        this.id = centroDeRecepcion.getId();
        this.email = centroDeRecepcion.getEmail();
        this.telefono = centroDeRecepcion.getTelefono();
        this.direccion = centroDeRecepcion.getDireccion();
    }

}
