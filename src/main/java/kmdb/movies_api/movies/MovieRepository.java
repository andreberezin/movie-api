package kmdb.movies_api.movies;

import kmdb.movies_api.actors.Actor;
import kmdb.movies_api.genres.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface MovieRepository
    extends JpaRepository<Movie, Long>,
            JpaSpecificationExecutor<Movie> {

    // Query to find movies by title from database
    @Query("SELECT movie FROM Movie movie WHERE movie.title = ?1")
    Optional<Movie> findByTitle(String title);

    // Query to find all movies associated with a given genre
    @Query("SELECT movie FROM Movie movie WHERE :genre MEMBER OF movie.genres")
    Set<Movie> findAllByGenresContains(Genre genre);

    // Query to find all movies associated with a given actor
    @Query("SELECT movie FROM Movie movie WHERE :actor MEMBER OF movie.actors")
    Set<Movie> findAllByActorsContains(Actor actor);

}
