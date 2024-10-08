package dssd.server.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

@Entity
public class Empleado extends Usuario{
    @Column(length = 250)
    private String sector;
    public Empleado() {
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_empleado"));
    }
    public Empleado(String nombre, String apellido, String mail, String password, String sector) {
        Usuario.builder().nombre(nombre).apellido(apellido).email(mail).password(password).build();
        this.sector = sector;
    }
}
