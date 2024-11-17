package dssd.server.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, length = 250)
    private String password;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String dni;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private Boolean activo = true;

    @Column(nullable = false, columnDefinition = "boolean default true")
    private Boolean habilitadoAdmin = true;

    @Column
    private String verificationCode;

    @Column
    private String contraCode;

    @ManyToOne
    private Rol rol;

    @ManyToOne
    @JoinColumn(name = "centro_recoleccion_id")
    private CentroRecoleccion centroRecoleccion;

    @ManyToMany
    @JoinTable(name = "usuario_punto_de_recoleccion", joinColumns = @JoinColumn(name = "usuario_id"), inverseJoinColumns = @JoinColumn(name = "punto_de_recoleccion_id"))
    private List<PuntoDeRecoleccion> puntosDeRecoleccion;

    public Usuario(String nombre, String apellido, String mail, String password, String username, String dni) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.email = mail;
        this.password = password;
        this.username = username;
        this.dni = dni;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<Permiso> permisos = rol.getPermisos();
        return permisos.stream().map(permiso -> new SimpleGrantedAuthority(permiso.getNombre())).toList();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return getHabilitadoAdmin();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return getActivo();
    }

    public void addPuntoDeRecoleccion(PuntoDeRecoleccion puntoDeRecoleccion) {
        if (puntosDeRecoleccion == null) {
            puntosDeRecoleccion = new ArrayList<>();
        }
        if (!puntosDeRecoleccion.contains(puntoDeRecoleccion)) {
            puntosDeRecoleccion.add(puntoDeRecoleccion);
            if (!puntoDeRecoleccion.getUsuarios().contains(this)) {
                puntoDeRecoleccion.getUsuarios().add(this);
            }
        }
    }

    public void removePuntoDeRecoleccion(PuntoDeRecoleccion puntoDeRecoleccion) {
        if (puntosDeRecoleccion != null) {
            puntosDeRecoleccion.remove(puntoDeRecoleccion);
        }
    }

}
