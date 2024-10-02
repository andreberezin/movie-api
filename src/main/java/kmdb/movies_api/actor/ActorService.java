package kmdb.movies_api.actor;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
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


    public List<Actor> getActors() {
        return actorRepository.findAll();
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
        Actor actor = actorRepository.findById(actorId)
                .orElseThrow(() -> new IllegalStateException(
                        "Actor with id " + actorId + " does not exist in database"
                ));

        actor.setName(name);
    }
}
