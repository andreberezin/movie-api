package kmdb.movies_api.genres;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepository
        extends JpaRepository<Genre, Long>,
                JpaSpecificationExecutor<Genre> {

}
