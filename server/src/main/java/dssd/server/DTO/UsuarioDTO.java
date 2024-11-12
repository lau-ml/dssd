package dssd.server.DTO;

import dssd.server.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@AllArgsConstructor
@Getter
public class UsuarioDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String username;
    private String dni;
    private boolean tieneRegistroCompletoPendiente = false;

    public UsuarioDTO() {

    }

    public UsuarioDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nombre = usuario.getNombre();
        this.apellido = usuario.getApellido();
        this.username = usuario.getUsername();
        this.dni = usuario.getDni();
    }

    public UsuarioDTO(Usuario usuario, boolean tieneRegistroCompletoPendiente) {
        this.id = usuario.getId();
        this.nombre = usuario.getNombre();
        this.apellido = usuario.getApellido();
        this.username = usuario.getUsername();
        this.dni = usuario.getDni();
        this.tieneRegistroCompletoPendiente = tieneRegistroCompletoPendiente;
    }

}
