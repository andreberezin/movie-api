package kmdb.movies_api.genres;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import kmdb.movies_api.movies.Movie;
import lombok.*;

import java.util.HashSet;
import java.util.Set;


@Entity
@Table
@Setter @Getter @NoArgsConstructor @AllArgsConstructor @ToString
public class Genre {
   @Id
   @SequenceGenerator(
           name = "genre_sequence",
           sequenceName = "genre_sequence",
           allocationSize = 1
   )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "genre_sequence"
    )

   private Long id;

   @NotBlank(message = "Name cannot be empty")
   private String name;

 /*@Getter
   @ManyToMany
   @JoinTable (
           name = "movies_by_genre",
           joinColumns = @JoinColumn(name = "genre_id"),
           inverseJoinColumns = @JoinColumn(name = "movie_id"  )
   )
   private Set<Movie> moviesByGenre = new HashSet<>();*/

   @JsonIgnore
   @ManyToMany(mappedBy = "genres")
   @Getter
   private Set<Movie> movies = new HashSet<>();

   public Genre (String name) {
       this.name = name;
   }

/*    public void setMovie(Movie movie) {
       moviesByGenre.add(movie);
    }*/

}