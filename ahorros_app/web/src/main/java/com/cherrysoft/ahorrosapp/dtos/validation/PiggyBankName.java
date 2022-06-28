package com.cherrysoft.ahorrosapp.dtos.validation;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Objects;

@Target({ElementType.FIELD, ElementType.TYPE_PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PiggyBankNameValidator.class)
public @interface PiggyBankName {
  String message() default "Username can only have letters, numbers, _ or -.";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}

class PiggyBankNameValidator implements ConstraintValidator<PiggyBankName, String> {
  public static final String VALID_PIGGYBANK_NAME_PATTERN = "^[a-zA-Z0-9_-]*$";

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    if (Objects.isNull(value)) {
      return false;
    }
    return value.matches(VALID_PIGGYBANK_NAME_PATTERN);
  }
}