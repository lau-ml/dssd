package dssd.apiecocycle.requests;

import dssd.apiecocycle.model.CentroTipo;
import dssd.apiecocycle.validators.ValidadorContra;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ValidadorContra
@Schema(description = "Datos necesarios para registrar un centro de recolección")
public class RegisterRequest {
    /*
     * username: ['', [Validators.required, Validators.minLength(6),
     * Validators.maxLength(100)]],
     * password: ['', [Validators.required, Validators.minLength(8),
     * Validators.maxLength(20)]],
     * email: ['', [Validators.required, Validators.email,
     * Validators.maxLength(150)]],
     * firstName: ['', [Validators.required, Validators.maxLength(50)]],
     * lastName: ['', [Validators.required, Validators.maxLength(50)]],
     * confirmPassword: ['', [Validators.required, Validators.minLength(8),
     * Validators.maxLength(20)]],
     */

    @Size(min = 8, max = 20, message = "La contraseña debe tener entre 8 y 20 caracteres")
    @NotBlank(message = "La contraseña no puede estar vacía")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&#+_\\-\\/\\\\|:;\\.,])[A-Za-z\\d@$!%*?&#+_\\-\\/\\\\|:;\\.,]{8,}$", message = "Debe tener 8 caracteres, y al menos una letra minúscula, una letra mayúscula, un número y un caracter especial")
    @Schema(description = "Contraseña del usuario, debe tener entre 8 y 20 caracteres, incluir una letra mayúscula, una minúscula, un número y un caracter especial")
    String password;

    @Size(min = 8, max = 20, message = "La confirmación de contraseña debe tener entre 8 y 20 caracteres")
    @NotBlank(message = "La confirmación de contraseña no puede estar vacía")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&#+_\\-\\/\\\\|:;\\.,])[A-Za-z\\d@$!%*?&#+_\\-\\/\\\\|:;\\.,]{8,}$", message = "Debe tener 8 caracteres, y al menos una letra minúscula, una letra mayúscula, un número y un caracter especial")
    @Schema(description = "Confirmación de la contraseña, debe coincidir con la contraseña y seguir las mismas reglas de formato")
    String confirmPassword;

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email debe ser válido")
    @Size(min = 1, max = 150, message = "El email debe tener como máximo 150 caracteres")
    @Schema(description = "Email del centro de recolección, debe ser un correo electrónico válido y tener como máximo 150 caracteres")
    String email;

    @NotBlank(message = "La dirección no puede estar vacía")
    @Size(min = 1, max = 150, message = "La dirección debe tener como máximo 150 caracteres")
    @Schema(description = "Dirección del centro de recolección, máximo 150 caracteres")
    String direccion;

    @NotBlank(message = "El teléfono no puede estar vacío")
    @Size(min = 1, max = 50, message = "El teléfono debe tener como máximo 50 caracteres")
    @Pattern(regexp = "^(\\+\\d{1,3}[- ]?)?\\d{10}$",
            message = "El teléfono debe ser un número válido de 10 dígitos, con un código de país opcional")
    @Schema(description = "Teléfono del centro de recolección, máximo 50 caracteres")
    String telefono;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(min = 1, max = 50, message = "El nombre debe tener como máximo 50 caracteres")
    @Schema(description = "Nombre del centro de recolección, máximo 50 caracteres")
    String nombre;

    @Schema(description = "Tipo del centro de recolección")
    CentroTipo tipo;

}
