package kmdb.movies_api.genres;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/genres")
public class GenreController {

    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    // retrieve data by page and page size
    @GetMapping(params = { "page", "size"})
    @ResponseStatus(HttpStatus.OK)
    public List<Genre> getGenresByPage(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
        return genreService.getGenresByPage(page, size);
    }

    // retrieve data one by one using id as parameter
    @GetMapping(path = "{genreId}")
    @ResponseStatus(HttpStatus.OK)
    public Genre getGenreById(
            @PathVariable Long genreId) {
        return genreService.getGenreById(genreId);
    }

    //retrieve data by name or retrieve all if a parameter isn't given
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Genre> findGenresByName(@RequestParam(required = false) String name) {
        return genreService.findGenresByName(name);
    }

    // add data
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addGenre(@Valid @RequestBody Genre genre) {
        genreService.addGenre(genre);
    }

    // delete data by id
    @DeleteMapping(path = "{genreId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGenre(
            @PathVariable("genreId") Long genreId,
            @RequestParam(value = "force", defaultValue = "false", required = false) boolean force) {
        genreService.deleteGenre(genreId, force);
    }

    // modify data by id
    @PatchMapping(path = "{genreId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateGenre(
            @PathVariable("genreId") Long genreId,
            @Valid @RequestBody Genre request) {
        genreService.updateGenre(genreId, request.getName());
    }
}
