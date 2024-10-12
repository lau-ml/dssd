package dssd.server.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest {
    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    String username;
    @NotBlank(message = "La contraseña no puede estar vacía")
    String password;
}
