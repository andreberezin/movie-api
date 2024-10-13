package kmdb.movies_api.genres;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class GenreConfig {

    @Bean
    CommandLineRunner commandLineRunnerGenre(GenreRepository genreRepository) {
        return args -> { // need 5 genres
            Genre genre1 = new Genre(
                    "action"
            );
            Genre genre2 = new Genre(
                    "sci-fi"
            );
            Genre genre3 = new Genre(
                    "drama"
            );
            Genre genre4 = new Genre(
                    "comedy"
            );
            Genre genre5 = new Genre(
                    "adventure"
            );
            Genre genre6 = new Genre(
                    "romance"
            );

            genreRepository.saveAll(
                    List.of(genre1, genre2, genre3, genre4, genre5, genre6)
            );
        };
    }

}
