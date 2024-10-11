package kmdb.movies_api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ApiException {
    private String httpStatus;
    private ArrayList<String> errors;
    //private List<String> errors;
    //private ZonedDateTime timestamp;

/*
    public ApiException(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
        //this.timestamp = timestamp;
    }
*/

/*    public ApiException(String message, HttpStatus httpStatus, List<String> error) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.errors = errors;
        //this.timestamp = timestamp;
    }

    public ApiException(String message, HttpStatus httpStatus, String error) {
        this.message = message;
        this.httpStatus = httpStatus;
        errors = Arrays.asList(error);
        //this.timestamp = timestamp;
    }*/

/*    public ApiException(String message) {
    }

    public ApiException(String message, Throwable cause) {
    }*/

/*    public ApiException(String message, HttpStatus httpStatus) {
    }*/
}
