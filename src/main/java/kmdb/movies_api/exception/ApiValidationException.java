package kmdb.movies_api.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

// TODO maybe don't need this?
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ApiValidationException {
    private HttpStatus httpStatus;
    private String error;
}
