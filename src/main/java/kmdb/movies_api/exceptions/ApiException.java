package kmdb.movies_api.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class ApiException {
    private String httpStatus;
    private ArrayList<String> errors;
}
