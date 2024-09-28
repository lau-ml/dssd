package dssd.server.model;

import java.util.List;
import java.util.ArrayList;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
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
    private List<Recolector> recolectores;

    public CentroRecoleccion() {

    }

    public CentroRecoleccion(String nombre, String email, String telefono) {
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
    }

    // Getters y setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public List<OrdenDeDistribucion> getOrdenesDeDistribucion() {
        return ordenesDeDistribucion;
    }

    public void setOrdenesDeDistribucion(List<OrdenDeDistribucion> ordenesDeDistribucion) {
        this.ordenesDeDistribucion = ordenesDeDistribucion;
    }

    public List<CantidadMaterial> getCantidadesMateriales() {
        return cantidadesMateriales;
    }

    public void setCantidadesMateriales(List<CantidadMaterial> cantidadesMateriales) {
        this.cantidadesMateriales = cantidadesMateriales;
    }

    public List<Recolector> getRecolectores() {
        return this.recolectores;
    }

    public void setRecolectores(List<Recolector> recolectores) {
        this.recolectores = recolectores;
    }
}
