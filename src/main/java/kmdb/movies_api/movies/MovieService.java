package kmdb.movies_api.movies;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import kmdb.movies_api.actors.Actor;
import kmdb.movies_api.actors.ActorRepository;
import kmdb.movies_api.exception.ResourceAlreadyExistsException;
import kmdb.movies_api.exception.ResourceNotFoundException;
import kmdb.movies_api.genres.Genre;
import kmdb.movies_api.genres.GenreRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final ActorRepository actorRepository;

    public MovieService(MovieRepository movieRepository, GenreRepository genreRepository, ActorRepository actorRepository) {
        this.movieRepository = movieRepository;
        this.genreRepository = genreRepository;
        this.actorRepository = actorRepository;
    }

    // get movies by page and page size
    public List<Movie> getMoviesByPage(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<Movie> moviesPage = movieRepository.findAll(pageable);
        List<Movie> moviesList = moviesPage.getContent();
        if (moviesList.isEmpty()) {
            throw new ResourceNotFoundException("No movies found on page " + page);
        }
        return moviesList;
    }

    // get movies by release year
    public List<Movie> getMoviesByReleaseYear(int releaseYear) {
        List<Movie> moviesList = movieRepository.findAll();

        if (releaseYear < 0 || releaseYear > 2300) {
            throw new IllegalArgumentException("Release year must be between 0 and 2300");
        }

        moviesList = moviesList.stream()
                .filter(movie -> movie.getReleaseYear() == releaseYear)
                .collect(Collectors.toList());

        if (moviesList.isEmpty()) {
            throw new ResourceNotFoundException("No movies found with release year " + releaseYear);
        }
        return moviesList;
    }

    // get movies by id
    public Movie getMovieById(Long movieId) {
        if (movieId < 1) {
            throw new IllegalArgumentException("Movie ID must be greater than 0");
        }
        return movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie with ID " + movieId + " does not exist"));
        }

    // check if there is a match in database for given movie title
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
        Specification<Movie> spec = titleContains(title);

        if (movieRepository.findAll(spec).isEmpty()) { // in case no movie matches given title
            throw new ResourceNotFoundException("Movie with title containing '" + title + "' does not exist");
        }

        return movieRepository.findAll(spec);
    }

    // filter movies by genre
    public List<Movie> getMoviesByGenre(Long genreId) {
        if (genreId < 1) {
            throw new IllegalArgumentException("Genre ID must be greater than 0");
        }

        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new ResourceNotFoundException("Genre with ID " + genreId + " does not exist"));

        List<Movie> moviesList = new ArrayList<>(movieRepository.findAllByGenresContains(genre));

        if (moviesList.isEmpty()) {
            throw new ResourceNotFoundException("No movies found in genre '" + genre.getName() + "'");
        }
        return moviesList;
    }

    // filter movies by actor
    public List<Movie> getMoviesByActor(Long actorId) {
        if (actorId < 1) {
            throw new IllegalArgumentException("Genre ID must be greater than 0");
        }

        Actor actor = actorRepository.findById(actorId)
                .orElseThrow(() -> new ResourceNotFoundException("Actor with ID " + actorId + " does not exist"));

        List<Movie> moviesList = new ArrayList<>(movieRepository.findAllByActorsContains(actor));

        if (moviesList.isEmpty()) {
            throw new ResourceNotFoundException("No movies found starring actor '" + actor.getName() + "'");
        }
        return moviesList;
    }

    // finds actors associated to movie
    public Set<Actor> getActorsInMovie(Long movieId) {
        if (movieId < 1) {
            throw new IllegalArgumentException("Movie ID must be greater than 0");
        }

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie with ID " + movieId + " does not exist"));

        if (movie.getActors().isEmpty()) {
            throw new ResourceNotFoundException("No actors associated with movie '" + movie.getTitle() + "'");
        }

        return movie.getActors();
    }

    // finds genres associated to movie
    public Set<Genre> getGenresInMovie(Long movieId) {
        if (movieId < 1) {
            throw new IllegalArgumentException("Movie ID must be greater than 0");
        }

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie with ID " + movieId + " does not exist"));

        if (movie.getGenres().isEmpty()) {
            throw new ResourceNotFoundException("No Genres associated with movie '" + movie.getTitle() + "'");
        }

        return movie.getGenres();
    }

    // add movie
    public void addMovie(Movie movie) {
        Optional<Movie> movieOptional = movieRepository
                .findByTitle(movie.getTitle());
        if(movieOptional.isPresent()) {
            throw new ResourceAlreadyExistsException("Movie '" + movie.getTitle() + "' already exists in database");
        }
        movieRepository.save(movie);
    }

    // delete movie
    public void deleteMovie(Long movieId) {
        if (movieId < 1) {
            throw new IllegalArgumentException("Movie ID must be greater than 0");
        }
        movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie with ID " + movieId + " does not exist"));

        movieRepository.deleteById(movieId);
    }
    // update movie
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

    // assign genres to movies
    public Movie assignGenresToMovies(Long movieId, Long genreId) {
        Movie movie = movieRepository.findById(movieId) // check movie with that id exists in database
                .orElseThrow(() -> new ResourceNotFoundException("Movie with ID " + movieId + " does not exist in database"));
        Genre genre = genreRepository.findById(genreId) // check genre with that id exists in database
                .orElseThrow(() -> new ResourceNotFoundException("Genre with ID " + genreId + " does not exist in database"));

        if (movie.getGenres().contains(genre)) { // check movie has a relationship with genre
            throw new ResourceAlreadyExistsException("Movie '" + movie.getTitle() + "' is already associated with genre '" + genre.getName() + "'");
        }
        movie.setGenre(genre);
        return movieRepository.save(movie);
    }

    // remove genres from movies
    public void removeGenreFromMovie(Long movieId, Long genreId) {
        Movie movie = movieRepository.findById(movieId) // check movie with that id exists in database
                .orElseThrow(() -> new ResourceNotFoundException("Movie with ID " + movieId + " does not exist in database"));
        Genre genre = genreRepository.findById(genreId) // check genre with that id exists in database
                .orElseThrow(() -> new ResourceNotFoundException("Genre with ID " + genreId + " does not exist in database"));

        if (!movie.getGenres().contains(genre)) { // check movie has a relationship with genre
            throw new ResourceAlreadyExistsException("Movie '" + movie.getTitle() + "' is not associated with genre '" + genre.getName() + "'");
        }
        movie.removeGenre(genre);
        movieRepository.save(movie);
    }

    // assign actors to movies
    public Movie assignActorToMovie(Long movieId, Long actorId) {
        Movie movie = movieRepository.findById(movieId) // check movie with that id exists in database
                .orElseThrow(() -> new ResourceNotFoundException("Movie with ID " + movieId + " does not exist in database"));
        Actor actor = actorRepository.findById(actorId) // check actor with that id exists in database
                .orElseThrow(() -> new ResourceNotFoundException("Actor with ID " + actorId + " does not exist in database"));

        if (movie.getActors().contains(actor)) { // check movie has a relationship with actor
            throw new ResourceAlreadyExistsException("Movie '" + movie.getTitle() + "' is already associated with actor '" + actor.getName() + "'");
        }
        movie.setActor(actor);
        return movieRepository.save(movie);
    }

    // remove actors from movies
    public void removeActorFromMovie(Long movieId, Long actorId) {
        Movie movie = movieRepository.findById(movieId) // check movie with that id exists in database
                .orElseThrow(() -> new ResourceNotFoundException("Movie with ID " + movieId + " does not exist in database"));
        Actor actor = actorRepository.findById(actorId) // check actor with that id exists in database
                .orElseThrow(() -> new ResourceNotFoundException("Actor with ID " + actorId + " does not exist in database"));

        if (!movie.getActors().contains(actor)) { // check movie has a relationship with genre
            throw new ResourceAlreadyExistsException("Movie '" + movie.getTitle() + "' is not associated with actor '" + actor.getName() + "'");
        }
        movie.removeActor(actor);
        movieRepository.save(movie);
    }
}
