package ru.khvatov.learningprojects.testtasks.symbolfrequencycalculatorapi.api.error;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.util.MimeType;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;

import static java.lang.String.format;
import static java.util.stream.Collectors.joining;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    protected ResponseEntity<Object> handleConstraintViolation(final ConstraintViolationException ex) {
        final var httpStatus = HttpStatus.BAD_REQUEST;

        final var message = "Validation error.";

        final var apiError = new ApiError(httpStatus, message, ex);
        apiError.addConstraintViolationValidationErrors(ex.getConstraintViolations());

        return ResponseEntity
                .status(httpStatus)
                .body(apiError);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        final HttpStatus httpStatus = HttpStatus.METHOD_NOT_ALLOWED;

        final String unsupportedHttpMethod = ex.getMethod();


        final String supportedHttpMethods = Stream.ofNullable(ex.getSupportedHttpMethods())
                .flatMap(Collection::stream)
                .map(HttpMethod::name)
                .collect(joining(", "));

        final StringBuilder message = new StringBuilder().append(
                format("%s method is not supported for this request.", unsupportedHttpMethod)
        );

        if (!supportedHttpMethods.isBlank()) {
            message.append(" ");
            message.append(
                    format("Supported methods are %s", supportedHttpMethods)
            );
        }


        final ApiError apiError = new ApiError(httpStatus, message.toString(), ex);

        return ResponseEntity.status(httpStatus)
                .headers(headers)
                .body(apiError);


    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        final HttpStatus httpStatus = HttpStatus.UNSUPPORTED_MEDIA_TYPE;

        final String unsupportedHttpMediaType = Optional.ofNullable(ex.getContentType())
                .map(MimeType::toString)
                .orElse("null");

        final String supportedHttpMediaTypes = ex.getSupportedMediaTypes().stream()
                .map(MediaType::toString)
                .collect(joining(", "));

        final StringBuilder message = new StringBuilder(
                format("%s media type is not supported for this request.", unsupportedHttpMediaType)
        );

        if (!supportedHttpMediaTypes.isBlank()) {
            message.append(" ");
            message.append(
                    format("Supported media types are %s", supportedHttpMediaTypes)
            );
        }

        final ApiError apiError = new ApiError(httpStatus, message.toString(), ex);

        return ResponseEntity.status(httpStatus)
                .headers(headers)
                .body(apiError);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(
            HttpMediaTypeNotAcceptableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        final HttpStatus httpStatus = HttpStatus.NOT_ACCEPTABLE;

        final String unacceptableHttpMediaType = request.getHeader("Accept");

        final String supportedHttpMediaTypes = ex.getSupportedMediaTypes().stream()
                .map(MediaType::toString)
                .collect(joining(", "));

        final StringBuilder message = new StringBuilder(
                format("Cannot produce a response matching `%s` media type.", unacceptableHttpMediaType)
        );

        if (!supportedHttpMediaTypes.isBlank()) {
            message.append(" ");
            message.append(
                    format("Supported media types are %s", supportedHttpMediaTypes)
            );
        }

        final ApiError apiError = new ApiError(httpStatus, message.toString(), ex);

        return ResponseEntity.status(httpStatus)
                .headers(headers)
                .body(apiError);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(
            MissingPathVariableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        final String message = format("`%s` path variable is missing", ex.getVariableName());

        final ApiError apiError = new ApiError(httpStatus, message, ex);

        return ResponseEntity.status(httpStatus)
                .headers(headers)
                .body(apiError);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        final String message = format("`%s` parameter is missing", ex.getParameterName());

        final ApiError apiError = new ApiError(httpStatus, message, ex);

        return ResponseEntity.status(httpStatus)
                .headers(headers)
                .body(apiError);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(
            MissingServletRequestPartException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        final ApiError apiError = new ApiError(httpStatus, ex.getLocalizedMessage(), ex);

        return ResponseEntity.status(httpStatus)
                .headers(headers)
                .body(apiError);
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(
            ServletRequestBindingException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        final HttpStatus httpStatus = HttpStatus.valueOf(status.value());

        final ApiError apiError = new ApiError(httpStatus, ex.getLocalizedMessage(), ex);

        return ResponseEntity.status(httpStatus)
                .headers(headers)
                .body(apiError);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        final String message = "Validation error";

        final ApiError apiError = new ApiError(httpStatus, message, ex);

        apiError.addFieldValidationErrors(ex.getBindingResult().getFieldErrors());
        apiError.addGlobalValidationErrors(ex.getBindingResult().getGlobalErrors());

        return ResponseEntity.status(httpStatus)
                .headers(headers)
                .body(apiError);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        final String message = "Could not find the %s method handler for URL %s".formatted(ex.getHttpMethod(), ex.getRequestURL());

        final ApiError apiError = new ApiError(httpStatus, message, ex);

        return ResponseEntity.status(httpStatus)
                .headers(headers)
                .body(apiError);

    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        final HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

        final String message = "Malformed body request";

        final ApiError apiError = new ApiError(httpStatus, message, ex);

        return ResponseEntity
                .status(httpStatus)
                .headers(headers)
                .body(apiError);

    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotWritable(HttpMessageNotWritableException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        final HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

        final String contentType = Optional.ofNullable(headers.getContentType())
                .map(MediaType::toString)
                .orElse("unknown");

        final String message = "Error writing %s output".formatted(contentType);

        final ApiError apiError = new ApiError(httpStatus, message, ex);

        return ResponseEntity
                .status(httpStatus)
                .headers(headers)
                .body(apiError);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {

        final HttpStatus httpStatus = HttpStatus.valueOf(statusCode.value());

        final ApiError apiError = new ApiError(httpStatus, ex.getLocalizedMessage());

        return super.handleExceptionInternal(ex, apiError, headers, statusCode, request);
    }
}
