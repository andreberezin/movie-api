package kmdb.movies_api.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.ArrayList;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class) // handle MethodArgumentNotValidException
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiException> handleMethodValidationExceptions(MethodArgumentNotValidException exception) {
        ArrayList<String> errors = new ArrayList<>();

        exception.getBindingResult().getAllErrors().forEach(error -> { // add every violation to the Array since there can be more tha one validation violation
            String errorMessage = error.getDefaultMessage();
            errors.add(errorMessage);
        });

        ApiException constraintViolationException = new ApiException(
                String.format(HttpStatus.BAD_REQUEST.value() + " " + HttpStatus.BAD_REQUEST.getReasonPhrase()), // reformat httpStatus so looks better
                errors
        );

        return new ResponseEntity<>(constraintViolationException, HttpStatus.BAD_REQUEST); // Return response with status code
    }

    @ExceptionHandler(ConstraintViolationException.class) // handle ConstraintViolationException
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiException> handleConstraintViolationException(ConstraintViolationException exception) {
        ArrayList<String> errors = new ArrayList<>();

        exception.getConstraintViolations().forEach(violation -> { // add every violation to the Array since there can be more tha one validation violation
            String errorMessage = violation.getMessage();
            errors.add(errorMessage);
        });

        ApiException constraintViolationException = new ApiException(
                String.format(HttpStatus.BAD_REQUEST.value() + " " + HttpStatus.BAD_REQUEST.getReasonPhrase()), // reformat httpStatus so looks better
                errors
        );

        return new ResponseEntity<>(constraintViolationException, HttpStatus.BAD_REQUEST); // Return response with status code
    }

    @ExceptionHandler(IllegalArgumentException.class) // handle IllegalArgumentException
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiException> handleIllegalArgumentException(IllegalArgumentException exception) {
        ArrayList<String> errors = new ArrayList<>();
        errors.add(exception.getMessage()); // add the error message to the Array

        ApiException illegalArgumentException = new ApiException(
                String.format(HttpStatus.BAD_REQUEST.value() + " " + HttpStatus.BAD_REQUEST.getReasonPhrase()), // reformat httpStatus so looks better
                errors
        );

        return new ResponseEntity<>(illegalArgumentException, HttpStatus.BAD_REQUEST); // Return response with status code
    }


    @ExceptionHandler(NoHandlerFoundException.class) // handle NoHandlerFoundException
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiException> handleNoHandlerFoundException(NoHandlerFoundException exception) {
        ArrayList<String> errors = new ArrayList<>();
        errors.add(exception.getMessage()); // add the error message to the Array

        ApiException noHandlerFoundException = new ApiException(
                String.format(HttpStatus.NOT_FOUND.value() + " " + HttpStatus.NOT_FOUND.getReasonPhrase()), // reformat httpStatus so looks better
                errors
        );

        return new ResponseEntity<>(noHandlerFoundException, HttpStatus.NOT_FOUND); // Return response with status code
    }

   @ExceptionHandler(ResourceNotFoundException.class) // custom exception for missing resource
   @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ApiException> handleResourceNotFoundException(ResourceNotFoundException exception) {
       ArrayList<String> errors = new ArrayList<>();
       errors.add(exception.getMessage());

       ApiException resourceNotFoundException = new ApiException(
               String.format(HttpStatus.NOT_FOUND.value() + " " + HttpStatus.NOT_FOUND.getReasonPhrase()),
               errors
       );

        return new ResponseEntity<>(resourceNotFoundException, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ResourceAlreadyExistsException.class) // custom exception for when resource already exists
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ApiException> handleResourceExists(ResourceAlreadyExistsException exception) {
        ArrayList<String> errors = new ArrayList<>();
        errors.add(exception.getMessage());

        ApiException resourceAlreadyExists = new ApiException(
                String.format(HttpStatus.CONFLICT.value() + " " + HttpStatus.CONFLICT.getReasonPhrase()),
                errors
        );

        return new ResponseEntity<>(resourceAlreadyExists, HttpStatus.CONFLICT);
    }
}
