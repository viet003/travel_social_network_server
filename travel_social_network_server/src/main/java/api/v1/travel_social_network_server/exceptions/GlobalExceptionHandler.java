package api.v1.travel_social_network_server.exceptions;

import api.v1.travel_social_network_server.reponses.Response;
import api.v1.travel_social_network_server.utilities.StatusRequestEnum;
import com.cloudinary.api.exceptions.BadRequest;
//import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
//@Hidden
public class GlobalExceptionHandler {
    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response<?> handleValidationExceptions(Exception ex) {
        Map<String, String> errors = new HashMap<>();
        if (ex instanceof MethodArgumentNotValidException validationEx) {
            validationEx.getBindingResult().getAllErrors().forEach((error) -> {
                String fieldName = ((FieldError) error).getField();
                String errorMessage = error.getDefaultMessage();
                errors.put(fieldName, errorMessage);
            });
        } else if (ex instanceof ConstraintViolationException constraintViolationEx) {
            constraintViolationEx.getConstraintViolations().forEach((violation) -> {
                String fieldName = "error";
                String errorMessage = violation.getMessage();
                errors.put(fieldName, errorMessage);
            });
        }
        return Response.builder()
                .status(StatusRequestEnum.FAIL)
                .message("BAD_REQUEST: Validation error")
                .data(errors)
                .build();
    }

    @ExceptionHandler(ResourceAlreadyExistedException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Response<?> handleResourceAlreadyExisted(ResourceAlreadyExistedException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return Response.builder()
                .status(StatusRequestEnum.FAIL)
                .message("CONFLICT: Resource already existed")
                .data(errors)
                .build();
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Response<?> handleResourceNotExisted(ResourceNotFoundException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return Response.builder()
                .status(StatusRequestEnum.FAIL)
                .message("NOT_FOUND : Resource is not existed")
                .data(errors)
                .build();
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public Response<?> handleBadCredentials(BadCredentialsException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return Response.builder()
                .status(StatusRequestEnum.FAIL)
                .message("UNAUTHORIZED: Bad credentials.")
                .data(errors)
                .build();
    }

    @ExceptionHandler(BadRequest.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Response<?> handleBadCredentials(BadRequest ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return Response.builder()
                .status(StatusRequestEnum.FAIL)
                .message("BAD_REQUEST: Bad credentials.")
                .data(errors)
                .build();
    }


    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.PAYLOAD_TOO_LARGE)
    public Response<?> handleMaxSizeException(MaxUploadSizeExceededException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", "File size exceeds limit.");
        return Response.builder()
                .status(StatusRequestEnum.FAIL)
                .message("PAYLOAD_TOO_LARGE: File size exceeds limit.")
                .data(errors)
                .build();
    }

    @ExceptionHandler(DisabledException.class)
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public Response<?> handleNotActiveAccount(DisabledException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        return Response.builder()
                .status(StatusRequestEnum.FAIL)
                .message("NOT_ACCEPTABLE: your account is not active yet. Please contact us for more information.")
                .data(errors)
                .build();
    }
}
