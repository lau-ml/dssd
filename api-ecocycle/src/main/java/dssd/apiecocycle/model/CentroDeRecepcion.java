package dssd.apiecocycle.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("CENTRO_RECEPCION")
public class CentroDeRecepcion extends Centro {

    @OneToMany(mappedBy = "centroDeRecepcion")
    private List<Orden> ordenes = new ArrayList<>();

    public CentroDeRecepcion() {
        super();
    }

    public CentroDeRecepcion(String email, String password, String telefono, String direccion) {
        super(email, password, telefono, direccion);
    }

    // Getters and Setters
    public List<Orden> getOrdenes() {
        return ordenes;
    }

    public void setOrdenes(List<Orden> ordenes) {
        this.ordenes = ordenes;
    }

}