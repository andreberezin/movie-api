package kmdb.movies_api.actors;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping(path = "api/actors")
public class ActorController {

    private final ActorService actorService;

    public ActorController(ActorService actorService) {
        this.actorService = actorService;
    }

    @GetMapping(params = { "page", "size"}) // retrieve by page
    @ResponseStatus(HttpStatus.OK)
    public List<Actor> getActors(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
        return actorService.getActors(page, size);
    }

    @GetMapping(path = "{actorId}") // retrieve data one by one using id as parameter
    @ResponseStatus(HttpStatus.OK)
    public Actor getActorsById(@PathVariable("actorId") Long actorId) {
        return actorService.getActorById(actorId);
    }

    @GetMapping //retrieve data by name or retrieve all if a parameter isn't given
    @ResponseStatus(HttpStatus.OK)
    public List<Actor> findActorsByName(@RequestParam(required = false) String name) {
        return actorService.findActorsByName(name);
    }

    @PostMapping // add data
    @ResponseStatus(HttpStatus.CREATED)
    public void addActor(@Valid @RequestBody Actor actor) {
        actorService.addActor(actor);
    }

    @DeleteMapping(path = "{actorId}") // delete data by id
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteActor(
            @PathVariable("actorId") Long actorId) {
        actorService.deleteActor(actorId);
    }

    @PatchMapping(path = "{actorId}") // modify data by id
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateActor(
            @PathVariable("actorId") Long actorId,

           // modifying via body
            @RequestBody Actor request) {
        actorService.updateActor(actorId, request.getName(), request.getBirthDate());
    }
            // modifying via parameters
/*
            @RequestParam(required = false) String name,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate birthDate)) {
*/

    }


