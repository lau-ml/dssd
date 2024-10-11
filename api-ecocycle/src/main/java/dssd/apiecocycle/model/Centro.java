package dssd.apiecocycle.model;

import java.util.Set;
import java.util.HashSet;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_centro", discriminatorType = DiscriminatorType.STRING)
public abstract class Centro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false, length = 250)
    private String password;

    @Column(nullable = false)
    private String telefono;

    @Column(nullable = false)
    private String direccion;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "centro_permiso", joinColumns = @JoinColumn(name = "centro_id"), inverseJoinColumns = @JoinColumn(name = "permiso_id"))
    private Set<Permiso> permisos = new HashSet<>();

    public Centro() {
    }

    public Centro(String email, String password, String telefono, String direccion) {
        this.email = email;
        this.password = password;
        this.telefono = telefono;
        this.direccion = direccion;
    }

    public Centro(String email, String password, String telefono, String direccion, Set<Permiso> permisos) {
        this.email = email;
        this.password = password;
        this.telefono = telefono;
        this.direccion = direccion;
        this.permisos = permisos;
    }

    public Centro(String email, String password, String telefono, String direccion, Permiso permiso) {
        this.email = email;
        this.password = password;
        this.telefono = telefono;
        this.direccion = direccion;
        this.permisos.add(permiso);
    }

    public void agregarPermiso(Permiso permiso) {
        this.permisos.add(permiso);
    }

    public void eliminarPermiso(Permiso permiso) {
        this.permisos.remove(permiso);
    }
}
