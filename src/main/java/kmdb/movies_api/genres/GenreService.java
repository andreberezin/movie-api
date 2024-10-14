package kmdb.movies_api.genres;

import jakarta.transaction.Transactional;
import kmdb.movies_api.exception.ResourceAlreadyExistsException;
import kmdb.movies_api.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GenreService {

    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    // get genres by page and page size
    public List<Genre> getGenresByPage(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<Genre> genresPage = genreRepository.findAll(pageable);
        List<Genre> genresList = genresPage.getContent();
        if (genresList.isEmpty()) {
            throw new ResourceNotFoundException("No genres found on page " + page);
        }
        return genresList;
    }

    // get genre by id
    public Genre getGenreById(Long genreId) {
        if (genreId < 1) {
            throw new IllegalArgumentException("Genre ID must be greater than 0");
        }
        return genreRepository.findById(genreId)
                .orElseThrow(() -> new ResourceNotFoundException("Genre with ID " + genreId + " does not exist"));
    }

    // check if there are any genres that match the given name
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
        Specification<Genre> spec = nameContains(name);

        if (genreRepository.findAll().isEmpty()) { // in case no movies are found
            throw new ResourceNotFoundException("No genres found");
        }

        if (genreRepository.findAll(spec).isEmpty()) { // in case no movie matches given title
            throw new ResourceNotFoundException("Genre containing '" + name + "' does not exist");
        }

        return genreRepository.findAll(spec);
    }

    // add a genre
    public ResponseEntity<String> addGenre(Genre genre) {
        Optional<Genre> genreOptional = genreRepository
                .findByName(genre.getName());
        if(genreOptional.isPresent()) {
            throw new ResourceAlreadyExistsException("Genre '" + genre.getName() + "' already exists in database");
        }
        genreRepository.save(genre);
        return new ResponseEntity<>("Genre added successfully", HttpStatus.CREATED);
    }

    // delete a genre
    public void deleteGenre(Long genreId, boolean force) {
        if (genreId < 1) {
            throw new IllegalArgumentException("Genre ID must be greater than 0");
        }
        Genre genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new ResourceNotFoundException("Genre with ID " + genreId + " does not exist"));

        int NumOfMovies = genre.getMovies().size();


        if (force) { // if force is true then remove all relationships and delete resource
            genreRepository.deleteById(genreId);
        }

            if (NumOfMovies > 0) { // if force is false and relationships exist then return exception
                if (NumOfMovies == 1) {
                    throw new IllegalStateException("Cannot delete genre '" + genre.getName() + "' because it is associated with " + NumOfMovies + " movie");
                } else {
                    throw new IllegalStateException("Cannot delete genre '" + genre.getName() + "' because it is associated with " + NumOfMovies + " movies");
                }
            }
            genreRepository.deleteById(genreId); // if force is false and relationships do not exist then delete resource
    }

    // update genre
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
