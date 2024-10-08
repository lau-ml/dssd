package dssd.server.model;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

@Setter
@Getter
@Entity
@DiscriminatorValue("recolector")
public class Recolector extends Usuario {

    @OneToMany(mappedBy = "recolector")
    private List<Notificacion> notificaciones = new ArrayList<>();

    @OneToMany(mappedBy = "recolector")
    private List<Estadistica> estadisticas;

    @OneToMany(mappedBy = "recolector")
    private List<RegistroRecoleccion> registroRecolecciones;

    public Recolector() {
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_recolector"));
    }


    public Recolector(String nombre, String apellido, String mail, String password, String username) {
        super(nombre, apellido, mail, password, username);
    }
}
