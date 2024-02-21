package ru.khvatov.learningprojects.testtasks.symbolfrequencycalculatorapi.api.error.util;

import jakarta.validation.ConstraintViolation;
import lombok.experimental.UtilityClass;
import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import ru.khvatov.learningprojects.testtasks.symbolfrequencycalculatorapi.api.error.suberrors.ValidationApiSubError;

@UtilityClass
public class ErrorMappers {

    public static ValidationApiSubError toValidationApiSubError(ObjectError objectError) {
        String object = objectError.getObjectName();
        String message = objectError.getDefaultMessage();
        return new ValidationApiSubError(object, message);
    }

    public static ValidationApiSubError toValidationApiSubError(FieldError fieldError) {
        String object = fieldError.getObjectName();
        String field = fieldError.getField();
        Object rejectedValue = fieldError.getRejectedValue();
        String message = fieldError.getDefaultMessage();
        return new ValidationApiSubError.Builder(object, message)
                .field(field)
                .rejectedValue(rejectedValue).build();
    }

    public static ValidationApiSubError toValidationApiSubError(ConstraintViolation<?> cv) {
        String object = cv.getRootBeanClass().getSimpleName();
        String field = ((PathImpl) cv.getPropertyPath()).getLeafNode().asString();
        Object rejectedValue = cv.getInvalidValue();
        String message = cv.getMessage();
        return new ValidationApiSubError.Builder(object, message)
                .field(field)
                .rejectedValue(rejectedValue).build();
    }
}
