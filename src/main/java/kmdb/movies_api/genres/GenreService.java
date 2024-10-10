package kmdb.movies_api.genres;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GenreService {

    private final GenreRepository genreRepository;

    @Autowired
    public GenreService(GenreRepository genreRepository) {this.genreRepository = genreRepository;}

/*    public List<Genre> getAllGenres() {
        return genreRepository.findAll();
    }*/

    public Optional<Genre> getGenreById(Long genreId) {

        boolean exists = genreRepository.existsById(genreId);
        if (!exists) {
            throw new IllegalStateException(
                    "Genre with id " + genreId + " does not exist in database"
            );
        }
        return genreRepository.findById(genreId);
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
        return genreRepository.findAll(spec);
    }


    public void addGenre(Genre genre) {
        Optional<Genre> genreOptional = genreRepository
                .findByName(genre.getName());
        if(genreOptional.isPresent()) {
            throw new IllegalStateException("Genre already exists in database");
        }
        genreRepository.save(genre);
    }

    public void deleteGenre(Long genreId) {
        boolean exists = genreRepository.existsById(genreId);
        if (!exists) {
            throw new IllegalArgumentException(
                    "Genre with id " + genreId + " does not exist in database");
        }
        genreRepository.deleteById(genreId);
    }

    @Transactional
    public void updateGenre(Long genreId, String name) {
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new IllegalStateException(
                        "Genre with id " + genreId + " does not exist in database"
                ));

        if (name != null && !name.isEmpty()) { // update only non-null fields
            genre.setName(name);
        }
    }
}
