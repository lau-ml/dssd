package dssd.apiecocycle.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.mapping.Set;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Entity
@Getter
@Setter
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(nullable = false)
    private String descripcion;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "material_proveedor",
            joinColumns = @JoinColumn(name = "material_id"),
            inverseJoinColumns = @JoinColumn(name = "proveedor_id"))
    private HashSet<CentroDeRecepcion> proveedores = new HashSet<>();

    public Material() {
    }

    public Material(String nombre, String descripcion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
    }

    public void addProveedor(CentroDeRecepcion proveedor) {
        this.proveedores.add(proveedor);
    }

    public boolean contieneProveedor(CentroDeRecepcion proveedor) {
        return this.proveedores.contains(proveedor);
    }
}
