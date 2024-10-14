package kmdb.movies_api.genres;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GenreRepository
        extends JpaRepository<Genre, Long>,
                JpaSpecificationExecutor<Genre> {

    // Query to find genres by name from database
    @Query("SELECT genre FROM Genre genre WHERE genre.name = ?1")
    Optional<Genre> findByName(String name);
}
