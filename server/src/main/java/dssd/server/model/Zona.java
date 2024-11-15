package dssd.server.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Entity
@Setter
public class Zona {
    @Id
    private Long id;

    private String nombre;

    @OneToOne(mappedBy = "zona")
    private Usuario usuario;

    @OneToMany(mappedBy = "zona")
    private List<Ubicacion> ubicaciones;

    public Zona(String nombre) {
        this.nombre = nombre;
    }

}
