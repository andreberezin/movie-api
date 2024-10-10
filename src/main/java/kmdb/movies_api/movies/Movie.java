package kmdb.movies_api.movies;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;


@Entity
@Table
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @ToString
public class Movie {
    @Id
    @SequenceGenerator(
            name = "movie_sequence",
            sequenceName = "movie_sequence",
            allocationSize = 1
    )

    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "movie_sequence"
    )

    private Long id;

    @NotEmpty(message = "Title cannot be empty")
    private String title;

    @Min(value = 0, message = "Movie release year must be between 0 and 2300")
    @Max(value = 2300, message = "Movie release year must be between 0 and 2300")
    private int releaseYear;

    @Min(value = 0, message = "Movie duration must be between 0 and 1000 minutes")
    @Max(value = 1000, message = "Movie duration must be between 0 and 1000 minutes")
    private int duration;

    public Movie(String title, int releaseYear, int duration) {
        this.title = title;
        this.releaseYear = releaseYear;
        this.duration = duration;
    }
}
