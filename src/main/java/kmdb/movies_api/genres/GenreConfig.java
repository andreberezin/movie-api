package kmdb.movies_api.genres;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class GenreConfig {

    @Bean
    CommandLineRunner commandLineRunnerGenre(GenreRepository genreRepository) {
        return args -> {
            Genre genre1 = new Genre(
                    "action"
            );
            Genre genre2 = new Genre(
                    "sci-fi"
            );

            genreRepository.saveAll(
                    List.of(genre1, genre2)
            );
        };
    }

}
