package kmdb.movies_api.actors;

import jakarta.transaction.Transactional;
import kmdb.movies_api.exceptions.ResourceAlreadyExistsException;
import kmdb.movies_api.exceptions.ResourceNotFoundException;
import kmdb.movies_api.movies.Movie;
import kmdb.movies_api.movies.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ActorService {

    private final ActorRepository actorRepository;
    private final MovieRepository movieRepository;

    @Autowired
    public ActorService(ActorRepository actorRepository, MovieRepository movieRepository) {
        this.actorRepository = actorRepository;
        this.movieRepository = movieRepository;
    }

    // get all actors
    public Optional<List<Actor>> getAllActors() {
        List<Actor> actorsList = actorRepository.findAll();
        if (actorsList.isEmpty()) {
            throw new ResourceNotFoundException("No actors found in the database");
        }
        return Optional.of(actorsList);
    }

    // get number of actors
    public String getActorCount() {
        return "Actors in database: " + actorRepository.count();
    }

    // get all the actors or actors by page number and size
    public Optional<List<Actor>> getActorsByPage(int page, int size) {
        if (size > 100) {
            throw new IllegalArgumentException("Page size must be less than or equal to 100");
        }
        PageRequest pageable = PageRequest.of(page, size);
        Page<Actor> actorsPage = actorRepository.findAll(pageable);
        List<Actor> actorsList = actorsPage.getContent();
        if (actorsList.isEmpty()) {
            throw new ResourceNotFoundException("No actors found on page " + page);
        } else {
            return Optional.of(actorsList);
        }
    }

    // get actors by id
    public Optional<Actor> getActorById(Long actorId) {
        if (actorId < 1) {
            throw new IllegalArgumentException("Actor ID must be greater than 0");
        }
        Optional<Actor> actor = actorRepository.findById(actorId);
        if (actor.isPresent()) {
            return actor;
        } else {
            throw new ResourceNotFoundException("Actor with ID " + actorId + " does not exist");
        }
    }

    // method to check if there is a match in database for entered name when filtering actors by name
    public static Specification<Actor> nameContains(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isEmpty()) {
                return criteriaBuilder.conjunction(); // Return true if no name is specified
            } else {
                return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
            }
        };
    }

    // filter actors by name
    public Optional<List<Actor>> findActorsByName(String name) {
        Specification<Actor> spec = nameContains(name);
        List<Actor> actorsList = actorRepository.findAll(spec);

        if (actorsList.isEmpty()) { // in case no movie matches given title
            throw new ResourceNotFoundException("Actor with name containing '" + name + "' does not exist");
        } else {
            return Optional.of(actorsList);
        }
    }

    // filter movies by actor
    public Optional<List<Movie>> getMoviesByActor(Long actorId) {
        if (actorId < 1) {
            throw new IllegalArgumentException("Genre ID must be greater than 0");
        }

        Actor actor = actorRepository.findById(actorId)
                .orElseThrow(() -> new ResourceNotFoundException("Actor with ID " + actorId + " does not exist"));

        List<Movie> moviesList = new ArrayList<>(movieRepository.findAllByActorsContains(actor));

        if (moviesList.isEmpty()) {
            throw new ResourceNotFoundException("No movies found starring actor '" + actor.getName() + "'");
        } else {
            return Optional.of(moviesList);
        }
    }

    // add actor
    public ResponseEntity<String> addActor(Actor actor) {
        Optional<Actor> actorOptional = actorRepository
                .findByName(actor.getName());

        if(actorOptional.isPresent()) {
                    throw new ResourceAlreadyExistsException("Actor '" + actor.getName() + "' already exists");
        } else {
            actorRepository.save(actor);
            return new ResponseEntity<>("Actor '" + actor.getName() + "' added successfully", HttpStatus.CREATED);
        }
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
