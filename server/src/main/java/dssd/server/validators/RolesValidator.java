package dssd.server.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RolesValidator implements ConstraintValidator<ValidRol, String> {

    @Override
    public void initialize(ValidRol constraintAnnotation) {
    }
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return false; // O true, dependiendo de si permites nulos o vacíos
        }
        try {
            return value.equals("ROLE_EMPLEADO") || value.equals("ROLE_RECOLECTOR");
        } catch (IllegalArgumentException e) {
            return false; // El valor no es un tipo válido
        }
    }
}
