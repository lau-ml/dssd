package dssd.apiecocycle.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@Entity
@NoArgsConstructor
@DiscriminatorValue("CENTRO_RECEPCION")
public class CentroDeRecepcion extends Centro {

    // Getters and Setters
    @OneToMany(mappedBy = "centroDeRecepcion")
    private List<Orden> ordenes = new ArrayList<>();

    @ManyToMany
    private Set<Material> materiales = new HashSet<>();

    public CentroDeRecepcion(String nombre, String email, String password, String telefono, String direccion, Rol rol) {
        super(nombre, email, password, telefono, direccion, rol);
    }

    public void addMaterial(Material material) {
        this.materiales.add(material);
    }


}
