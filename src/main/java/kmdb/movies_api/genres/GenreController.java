package kmdb.movies_api.genres;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/genres")
public class GenreController {

    private final GenreService genreService;

    @Autowired
    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }

    @GetMapping
    public List<Genre> getAllGenres() {
        return genreService.getAllGenres();
    }

    @PostMapping
    public void addGenre(@RequestBody Genre genre) {
        genreService.addGenre(genre);
    }

    @DeleteMapping(path = "{genreId}")
    public void deleteGenre(
            @PathVariable("genreId") Long genreId) {
        genreService.deleteGenre(genreId);
    }

/*    @PatchMapping(path = "{genreId}")
    public void updateGenre(
            @PathVariable("GenreId") Long genreId,
            @RequestBody Genre request) {
        genreService.updateGenre(genreId, request.getName());
        genreService.updateGenre(genreId, request.getName());
    }*/

}
