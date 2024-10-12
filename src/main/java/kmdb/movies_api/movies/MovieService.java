package kmdb.movies_api.movies;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import kmdb.movies_api.exception.ResourceAlreadyExistsException;
import kmdb.movies_api.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public List<Movie> getMovies(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<Movie> moviesPage = movieRepository.findAll(pageable);
        List<Movie> moviesList = moviesPage.getContent();
        if (moviesList.isEmpty()) {
            throw new ResourceNotFoundException("No actors found on page " + page + " with size " + size);
        }
        return moviesList;
    }

    public Movie getMovieById(Long movieId) {
        if (movieId < 1) {
            throw new IllegalArgumentException("Movie ID must be greater than 0");
        }
        return movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie with ID " + movieId + " does not exist"));
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

    public void addMovie(Movie movie) {
        Optional<Movie> movieOptional = movieRepository
                .findByTitle(movie.getTitle());
        if(movieOptional.isPresent()) {
            throw new ResourceAlreadyExistsException("Movie already exists in database");
        }
        movieRepository.save(movie);
    }

    public void deleteMovie(Long movieId) {
        if (movieId < 1) {
            throw new IllegalArgumentException("Movie ID must be greater than 0");
        }
        movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie with ID " + movieId + " does not exist"));
        movieRepository.deleteById(movieId);
    }

    @Transactional
    public void updateMovie(Long movieId, String title, @Valid @RequestBody int releaseYear, @Valid @RequestBody int duration) {
        if (movieId < 1) {
            throw new IllegalArgumentException("Movie ID must be greater than 0");
        }
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Movie with ID " + movieId + " does not exist in database"
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
