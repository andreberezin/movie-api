package kmdb.movies_api.actor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(path = "api/v1/actor")
public class ActorController {

    private final ActorService actorService;

    @Autowired
    public ActorController(ActorService actorService) {
        this.actorService = actorService;
    }

    @GetMapping // retrieve data
    public List<Actor> getActors() {
        return actorService.getActors();
    }

    @PostMapping // add data
    public void addActor(@RequestBody Actor actor) {
        actorService.addActor(actor);
    }

    @DeleteMapping(path = "{actorId}")
    public void deleteActor(
            @PathVariable("actorId") Long actorId) {
        actorService.deleteActor(actorId);
    }

    @PutMapping(path = "{actorId}")
    public void updateActor(
            @PathVariable("actorId") Long actorId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) LocalDate birthDate) {
        actorService.updateActor(actorId, name, birthDate);
    }
}
