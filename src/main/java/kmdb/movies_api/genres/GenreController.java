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

    @GetMapping(params = { "page", "size"}) // retrieve by page
    @ResponseStatus(HttpStatus.OK)
    public List<Genre> getGenres(
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,
            @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
        return genreService.getGenres(page, size);
    }

    @GetMapping(path = "{genreId}") // retrieve data one by one using id as parameter
    @ResponseStatus(HttpStatus.OK)
    public Genre getGenreById(
            @PathVariable Long genreId) {
        return genreService.getGenreById(genreId);
    }

    @GetMapping//retrieve data by name or retrieve all if a parameter isn't given
    @ResponseStatus(HttpStatus.OK)
    public List<Genre> findGenresByName(@RequestParam(required = false) String name) {
        return genreService.findGenresByName(name);
    }

    @PostMapping// add data
    @ResponseStatus(HttpStatus.CREATED)
    public void addGenre(@Valid @RequestBody Genre genre) {
        genreService.addGenre(genre);
    }

    @DeleteMapping(path = "{genreId}") // delete data by id
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGenre(
            @PathVariable("genreId") Long genreId) {
        genreService.deleteGenre(genreId);
    }

    @PatchMapping(path = "{genreId}") // modify data by id
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateGenre(
            @PathVariable("genreId") Long genreId,
            @Valid @RequestBody Genre request) {
        genreService.updateGenre(genreId, request.getName());
        //genreService.updateGenre(genreId, request.getName());
    }

}
