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
                        "Brad Pitt",
                        LocalDate.of(1963, 12, 18)
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
                Actor actor10 = new Actor(
                        "Leonardo DiCaprio",
                        LocalDate.of(1974, 11, 11)
                );
                Actor actor11 = new Actor(
                        "Emily Blunt",
                        LocalDate.of(1983, 2, 23)
                );
                Actor actor12 = new Actor(
                        "Matt Damon",
                        LocalDate.of(1970, 10, 8)
                );
                Actor actor13 = new Actor(
                        "Jeff Goldblum",
                        LocalDate.of(1952, 10, 22)
                );
                Actor actor14 = new Actor(
                        "Russel Crowe",
                        LocalDate.of(1964, 3, 7)
                );
                Actor actor15 = new Actor(
                        "Robin Wright",
                        LocalDate.of(1966, 3, 8)
                );
                Actor actor16 = new Actor(
                        "Samuel L. Jackson",
                        LocalDate.of(1948, 12, 21)
                );
                Actor actor17 = new Actor(
                        "Uma Thurman",
                        LocalDate.of(1970, 3, 29)
                );


                actorRepository.saveAll(
                        List.of(actor1, actor2, actor3, actor4, actor5, actor6, actor7, actor8, actor9, actor10, actor11, actor12, actor13, actor14, actor15, actor16, actor17)
                );


            };
        }
}
*/
