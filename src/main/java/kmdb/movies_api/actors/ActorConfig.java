package kmdb.movies_api.actors;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class ActorConfig {

    @Bean
        CommandLineRunner commandLineRunnerActor(ActorRepository actorRepository) {
            return args -> {
                Actor actor1 = new Actor(
                        "Steve Carell",
                        LocalDate.of(1962, 8, 16)
                );

                Actor actor2 = new Actor(
                        "Robert De Niro",
                        LocalDate.of(1943, 8, 17)
                );

                actorRepository.saveAll(
                        List.of(actor1, actor2)
                );
            };
        }
}
