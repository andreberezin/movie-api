package kmdb.movies_api.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;


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

    @NotBlank(message = "Title cannot be empty")
    private String title;

    @Min(value = 0, message = "Movie release year must be between 0 and 2300")
    @Max(value = 2300, message = "Movie release year must be between 0 and 2300")
    private int releaseYear;

    @Min(value = 0, message = "Movie duration must be between 0 and 1000 minutes")
    @Max(value = 1000, message = "Movie duration must be between 0 and 1000 minutes")
    private int duration;

    // ManyToMany mapping between movies and actors

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable (
            name = "actors",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "actor_id"))
    @Getter
    private Set<Actor> actors = new HashSet<>();

    // ManyToMany mapping between movies and genres

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable (
            name = "genres",
            joinColumns = @JoinColumn(name = "movie_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"  ))
    @Getter
    private Set<Genre> genres = new HashSet<>();

    public Movie(String title, int releaseYear, int duration) {
        this.title = title;
        this.releaseYear = releaseYear;
        this.duration = duration;
    }

    public void setActor(Actor actor) {
        actors.add(actor);
    }

    public void removeActor(Actor actor) {
        actors.remove(actor);
    }

    public void setGenre(Genre genre) { genres.add(genre);}

    public void removeGenre(Genre genre) {
        genres.remove(genre);
    }

    public Movie(Set<Genre> genres) {
        this.genres = genres;
    }
}
