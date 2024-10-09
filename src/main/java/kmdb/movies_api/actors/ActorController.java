package kmdb.movies_api.actors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping(path = "api/actors")
public class ActorController {

    private final ActorService actorService;

    @Autowired
    public ActorController(ActorService actorService) {
        this.actorService = actorService;
    }


    @GetMapping(path = "{actorId}") // retrieve data one by one using id as parameter
    public Optional<Actor> getActorsById(
            @PathVariable("actorId") Long actorId) {
        return actorService.getActorById(actorId);
    }

    @GetMapping //retrieve data by name or retrieve all if a parameter isn't given
    public List<Actor> findActorsByName(@RequestParam(required = false) String name) {
        return actorService.findActorsByName(name);
    }

    @PostMapping // add data
    public void addActor(@RequestBody Actor actor) {
        actorService.addActor(actor);
    }

    @DeleteMapping(path = "{actorId}") // delete data by id
    public void deleteActor(
            @PathVariable("actorId") Long actorId) {
        actorService.deleteActor(actorId);
    }

    @PatchMapping(path = "{actorId}") // modify data by id
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


