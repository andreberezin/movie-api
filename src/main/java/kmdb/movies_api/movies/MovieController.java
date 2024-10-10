package kmdb.movies_api.movies;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

/*    @GetMapping
    public List<Movie> getMovies() {
        return movieService.getAllMovies();
    }*/

    @GetMapping(path = "{movieId}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Movie> getMovieById(
            @PathVariable Long movieId) {
        return movieService.getMovieById(movieId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Movie> findMoviesByTitle(@RequestParam(required = false) String title) {
        return movieService.findMoviesByTitle(title);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addMovie(@RequestBody Movie movie) {
        movieService.addMovie(movie);
    }

    @DeleteMapping(path = "{movieId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMovie(
            @PathVariable("movieId") Long movieId) {
        movieService.deleteMovie(movieId);
    }

    @PatchMapping(path = "{movieId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateMovie(
            @PathVariable("movieId") Long movieId,
            @RequestBody Movie request) {
        movieService.updateMovie(movieId, request.getTitle(), request.getReleaseYear(), request.getDuration());
    }
}
