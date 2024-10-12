package dssd.apiecocycle.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Entity
@DiscriminatorValue("CENTRO_RECEPCION")
public class CentroDeRecepcion extends Centro {

    // Getters and Setters
    @OneToMany(mappedBy = "centroDeRecepcion")
    private List<Orden> ordenes = new ArrayList<>();

    public CentroDeRecepcion() {
        super();
    }

    public CentroDeRecepcion(String email, String password, String telefono, String direccion) {
        super(email, password, telefono, direccion);
    }

}
