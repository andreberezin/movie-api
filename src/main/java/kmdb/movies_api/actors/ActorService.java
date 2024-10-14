package kmdb.movies_api.actors;

import jakarta.transaction.Transactional;
import kmdb.movies_api.exception.ResourceAlreadyExistsException;
import kmdb.movies_api.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ActorService {

    private final ActorRepository actorRepository;

    public ActorService(ActorRepository actorRepository) {
        this.actorRepository = actorRepository;
    }

    // get all the actors or actors by page number and size
    public List<Actor> getActorsByPage(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<Actor> actorsPage = actorRepository.findAll(pageable);
        List<Actor> actorsList = actorsPage.getContent();
        if (actorsList.isEmpty()) {
            throw new ResourceNotFoundException("No actors found on page " + page);
        }
        return actorsList;
    }

    // get actors by id
    public Actor getActorById(Long actorId) {
        if (actorId < 1) {
            throw new IllegalArgumentException("Actor ID must be greater than 0");
        }
       return actorRepository.findById(actorId)
               .orElseThrow(() -> new ResourceNotFoundException("Actor with ID " + actorId + " does not exist")
       );

    }
    // method to check if there is a match in database for entered name when filtering actors by name
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
        Specification<Actor> spec = nameContains(name);

        if (actorRepository.findAll().isEmpty()) { // in case no movies are found
            throw new ResourceNotFoundException("No actors found");
        }

        if (actorRepository.findAll(spec).isEmpty()) { // in case no movie matches given title
            throw new ResourceNotFoundException("Actor with name containing '" + name + "' does not exist");
        }

        return actorRepository.findAll(spec);
    }

    // add actor
    public ResponseEntity<String> addActor(Actor actor) {
        Optional<Actor> actorOptional = actorRepository
                .findByName(actor.getName());
        if(actorOptional.isPresent()) {
                    throw new ResourceAlreadyExistsException("Actor '" + actor.getName() + "' already exists");
        }
        actorRepository.save(actor);
        return new ResponseEntity<>("Actor '" + actor.getName() + "' added successfully", HttpStatus.CREATED);
    }

    // remove actor
    public void deleteActor(Long actorId, boolean force) {
        if (actorId < 1) {
            throw new IllegalArgumentException("Actor ID must be greater than 0");
        }
        Actor actor = actorRepository.findById(actorId)
                .orElseThrow(() -> new ResourceNotFoundException("Actor with ID " + actorId + " does not exist"));

        int numOfMovies = actor.getMovies().size();

        if (force) { // if force is true then delete resource regardless of relationships
            actorRepository.deleteById(actorId);
        }

        if (numOfMovies > 0) { // if force is false and relationships exist then return exception
            if (numOfMovies == 1) {
                throw new IllegalStateException(("Cannot delete actor '" + actor.getName() + "' because they are associated with " + numOfMovies + " movie"));
            } else {
                throw new IllegalStateException(("Cannot delete actor '" + actor.getName() + "' because they are associated with " + numOfMovies + " movies"));
            }
        }
        actorRepository.deleteById(actorId); // if force is false and relationships do not exist then delete resource
    }

    // update actor
    @Transactional
    public void updateActor(Long actorId, String name, String birthDate) {
        if (actorId < 1) {
            throw new IllegalArgumentException("Actor ID must be greater than 0");
        }
        Actor actor = actorRepository.findById(actorId)
                .orElseThrow(() -> new ResourceNotFoundException("Actor with ID " + actorId + " does not exist"));

        if (name != null && !name.isEmpty()) { // update only non-null fields
            actor.setName(name);
        }

        if (birthDate != null) { // update only non-null fields
            actor.setBirthDate(birthDate);
        }
    }
}
