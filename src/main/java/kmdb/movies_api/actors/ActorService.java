package kmdb.movies_api.actors;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import kmdb.movies_api.exception.ResourceAlreadyExistsException;
import kmdb.movies_api.exception.ResourceNotFoundException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ActorService {

    private final ActorRepository actorRepository;

    public ActorService(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }


/*    public List<Actor> getActors() { // findActorsByName already does this if no parameter is given
        return actorRepository.findAll();
    }*/

    public Actor getActorById(Long actorId) {
       return actorRepository.findById(actorId)
               .orElseThrow(() -> new ResourceNotFoundException("Actor with id " + actorId + " does not exist")
       );
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

        if (actorRepository.findAll(spec).isEmpty()) { // in case no movie matches given title
            throw new ResourceNotFoundException("Actor with name containing " + name + " does not exist");
        }

        return actorRepository.findAll(spec);
    }

    public void addActor(@Valid @RequestBody Actor actor) {
        Optional<Actor> actorOptional = actorRepository
                .findByName(actor.getName());
        if(actorOptional.isPresent()) {
                    throw new ResourceAlreadyExistsException("Actor " + actor.getName() + " already exists");
        }
        actorRepository.save(actor);
    }

    public void deleteActor(Long actorId) {
        actorRepository.findById(actorId)
                .orElseThrow(() -> new ResourceNotFoundException("Actor with id " + actorId + " does not exist"));
        actorRepository.deleteById(actorId);
    }



    @Transactional
    public void updateActor(Long actorId, String name, LocalDate birthDate) {
        Actor actor = actorRepository.findById(actorId)
                .orElseThrow(() -> new ResourceNotFoundException("Actor with id " + actorId + " does not exist"));

        if (name != null && !name.isEmpty()) { // update only non-null fields
            actor.setName(name);
        }

        if (birthDate != null) { // update only non-null fields
            actor.setBirthDate(birthDate);
        }

        //actorRepository.save(actor); // @Transactional already saves automatically
    }


}
