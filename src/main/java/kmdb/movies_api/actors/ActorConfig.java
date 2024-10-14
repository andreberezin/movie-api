/*
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
            return args -> { // need 15 actors
                Actor actor1 = new Actor(
                        "Steve Carell",
                        LocalDate.of(1962, 8, 16)
                );
                Actor actor2 = new Actor(
                        "Robert De Niro",
                        LocalDate.of(1943, 8, 17)
                );
                Actor actor3 = new Actor(
                        "Tom Hanks",
                        LocalDate.of(1956, 7, 9)
                );
                Actor actor4 = new Actor(
                        "Jack Nicholson",
                        LocalDate.of(1937, 3, 22)
                );
                Actor actor5 = new Actor(
                        "Denzel Washington",
                        LocalDate.of(1954, 12, 28)
                );
                Actor actor6 = new Actor(
                        "Al Pacino",
                        LocalDate.of(1940, 3, 25)
                );
                Actor actor7 = new Actor(
                        "Morgan Freeman",
                        LocalDate.of(1937, 6, 1)
                );
                Actor actor8 = new Actor(
                        "Christian Bale",
                        LocalDate.of(1974, 1, 30)
                );
                Actor actor9 = new Actor(
                        "Helen Hunt",
                        LocalDate.of(1963, 6, 15)
                );



                actorRepository.saveAll(
                        List.of(actor1, actor2, actor3, actor4, actor5, actor6, actor7, actor8, actor9)
                );
            };
        }
}
*/
