package kmdb.movies_api.repositories;

import kmdb.movies_api.entities.Actor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActorRepository
        extends JpaRepository<Actor, Long>,
                JpaSpecificationExecutor<Actor> {

    // Query to find actors by name from database
    @Query("SELECT actor FROM Actor actor WHERE actor.name = ?1")
    Optional<Actor> findByName(String name);

}
