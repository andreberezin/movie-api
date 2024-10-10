package kmdb.movies_api.actors;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table
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

    @NotNull(message = "Name cannot be null")
    @NotEmpty(message = "Name cannot be empty")
    private String name;

    @NotNull
    private LocalDate birthDate;

    /*    public Actor(Long id, String name, LocalDate birthDate) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
    }*/

        public Actor(String name, LocalDate birthDate) {
        this.name = name;
        this.birthDate = birthDate;
    }

/*    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }*/

/*    @Override
    public String toString() {
        return "Actor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birthDate=" + birthDate +
                '}';
    }*/
}
