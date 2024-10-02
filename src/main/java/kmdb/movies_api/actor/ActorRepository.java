package kmdb.movies_api.actor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActorRepository
        extends JpaRepository<Actor, Long> {

    @Query("SELECT actor FROM Actor actor WHERE actor.name = ?1")
    Optional<Actor> findByName(String name);

}
