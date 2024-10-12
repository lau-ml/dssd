package dssd.server.model;

import java.util.List;
import java.util.ArrayList;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
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
