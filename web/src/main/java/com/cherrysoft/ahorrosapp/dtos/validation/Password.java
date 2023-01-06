package com.cherrysoft.ahorrosapp.dtos.validation;


import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import javax.validation.constraints.Size;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD, ElementType.TYPE_PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
@Size(
    min = 6,
    max = 16,
    message = "Password must be between {min} and {max} chars."
)
public @interface Password {
  String message() default "A password is required.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}

class PasswordValidator implements ConstraintValidator<Password, String> {

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return true;
  }

}
