package kmdb.movies_api.actors;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

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

    //@Min(message = "The smallest id number is 1", value = 1L)
    private Long id;

    @NotEmpty(message = "Name cannot be empty")
    private String name;

    private LocalDate birthDate;
        public Actor(String name, LocalDate birthDate) {
        this.name = name;
        this.birthDate = birthDate;
    }
}
