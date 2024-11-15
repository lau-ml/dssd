package dssd.server.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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

    @OneToMany(mappedBy = "zona")
    private List<CentroRecoleccion> CentroRecoleccion;

    @OneToMany(mappedBy = "zona")
    private List<Ubicacion> ubicaciones;

}
