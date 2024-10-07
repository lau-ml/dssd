package dssd.server.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ttps.java.entregable6_v2.helpers.validators.ValidadorContra;

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
