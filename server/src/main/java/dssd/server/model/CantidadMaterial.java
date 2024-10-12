package dssd.server.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class CantidadMaterial {
    // Getters y setters
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer cantidad;

    @ManyToOne
    @JoinColumn(name = "centro_recoleccion_id")
    private CentroRecoleccion centroRecoleccion;

    @ManyToOne
    @JoinColumn(name = "material_id")
    private Material material;

    public CantidadMaterial() {

    }

}
