package kmdb.movies_api.movies;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface MovieRepository
    extends JpaRepository<Movie, Long>,
            JpaSpecificationExecutor<Movie> {
    @Query("SELECT movie FROM Movie movie WHERE movie.title = ?1")
    Optional<Movie> findByTitle(String title);
}
