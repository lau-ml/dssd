package dssd.server.requests;

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
public class RegisterRequest {
/*
    username: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(100)]],
    password: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(20)]],
    email: ['', [Validators.required, Validators.email, Validators.maxLength(150)]],
    firstName: ['', [Validators.required, Validators.maxLength(50)]],
    lastName: ['', [Validators.required, Validators.maxLength(50)]],
    confirmPassword: ['', [Validators.required, Validators.minLength(8), Validators.maxLength(20)]],
  */


    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    @Size(min = 6, max = 100, message = "El nombre de usuario debe tener entre 6 y 100 caracteres")
    String username;
    @Size(min = 8, max = 20, message = "La contraseña debe tener entre 8 y 20 caracteres")
    @NotBlank(message = "La contraseña no puede estar vacía")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&#+_\\-\\/\\\\|:;\\.,])[A-Za-z\\d@$!%*?&#+_\\-\\/\\\\|:;\\.,]{8,}$",
            message = "Debe tener 8 caracteres, y al menos una letra minúscula, una letra mayúscula, un número y un caracter especial")
    String password;

    @Size(min = 8, max = 20, message = "La confirmación de contraseña debe tener entre 8 y 20 caracteres")
    @NotBlank(message = "La confirmación de contraseña no puede estar vacía")
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&#+_\\-\\/\\\\|:;\\.,])[A-Za-z\\d@$!%*?&#+_\\-\\/\\\\|:;\\.,]{8,}$",
            message = "Debe tener 8 caracteres, y al menos una letra minúscula, una letra mayúscula, un número y un caracter especial")
    String confirmPassword;



    @Size(min = 1, max = 50, message = "El nombre debe tener como máximo 50 caracteres")
    @NotBlank(message = "El nombre no puede estar vacío")
    String firstName;
    @NotBlank(message = "El apellido no puede estar vacío")
    @Size(min = 1, max = 50, message = "El apellido debe tener como máximo 50 caracteres")
    String lastName;
    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El email debe ser válido")
    @Size(min = 1, max = 150, message = "El email debe tener como máximo 150 caracteres")
    String email;
    @NotBlank(message = "El DNI no puede estar vacío")
    @Size(min = 1, max = 10, message = "El DNI debe tener entre 1 y 10 dígitos")
    @Pattern(regexp = "^[0-9]{1,10}$", message = "El DNI debe contener solamente dígitos")
    String dni;
}

