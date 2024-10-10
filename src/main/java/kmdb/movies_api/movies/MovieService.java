package kmdb.movies_api.movies;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import kmdb.movies_api.exception.ResourceAlreadyExistsException;
import kmdb.movies_api.exception.ResourceNotFoundException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }
/*    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }*/

    // TODO if id is not specified. Currently returns 404 with default body
    public Movie getMovieById(Long movieId) {
        return movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie with id " + movieId + " does not exist"));
        }


    public static Specification<Movie> titleContains(String title) {
        return (root, query, criteriaBuilder) -> {
            if (title == null || title.isEmpty()) {
                return criteriaBuilder.conjunction(); // Return true if no title is specified
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%");
        };
    }

    // filter movies by title
    public List<Movie> findMoviesByTitle(String title) {
        if (title == null || title.isEmpty()) {
            return movieRepository.findAll(); // Return all movies if no title is specified
        }
        Specification<Movie> spec = titleContains(title);

        if (movieRepository.findAll(spec).isEmpty()) { // in case no movie matches given title
            throw new ResourceNotFoundException("Movie with title containing " + title + " does not exist");
        }

        return movieRepository.findAll(spec);
    }

    public void addMovie(@Valid @RequestBody Movie movie) {
        Optional<Movie> movieOptional = movieRepository
                .findByTitle(movie.getTitle());
        if(movieOptional.isPresent()) {
            throw new ResourceAlreadyExistsException("Movie already exists in database");
        }
        movieRepository.save(movie);
    }

    public void deleteMovie(Long movieId) {
        boolean exists = movieRepository.existsById(movieId);
        if (!exists) {
            throw new ResourceNotFoundException(
                    "Movie with id " + movieId + " does not exist in database");
        }
        movieRepository.deleteById(movieId);
    }

    @Transactional
    public void updateMovie(@Valid @RequestBody Long movieId, String title, int releaseYear, int duration) {
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Movie with id " + movieId + " does not exist in database"
                ));

        if (title != null && !title.isEmpty()) { // update only non-null fields
            movie.setTitle(title);
        }

        if (releaseYear > 0) { // update only non-null fields
            movie.setReleaseYear(releaseYear);
        }

        if (duration > 0) { // update only non-null fields
            movie.setDuration(duration);
        }
    }
}
