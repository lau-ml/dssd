package dssd.server.validators;

import dssd.server.requests.CambiarPassRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<ValidadorContra, Object> {

    @Override
    public void initialize(ValidadorContra constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        CambiarPassRequest request = (CambiarPassRequest) value;
        return request.getPassword() != null && request.getPassword().equals(request.getConfirmPassword());
    }
}
