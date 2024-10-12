package kmdb.movies_api.movies;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class MovieConfig {

    @Bean
    CommandLineRunner CommandLineRunnerMovie(MovieRepository movieRepository) {
        return args -> {
            Movie movie1 = new Movie(
                    "The Godfather",
                    1972,
                    175
            );
            Movie movie2 = new Movie(
                    "The Shawnshank redemption",
                    1994,
                    142
            );

            movieRepository.saveAll(
                    List.of(movie1, movie2)
            );
        };
    }
}
