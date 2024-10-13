package kmdb.movies_api.movies;

import kmdb.movies_api.genres.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.Set;

public interface MovieRepository
    extends JpaRepository<Movie, Long>,
            JpaSpecificationExecutor<Movie> {
    @Query("SELECT movie FROM Movie movie WHERE movie.title = ?1")
    Optional<Movie> findByTitle(String title);

    // TODO figure out how to include associated movies when getting genres from database
    @Query("SELECT movie FROM Movie movie WHERE :genre MEMBER OF movie.genres")
    Set<Movie> findAllByGenresContains(Genre genre);

}
