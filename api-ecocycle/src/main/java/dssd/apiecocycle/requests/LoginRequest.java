package dssd.apiecocycle.requests;

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
    @NotBlank(message = "El mail del centro no puede estar vacío")
    String email;
    @NotBlank(message = "La contraseña no puede estar vacía")
    String password;
}
