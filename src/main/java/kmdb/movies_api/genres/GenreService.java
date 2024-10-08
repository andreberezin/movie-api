package kmdb.movies_api.genres;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreService {

    private final GenreRepository genreRepository;

    @Autowired
    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }

    public void addGenre(Genre genre) {
        genreRepository.save(genre);
    }

    public void deleteGenre(Long genreId) {
        genreRepository.deleteById(genreId);
    }

/*    @Transactional
    public void updateGenre(Genre genre) {
    }*/
}
