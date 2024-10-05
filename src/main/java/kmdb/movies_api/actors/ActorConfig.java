package kmdb.movies_api.actors;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;
import java.util.List;

@Configuration
public class ActorConfig {

    @Bean
        CommandLineRunner commandLineRunner(ActorRepository repository) {
            return args -> {
                Actor SteveCarell = new Actor(
                        "Steve Carell",
                        LocalDate.of(1962, 8, 16)
                );

                Actor RobertDeNiro = new Actor(
                        "Robert De Niro",
                        LocalDate.of(1943, 8, 17)
                );

                repository.saveAll(
                        List.of(SteveCarell, RobertDeNiro)
                );
            };
        }
}
