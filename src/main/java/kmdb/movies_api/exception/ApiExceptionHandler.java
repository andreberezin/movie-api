package kmdb.movies_api.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)  // Returning 400 for bad request
    public ResponseEntity<Map<String, String>> handleMethodValidationExceptions(MethodArgumentNotValidException exception) {
        Map<String, String> errors = new HashMap<>();

        // Extract field-specific error messages
        exception.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);  // Field name as key, error message as value
        });

        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

/*    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiException handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        return new ApiException(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }*/

/*    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleDataIntegrityViolation(DataIntegrityViolationException exception) {
        // Handle DB constraint violations here
        return new ResponseEntity<>("Database error: " + exception.getMessage(), HttpStatus.BAD_REQUEST);
    }*/

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, Object>> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getConstraintViolations().forEach(violation -> {
            String fieldName = violation.getPropertyPath().toString();
            String errorMessage = violation.getMessage();
            errors.put(fieldName, errorMessage);
        });

        // Create response body with status code and error messages
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("HttpStatus", String.format(HttpStatus.BAD_REQUEST.value() + " " + HttpStatus.BAD_REQUEST.getReasonPhrase())); // Add status code 400 to the body
        responseBody.put("errors", errors); // Add validation error messages

        return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST); // Return response with status code
    }

   @ExceptionHandler(value = {ResourceNotFoundException.class})
   @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiException> handleResourceNotFoundException(ResourceNotFoundException exception) {
        // 1. create a payload containing exception and details
        ApiException resourceNotFoundException = new ApiException(
                exception.getMessage(),
                HttpStatus.NOT_FOUND
                //ZonedDateTime.now(ZoneId.of("Z"))
        );
        // 2. return response entity
        return new ResponseEntity<>(resourceNotFoundException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {ResourceAlreadyExistsException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ApiException> handleResourceExists(ResourceAlreadyExistsException exception) {
        // 1. create a payload containing exception and details
        ApiException resourceAlreadyExists = new ApiException(
                exception.getMessage(),
                HttpStatus.CONFLICT
                //ZonedDateTime.now(ZoneId.of("Z"))
        );
        // 2. return response entity
        return new ResponseEntity<>(resourceAlreadyExists, HttpStatus.CONFLICT);
    }
}
