package kmdb.movies_api.actors;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class Actor {
    @Id
    @SequenceGenerator(
            name = "actor_sequence",
            sequenceName = "actor_sequence",
            allocationSize = 1
    )

    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "actor_sequence"
    )

    private Long id;

    //@NotEmpty(message = "Name cannot be empty")
    @NotBlank(message = "Name cannot be empty")
    private String name;

    @Column(columnDefinition = "VARCHAR(10)")
    @Pattern(regexp = "(19|20)\\d{2}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])", message = "Please enter a valid date in yyyy-MM-dd format")
    private String birthDate;
        public Actor(String name, LocalDate birthDate) {
        this.name = name;
        this.birthDate = birthDate.toString();
    }
}
