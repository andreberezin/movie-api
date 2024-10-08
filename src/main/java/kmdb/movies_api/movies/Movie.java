package kmdb.movies_api.movies;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table
@Getter @Setter @NoArgsConstructor
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
    private String title;
    private int releaseYear;
    private double duration;

    public Movie(String title, int releaseYear, double duration) {
        this.title = title;
        this.releaseYear = releaseYear;
        this.duration = duration;
    }

}
