package kmdb.movies_api.movies;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import kmdb.movies_api.genres.Genre;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping(params = { "page", "size"}) // retrieve by page
    @ResponseStatus(HttpStatus.OK)
    public List<Movie> getMoviesByPage(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
        return movieService.getMoviesByPage(page, size);
    }

    @GetMapping(params = "releaseYear")
    @ResponseStatus(HttpStatus.OK)
    public List<Movie> getMoviesByReleaseYear(
            @RequestParam(value = "releaseYear", defaultValue = "", required = false)
            int releaseYear) {
        return movieService.getMoviesByReleaseYear(releaseYear);
    }

    @GetMapping(path = "{movieId}") // retrieve data one by one using id as parameter
    @ResponseStatus(HttpStatus.OK)
    public Movie getMovieById(
            @PathVariable Long movieId) {
        return movieService.getMovieById(movieId);
    }

    @GetMapping //retrieve data by name or retrieve all if a parameter isn't given
    @ResponseStatus(HttpStatus.OK)
    public List<Movie> findMoviesByTitle(@RequestParam(required = false) String title) {
        return movieService.findMoviesByTitle(title);
    }

    @PostMapping // add data
    @ResponseStatus(HttpStatus.CREATED)
    public void addMovie(@Valid @RequestBody Movie movie) {
        movieService.addMovie(movie);
    }

    @DeleteMapping(path = "{movieId}") // delete data by id
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMovie(
            @PathVariable("movieId") Long movieId,
            @RequestParam(value = "force", defaultValue = "false", required = false) boolean force) {
        movieService.deleteMovie(movieId, force);
    }

    @PatchMapping(path = "{movieId}") // modify data by id
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateMovie(
            @PathVariable("movieId") Long movieId,
            @RequestBody Movie request) {
        movieService.updateMovie(movieId, request.getTitle(), request.getReleaseYear(), request.getDuration());
    }

    @PutMapping("/{movieId}/genres/{genreId}")
    Movie assignGenresToMovies(
            @PathVariable Long movieId,
            @PathVariable Long genreId) {
        return movieService.assignGenresToMovies(movieId, genreId);
    }

    @DeleteMapping("/{movieId}/genres/{genreId}")
    public void removeGenreFromMovie(
            @PathVariable Long movieId,
            @PathVariable Long genreId) {
        movieService.removeGenreFromMovie(movieId, genreId);
    }

    @PutMapping("/{movieId}/actors/{actorsId}")
    Movie assignActorToMovie(
            @PathVariable Long movieId,
            @PathVariable Long actorsId) {
        return movieService.assignActorToMovie(movieId, actorsId);
    }

    @DeleteMapping("/{movieId}/actors/{actorsId}")
    public void removeActorFromMovie(
            @PathVariable Long movieId,
            @PathVariable Long actorsId) {
        movieService.removeActorFromMovie(movieId, actorsId);
    }
}
