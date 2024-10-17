package kmdb.movies_api.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import kmdb.movies_api.entities.Actor;
import kmdb.movies_api.entities.Movie;
import kmdb.movies_api.services.MovieService;
import kmdb.movies_api.services.ActorService;
import kmdb.movies_api.entities.Genre;
import kmdb.movies_api.services.GenreService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@Validated
@AllArgsConstructor
@RequestMapping(path = "api/movies")
public class MovieController {

    private final MovieService movieService;
    private final ActorService actorService;
    private final GenreService genreService;

    // get all movies
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Optional<List<Movie>> getAllMovies() {
        return movieService.getAllMovies();
    }

    // get number of movies
    @GetMapping(params = "count")
    @ResponseStatus(HttpStatus.OK)
    public String getMovieCount() {
        return movieService.getMovieCount();
    }

    // get movies by page and page size
    @GetMapping(params = { "page", "size"})
    @ResponseStatus(HttpStatus.OK)
    public Optional<List<Movie>> getMoviesByPage(
            @Min(value = 0, message = "Page index must not be less than zero")
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,

            @Min(value = 0, message = "Page size must not be less than one")
            @Max(value = 100, message = "Page size limit is 100")
            @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
        return movieService.getMoviesByPage(page, size);
    }

    // get movie by release year
    @GetMapping(params = "releaseYear")
    @ResponseStatus(HttpStatus.OK)
    public Optional<List<Movie>> getMoviesByReleaseYear(
            @RequestParam(value = "releaseYear", defaultValue = "", required = false)
            int releaseYear) {
        return movieService.getMoviesByReleaseYear(releaseYear);
    }

    // get data one by one using id as parameter
    @GetMapping(path = "{movieId}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Movie> getMovieById(
            @PathVariable @Positive(message = "Movie ID must be greater than 0") Long movieId) {
        return movieService.getMovieById(movieId);
    }

    // get movies by name or get all if a parameter isn't given
    @GetMapping(path = "/search")
    @ResponseStatus(HttpStatus.OK)
    public Optional<List<Movie>> findMoviesByTitle(@RequestParam(required = false) String title) {
        return movieService.findMoviesByTitle(title);
    }

    // get movies by genre
    @GetMapping(params = "genre")
    @ResponseStatus(HttpStatus.OK)
    public Optional<List<Movie>> getMoviesByGenre(
            @RequestParam(value = "genre", defaultValue = "", required = false)
            @Positive(message = "Genre ID must be greater than 0") Long genreId) {
        return genreService.getMoviesByGenre(genreId);
    }

    // get movies by actor
    // /api/movies?actor={Actor.id}
    @GetMapping(params = "actor")
    @ResponseStatus(HttpStatus.OK)
    public Optional<List<Movie>> getMoviesByActor(
            @RequestParam(value = "actor", defaultValue = "", required = false)
            @Positive(message = "Actor ID must be greater than 0") Long actorId)
    {
        return actorService.getMoviesByActor(actorId);
    }

    // get actors in a movie
    @GetMapping("/{movieId}/actors")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Set<Actor>> getActorsInMovie(
            @PathVariable @Positive(message = "Movie ID must be greater than 0") Long movieId) {
        return movieService.getActorsInMovie(movieId);
    }

    // get genres associated to a movie
    @GetMapping("/{movieId}/genres")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Set<Genre>> getGenresInMovie(
            @PathVariable @Positive(message = "Movie ID must be greater than 0") Long movieId) {
        return movieService.getGenresInMovie(movieId);
    }

    // add movie
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> addMovie(@Valid @RequestBody Movie movie) {
        return movieService.addMovie(movie);
    }

    // delete movie
    @DeleteMapping(path = "{movieId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMovie(
            @PathVariable("movieId") @Positive(message = "Movie ID must be greater than 0") Long movieId,
            @RequestParam(value = "force", defaultValue = "false", required = false) boolean force) {
        movieService.deleteMovie(movieId, force);
    }

    // update movie
    @PatchMapping(path = "{movieId}") // modify data by id
    @ResponseStatus(HttpStatus.OK)
    public void updateMovie(
            @PathVariable("movieId") @Positive(message = "Movie ID must be greater than 0") Long movieId,
            @Valid @RequestBody Movie movie) {
        movieService.updateMovie(movieId, movie.getTitle(), movie.getReleaseYear(), movie.getDuration(),
                movie.getGenres(), movie.getActors());
    }

    // assign genres to movies
    @PutMapping("/{movieId}/genres/{genreId}")
    @ResponseStatus(HttpStatus.OK)
    public Movie assignGenresToMovies(
            @PathVariable @Positive(message = "Movie ID must be greater than 0") Long movieId,
            @PathVariable @Positive(message = "Genre ID must be greater than 0") Long genreId) {
        return movieService.assignGenresToMovies(movieId, genreId);
    }

    // remove assigned genres from movies
    @DeleteMapping("/{movieId}/genres/{genreId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeGenreFromMovie(
            @PathVariable @Positive(message = "Movie ID must be greater than 0")Long movieId,
            @PathVariable @Positive(message = "Genre ID must be greater than 0") Long genreId) {
        movieService.removeGenreFromMovie(movieId, genreId);
    }

    // assign actors to movies
    @PutMapping("/{movieId}/actors/{actorsId}")
    @ResponseStatus(HttpStatus.OK)
    public Movie assignActorToMovie(
            @PathVariable @Positive(message = "Movie ID must be greater than 0") Long movieId,
            @PathVariable @Positive(message = "Actor ID must be greater than 0") Long actorsId) {
        return movieService.assignActorToMovie(movieId, actorsId);
    }

    // remove assigned actors from movies
    @DeleteMapping("/{movieId}/actors/{actorsId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeActorFromMovie(
            @PathVariable @Positive(message = "Movie ID must be greater than 0") Long movieId,
            @PathVariable @Positive(message = "Actor ID must be greater than 0") Long actorsId) {
        movieService.removeActorFromMovie(movieId, actorsId);
    }
}
