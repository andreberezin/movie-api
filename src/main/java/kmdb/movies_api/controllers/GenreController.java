package kmdb.movies_api.controllers;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
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

@RestController
@Validated
@AllArgsConstructor
@RequestMapping(path = "api/genres")
public class GenreController {

    private final GenreService genreService;

    // get all genres
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Optional<List<Genre>> getAllGenres() {
        return genreService.getAllGenres();
    }

    // get number of genres
    @GetMapping(params = "count")
    @ResponseStatus(HttpStatus.OK)
    public String getGenreCount() {
        return genreService.getGenreCount();
    }

    // retrieve data by page and page size
    @GetMapping(params = { "page", "size"})
    @ResponseStatus(HttpStatus.OK)
    public Optional<List<Genre>> getGenresByPage(
            @Min(value = 0, message = "Page index must not be less than zero")
            @RequestParam(value = "page", defaultValue = "0", required = false) int page,

            @Min(value = 0, message = "Page size must not be less than one")
            @Max(value = 100, message = "Page size limit is 100")
            @RequestParam(value = "size", defaultValue = "10", required = false) int size) {
        return genreService.getGenresByPage(page, size);
    }

    // retrieve data one by one using id as parameter
    @GetMapping(path = "{genreId}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<Genre> getGenreById(
            @PathVariable @Positive(message = "Genre ID must be greater than 0") Long genreId) {
        return genreService.getGenreById(genreId);
    }

    //retrieve data by name or retrieve all if a parameter isn't given
    @GetMapping(path = "/search")
    @ResponseStatus(HttpStatus.OK)
    public Optional<List<Genre>> findGenresByName(@RequestParam(required = false) String name) {
        return genreService.findGenresByName(name);
    }

    // add data
    @PostMapping
    public ResponseEntity<String> addGenre(@Valid @RequestBody Genre genre) {
        return genreService.addGenre(genre);
    }

    // delete data by id
    @DeleteMapping(path = "{genreId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteGenre(
            @PathVariable("genreId") @Positive(message = "Genre ID must be greater than 0") Long genreId,
            @RequestParam(value = "force", defaultValue = "false", required = false) boolean force) {
        genreService.deleteGenre(genreId, force);
    }

    // modify data by id
    @PatchMapping(path = "{genreId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateGenre(
            @PathVariable("genreId") @Positive(message = "Genre ID must be greater than 0") Long genreId,
            @Valid @RequestBody Genre request) {
        genreService.updateGenre(genreId, request.getName());
    }
}
