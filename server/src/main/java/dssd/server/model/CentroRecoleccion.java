package dssd.server.model;

import java.util.List;
import java.util.ArrayList;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class CentroRecoleccion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String telefono;

    @OneToMany(mappedBy = "centroRecoleccion")
    private List<OrdenDeDistribucion> ordenesDeDistribucion = new ArrayList<>();

    @OneToMany(mappedBy = "centroRecoleccion")
    private List<CantidadMaterial> cantidadesMateriales = new ArrayList<>();

    @OneToMany(mappedBy = "centroRecoleccion")

    private List<Usuario> recolectores;

    public CentroRecoleccion() {

    }

    public CentroRecoleccion(String nombre, String email, String telefono) {
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
    }

}
