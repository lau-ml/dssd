package dssd.server.requests;

import dssd.server.validators.ValidadorContra;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ValidadorContra
public class CambiarPassRequest {


    @NotBlank(message = "La contraseña no puede estar vacía")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Debe tener 8 caracteres, y al menos una letra minúscula, una letra mayúscula, un número y un caracter especial")
    String password;
    @NotBlank(message = "La contraseña no puede estar vacía")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Debe tener 8 caracteres, y al menos una letra minúscula, una letra mayúscula, un número y un caracter especial")
    String confirmPassword;
    String code;
}
