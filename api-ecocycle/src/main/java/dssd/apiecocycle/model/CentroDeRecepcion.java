package dssd.apiecocycle.model;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@DiscriminatorValue("CENTRO_RECEPCION")
public class CentroDeRecepcion extends Centro {

    @OneToMany(mappedBy = "centroDeRecepcion")
    private Set<Orden> ordenes;

    // Getters and Setters
}