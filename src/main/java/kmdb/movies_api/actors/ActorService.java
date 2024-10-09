package kmdb.movies_api.actors;

import jakarta.transaction.Transactional;
import kmdb.movies_api.exception.ApiRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ActorService {

    private final ActorRepository actorRepository;

    @Autowired
    public ActorService(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }


/*    public List<Actor> getActors() { // findActorsByName already does this if no parameter is given
        return actorRepository.findAll();
    }*/

    public Optional<Actor> getActorById(Long actorId) {

        boolean exists = actorRepository.existsById(actorId);
        if(!exists) {
            throw new ApiRequestException("Actor with id " + actorId + " does not exist");
        }

       return actorRepository.findById(actorId);
    }

    public static Specification<Actor> nameContains(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isEmpty()) {
                return criteriaBuilder.conjunction(); // Return true if no name is specified
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    // filter actors by name
    public List<Actor> findActorsByName(String name) {
        if (name == null || name.isEmpty()) {
            return actorRepository.findAll(); // Return all actors if no name is specified
        }
        Specification<Actor> spec = nameContains(name);
        return actorRepository.findAll(spec);
    }

    public void addActor(Actor actor) {
        Optional<Actor> actorOptional = actorRepository
                .findByName(actor.getName());
        if(actorOptional.isPresent()) {
            throw new IllegalStateException("Actor already exists in database");
        }
        actorRepository.save(actor);
    }

    public void deleteActor(Long actorId) {
        boolean exists = actorRepository.existsById(actorId);
        if(!exists) {
            throw new IllegalStateException(
                    "Actor with id " + actorId + " does not exist in database");
        }
        actorRepository.deleteById(actorId);
    }

    @Transactional
    public void updateActor(Long actorId, String name, LocalDate birthDate) {
        Actor actor = actorRepository.findById(actorId) // fetch entity from db
                .orElseThrow(() -> new IllegalStateException(
                        "Actor with id " + actorId + " does not exist in database"
                ));

        if (name != null && !name.isEmpty()) { // update only non-null fields
            actor.setName(name);
        }

        if (birthDate != null) { // update only non-null fields
            actor.setBirthDate(birthDate);
        }

        //actorRepository.save(actor); // @Transactional already saves automatically
    }


}
