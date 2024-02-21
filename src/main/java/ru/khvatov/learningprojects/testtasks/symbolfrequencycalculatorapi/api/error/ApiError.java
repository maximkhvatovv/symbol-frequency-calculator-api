package ru.khvatov.learningprojects.testtasks.symbolfrequencycalculatorapi.api.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import jakarta.validation.ConstraintViolation;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import ru.khvatov.learningprojects.testtasks.symbolfrequencycalculatorapi.api.error.suberrors.ApiSubError;
import ru.khvatov.learningprojects.testtasks.symbolfrequencycalculatorapi.api.error.util.ErrorMappers;
import ru.khvatov.learningprojects.testtasks.symbolfrequencycalculatorapi.api.error.util.LowerCaseClassNameResolver;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.CUSTOM)
@JsonTypeIdResolver(LowerCaseClassNameResolver.class)
@Data
public class ApiError {
    private HttpStatus status;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;

    private String message;
    private String debugMessage;

    private List<ApiSubError> subErrors;

    public ApiError() {
        this.timestamp = LocalDateTime.now();
    }

    public ApiError(HttpStatus status) {
        this();
        this.status = status;
    }

    public ApiError(HttpStatus status, Throwable exception) {
        this(status);
        this.message = "Unexpected error";
        this.debugMessage = exception.getLocalizedMessage();
    }

    public ApiError(HttpStatus status, String message) {
        this(status);
        this.message = message;
    }

    public ApiError(HttpStatus status, String message, Throwable exception) {
        this(status);
        this.message = message;
        this.debugMessage = exception.getLocalizedMessage();
    }

    public void addGlobalValidationErrors(List<ObjectError> globalErrors) {
        globalErrors.stream()
                .map(ErrorMappers::toValidationApiSubError)
                .forEach(this::addSubError);
    }

    private void addSubError(ApiSubError e) {
        if (subErrors == null) {
            subErrors = new ArrayList<>();
        }
        subErrors.add(e);
    }

    public void addFieldValidationErrors(List<FieldError> fieldErrors) {
        fieldErrors.stream()
                .map(ErrorMappers::toValidationApiSubError)
                .forEach(this::addSubError);
    }


    public void addConstraintViolationValidationErrors(Set<ConstraintViolation<?>> constraintViolations) {
        constraintViolations.stream()
                .map(ErrorMappers::toValidationApiSubError)
                .forEach(this::addSubError);
    }


}
