package dssd.apiecocycle.validators;

import dssd.apiecocycle.requests.RegisterRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<ValidadorContra, Object> {

    @Override
    public void initialize(ValidadorContra constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        RegisterRequest request = (RegisterRequest) value;
        return request.getPassword() != null && request.getPassword().equals(request.getConfirmPassword());
    }
}
