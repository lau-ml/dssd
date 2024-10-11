package dssd.apiecocycle.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    public CentroDeRecepcion(String email, String password, String telefono, String direccion, Permiso permiso) {
        super(email, password, telefono, direccion, permiso);
    }

    public CentroDeRecepcion(String email, String password, String telefono, String direccion, Set<Permiso> permisos) {
        super(email, password, telefono, direccion, permisos);
    }

    // Getters and Setters
    public List<Orden> getOrdenes() {
        return ordenes;
    }

    public void setOrdenes(List<Orden> ordenes) {
        this.ordenes = ordenes;
    }

}