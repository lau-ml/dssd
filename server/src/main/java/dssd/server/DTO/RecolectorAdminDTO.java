package dssd.server.DTO;

import dssd.server.model.Usuario;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@AllArgsConstructor
@Getter
public class RecolectorAdminDTO {
    private Long id;
    private String nombre;
    private String apellido;
    private String username;
    private String dni;
    private String email;
    private boolean activo;

    public RecolectorAdminDTO() {

    }

    public RecolectorAdminDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nombre = usuario.getNombre();
        this.apellido = usuario.getApellido();
        this.username = usuario.getUsername();
        this.dni = usuario.getDni();
        this.email = usuario.getEmail();
        this.activo = usuario.getActivo();
    }

}
