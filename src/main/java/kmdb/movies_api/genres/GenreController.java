package kmdb.movies_api.genres;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "api/genres")
public class GenreController {

    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

/*    @GetMapping
    public List<Genre> getAllGenres() {
        return genreService.getAllGenres();
    }*/

    @GetMapping(path = "{genreId}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Genre> getGenreById(
            @PathVariable Long genreId) {
        return genreService.getGenreById(genreId);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Genre> findGenresByName(@RequestParam(required = false) String name) {
        return genreService.findGenresByName(name);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addGenre(@RequestBody Genre genre) {
        genreService.addGenre(genre);
    }

    @DeleteMapping(path = "{genreId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGenre(
            @PathVariable("genreId") Long genreId) {
        genreService.deleteGenre(genreId);
    }

    @PatchMapping(path = "{genreId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateGenre(
            @PathVariable("genreId") Long genreId,
            @RequestBody Genre request) {
        genreService.updateGenre(genreId, request.getName());
        //genreService.updateGenre(genreId, request.getName());
    }

}
