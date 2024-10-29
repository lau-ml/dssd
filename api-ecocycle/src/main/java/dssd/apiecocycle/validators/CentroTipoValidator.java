package dssd.apiecocycle.validators;

import dssd.apiecocycle.model.CentroTipo;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class CentroTipoValidator implements ConstraintValidator<ValidCentroTipo, String> {

    @Override
    public void initialize(ValidCentroTipo constraintAnnotation) {
    }
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return false; // O true, dependiendo de si permites nulos o vacíos
        }
        try {
            CentroTipo.valueOf(value);
            return true;
        } catch (IllegalArgumentException e) {
            return false; // El valor no es un tipo válido
        }
    }
}
