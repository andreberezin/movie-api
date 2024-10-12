package kmdb.movies_api.movies;

import jakarta.validation.Valid;
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
    public List<Movie> getGenres(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
        return movieService.getMovies(page, size);
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
            @PathVariable("movieId") Long movieId) {
        movieService.deleteMovie(movieId);
    }

    @PatchMapping(path = "{movieId}") // modify data by id
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateMovie(
            @PathVariable("movieId") Long movieId,
            @RequestBody Movie request) {
        movieService.updateMovie(movieId, request.getTitle(), request.getReleaseYear(), request.getDuration());
    }
}
