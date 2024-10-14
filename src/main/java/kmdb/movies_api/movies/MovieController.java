package kmdb.movies_api.movies;

import jakarta.validation.Valid;
import kmdb.movies_api.actors.Actor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "api/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    // get movies by page and page size
    @GetMapping(params = { "page", "size"})
    @ResponseStatus(HttpStatus.OK)
    public List<Movie> getMoviesByPage(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
        return movieService.getMoviesByPage(page, size);
    }

    // get movie by release year
    @GetMapping(params = "releaseYear")
    @ResponseStatus(HttpStatus.OK)
    public List<Movie> getMoviesByReleaseYear(
            @RequestParam(value = "releaseYear", defaultValue = "", required = false)
            int releaseYear) {
        return movieService.getMoviesByReleaseYear(releaseYear);
    }

    // get data one by one using id as parameter
    @GetMapping(path = "{movieId}")
    @ResponseStatus(HttpStatus.OK)
    public Movie getMovieById(
            @PathVariable Long movieId) {
        return movieService.getMovieById(movieId);
    }

    // get movies by name or get all if a parameter isn't given
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Movie> findMoviesByTitle(@RequestParam(required = false) String title) {
        return movieService.findMoviesByTitle(title);
    }

    // get movies by genre
    @GetMapping(params = "genre")
    @ResponseStatus(HttpStatus.OK)
    public List<Movie> getMoviesByGenre(
            @RequestParam(value = "genre", defaultValue = "", required = false) Long genreId) {
        return movieService.getMoviesByGenre(genreId);
    }

    // get movies by actor
    @GetMapping(params = "actor")
    @ResponseStatus(HttpStatus.OK)
    public List<Movie> getMoviesByActor(
            @RequestParam(value = "actor", defaultValue = "", required = false) Long actorId) {
        return movieService.getMoviesByActor(actorId);
    }

    // get actors in a movie
    @GetMapping("/{movieId}/actors")
    @ResponseStatus(HttpStatus.OK)
    public Set<Actor> getActorsInMovie(
            @PathVariable Long movieId) {
        return movieService.getActorsInMovie(movieId);
    }

    // add movie
    @PostMapping // add data
    @ResponseStatus(HttpStatus.CREATED)
    public void addMovie(@Valid @RequestBody Movie movie) {
        movieService.addMovie(movie);
    }

    // delete movie
    @DeleteMapping(path = "{movieId}") // delete data by id
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMovie(
            @PathVariable("movieId") Long movieId) {
        movieService.deleteMovie(movieId);
    }

    // update movie
    @PatchMapping(path = "{movieId}") // modify data by id
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateMovie(
            @PathVariable("movieId") Long movieId,
            @RequestBody Movie request) {
        movieService.updateMovie(movieId, request.getTitle(), request.getReleaseYear(), request.getDuration());
    }

    // assign genres to movies
    @PutMapping("/{movieId}/genres/{genreId}")
    Movie assignGenresToMovies(
            @PathVariable Long movieId,
            @PathVariable Long genreId) {
        return movieService.assignGenresToMovies(movieId, genreId);
    }

    // remove assigned genres from movies
    @DeleteMapping("/{movieId}/genres/{genreId}")
    public void removeGenreFromMovie(
            @PathVariable Long movieId,
            @PathVariable Long genreId) {
        movieService.removeGenreFromMovie(movieId, genreId);
    }

    // assign actors to movies
    @PutMapping("/{movieId}/actors/{actorsId}")
    Movie assignActorToMovie(
            @PathVariable Long movieId,
            @PathVariable Long actorsId) {
        return movieService.assignActorToMovie(movieId, actorsId);
    }

    // remove assigned actors from movies
    @DeleteMapping("/{movieId}/actors/{actorsId}")
    public void removeActorFromMovie(
            @PathVariable Long movieId,
            @PathVariable Long actorsId) {
        movieService.removeActorFromMovie(movieId, actorsId);
    }
}
