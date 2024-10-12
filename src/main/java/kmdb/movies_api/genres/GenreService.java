package kmdb.movies_api.genres;

import jakarta.transaction.Transactional;
import kmdb.movies_api.exception.ResourceAlreadyExistsException;
import kmdb.movies_api.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GenreService {

    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {this.genreRepository = genreRepository;}

    public List<Genre> getGenres(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<Genre> genresPage = genreRepository.findAll(pageable);
        List<Genre> genresList = genresPage.getContent();
        return genresList;
    }

    public Genre getGenreById(Long genreId) {
        if (genreId < 1) {
            throw new IllegalArgumentException("Genre ID must be greater than 0");
        }
        return genreRepository.findById(genreId)
                .orElseThrow(() -> new ResourceNotFoundException("Genre with ID " + genreId + " does not exist"));
    }

    public static Specification<Genre> nameContains(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isEmpty()) {
                return criteriaBuilder.conjunction(); // Return true if no name is specified
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    // filter genres by name
    public List<Genre> findGenresByName(String name) {
        if (name == null || name.isEmpty()) {
            return genreRepository.findAll(); // Return all genres if no name is specified
        }
        Specification<Genre> spec = nameContains(name);

        if (genreRepository.findAll(spec).isEmpty()) { // in case no movie matches given title
            throw new ResourceNotFoundException("Genre containing " + name + " does not exist");
        }

        return genreRepository.findAll(spec);
    }


    public void addGenre(Genre genre) {
        Optional<Genre> genreOptional = genreRepository
                .findByName(genre.getName());
        if(genreOptional.isPresent()) {
            throw new ResourceAlreadyExistsException("Genre already exists in database");
        }
        genreRepository.save(genre);
    }

    public void deleteGenre(Long genreId) {
        if (genreId < 1) {
            throw new IllegalArgumentException("Genre ID must be greater than 0");
        }
        boolean exists = genreRepository.existsById(genreId);
        if (!exists) {
            throw new ResourceNotFoundException(
                    "Genre with ID " + genreId + " does not exist in database");
        }
        genreRepository.deleteById(genreId);
    }

    @Transactional
    public void updateGenre(Long genreId, String name) {
        if (genreId < 1) {
            throw new IllegalArgumentException("Genre ID must be greater than 0");
        }
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Genre with ID " + genreId + " does not exist in database"
                ));
        genre.setName(name);
    }
}
