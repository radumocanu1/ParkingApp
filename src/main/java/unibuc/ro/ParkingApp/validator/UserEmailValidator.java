package unibuc.ro.ParkingApp.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UserEmailValidatorImplementation.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)

public @interface UserEmailValidator {
    String message() default "This is not a valid email address!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

}
