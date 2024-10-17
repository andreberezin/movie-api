package kmdb.movies_api.actors;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Validated
@RequestMapping(path = "api/actors")
public class ActorController {

    private final ActorService actorService;

    @Autowired
    public ActorController(ActorService actorService) {
        this.actorService = actorService;
    }

    // get all actors
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Optional<List<Actor>> getAllActors() {
        return actorService.getAllActors();
    }

    // get number of actors
    @GetMapping(params = "count")
    @ResponseStatus(HttpStatus.OK)
    public String getActorCount() {
        return actorService.getActorCount();
    }

    // get actors by page and page size
    @GetMapping(params = { "page", "size"}) // retrieve by page
    @ResponseStatus(HttpStatus.OK)
    public Optional<List<Actor>> getActorsByPage(
            @Min(value = 0, message = "Page index must not be less than zero")
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,

            @Min(value = 0, message = "Page size must not be less than one")
            @Max(value = 100, message = "Page size limit is 100")
            @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
        return actorService.getActorsByPage(page, size);
    }

    // get actors by id
    @GetMapping(path = "{actorId}") // retrieve data one by one using id as parameter
    @ResponseStatus(HttpStatus.OK)
    public Optional<Actor> getActorsById(
            @PathVariable @Positive(message = "Actor ID must be greater than 0") Long actorId) {
        return actorService.getActorById(actorId);
    }

    //retrieve data by name or retrieve all if a parameter isn't given
    @GetMapping(path = "/search")
    @ResponseStatus(HttpStatus.OK)
    public Optional<List<Actor>> findActorsByName(@RequestParam(required = false) String name) {
        return actorService.findActorsByName(name);
    }

    // add data
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> addActor(@Valid @RequestBody Actor actor) {
        return actorService.addActor(actor);
    }

    // delete data by id. Also include force parameter in case relationships exist
    @DeleteMapping(path = "{actorId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteActor(
            @PathVariable("actorId") @Positive(message = "Movie ID must be greater than 0") Long actorId,
            @RequestParam(value = "force", defaultValue = "false", required = false) boolean force) {
        actorService.deleteActor(actorId, force);
    }

    // modify data by id
    @PatchMapping(path = "{actorId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateActor(
            @PathVariable("actorId") @Positive(message = "Movie ID must be greater than 0") Long actorId,
            @RequestBody Actor request) { // modifying via body
        actorService.updateActor(actorId, request.getName(), request.getBirthDate());
        }
    }


