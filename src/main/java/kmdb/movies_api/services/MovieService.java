package kmdb.movies_api.services;

import jakarta.transaction.Transactional;
import kmdb.movies_api.entities.Actor;
import kmdb.movies_api.entities.Movie;
import kmdb.movies_api.repositories.ActorRepository;
import kmdb.movies_api.exceptions.ResourceAlreadyExistsException;
import kmdb.movies_api.exceptions.ResourceNotFoundException;
import kmdb.movies_api.entities.Genre;
import kmdb.movies_api.repositories.GenreRepository;
import kmdb.movies_api.repositories.MovieRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final ActorRepository actorRepository;

    // get all movies
    public Optional<List<Movie>> getAllMovies() {
        List<Movie> moviesList = movieRepository.findAll();
        if (moviesList.isEmpty()) {
            throw new ResourceNotFoundException("No movies found in the database");
        }
        return Optional.of(moviesList);
    }

    // get number of movies
    public String getMovieCount() {
        return "Movies in database: " + movieRepository.count();
    }

    // get movies by page and page size
    public Optional<List<Movie>> getMoviesByPage(int page, int size) {
        if (size > 100) {
            throw new IllegalArgumentException("Page size must be less than or equal to 100");
        }
        PageRequest pageable = PageRequest.of(page, size);
        Page<Movie> moviesPage = movieRepository.findAll(pageable);
        List<Movie> moviesList = moviesPage.getContent();

        // method #1
        return Optional.ofNullable(Optional.of(moviesList)
                .filter(list -> !list.isEmpty())
                .orElseThrow(() -> new ResourceNotFoundException("No movies found on page " + page)));
    }

    // get movies by release year
    public Optional<List<Movie>> getMoviesByReleaseYear(int releaseYear) {
        if (releaseYear < 0 || releaseYear > 2300) {
            throw new IllegalArgumentException("Release year must be between 0 and 2300");
        }

        List<Movie> moviesList = movieRepository.findAll();

        return Optional.ofNullable(Optional.of(moviesList.stream()
                        .filter(movie -> movie.getReleaseYear() == releaseYear)
                        .collect(Collectors.toList()))
                        .filter(list -> !list.isEmpty())
                .orElseThrow(() -> new ResourceNotFoundException("No movies found with release year " + releaseYear)));

    }

    // get movies by id
    public Optional<Movie> getMovieById(Long movieId) {
/*        if (movieId < 1) {
            throw new IllegalArgumentException("Movie ID must be greater than 0");
        }*/

        Optional<Movie> movie= movieRepository.findById(movieId);
        if (movie.isPresent()) {
            return movie;
        } else {
            throw new ResourceNotFoundException("Movie with ID " + movieId + " does not exist");
        }
    }

    // check if there is a match in database for given movie title
    public static Specification<Movie> titleContains(String title) {
        return (root, query, criteriaBuilder) -> {
            if (title == null || title.isEmpty()) {
                return criteriaBuilder.conjunction(); // Return true if no title is specified
            } else {
                return criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%");
            }
        };
    }

    // filter movies by title
    public Optional<List<Movie>> findMoviesByTitle(String title) {
        Specification<Movie> spec = titleContains(title);
        List<Movie> moviesList = movieRepository.findAll(spec);

        return Optional.ofNullable(Optional.of(moviesList)
                .filter(list -> !list.isEmpty())
                .orElseThrow(() -> new ResourceNotFoundException("Movie with title containing '" + title + "' does not exist")));

    }

    // finds actors associated to movie
    public Optional<Set<Actor>> getActorsInMovie(Long movieId) {
        if (movieId < 1) {
            throw new IllegalArgumentException("Movie ID must be greater than 0");
        }

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie with ID " + movieId + " does not exist"));

        Set<Actor> actorsInMovie = movie.getActors();
        if (actorsInMovie.isEmpty()) {
            throw new ResourceNotFoundException("No actors associated with movie '" + movie.getTitle() + "'");
        }

        return Optional.of(actorsInMovie);
    }

    // finds genres associated to movie
    public Optional<Set<Genre>> getGenresInMovie(Long movieId) {
        if (movieId < 1) {
            throw new IllegalArgumentException("Movie ID must be greater than 0");
        }

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie with ID " + movieId + " does not exist"));

        Set<Genre> genresInMovie = movie.getGenres();
        if (genresInMovie.isEmpty()) {
            throw new ResourceNotFoundException("No Genres associated with movie '" + movie.getTitle() + "'");
        } else {
            return Optional.of(genresInMovie);
        }
    }

    // add movie
    public ResponseEntity<String> addMovie(Movie movie) {
        Optional<Movie> movieOptional = movieRepository
                .findByTitle(movie.getTitle());

        if(movieOptional.isPresent()) {
            throw new ResourceAlreadyExistsException("Movie '" + movie.getTitle() + "' already exists");
            }

        Set<Genre> genres = movie.getGenres().stream()
                .map(genre -> genreRepository.findByName(genre.getName())
                        .orElseThrow(() -> new ResourceNotFoundException("Genre '" + genre.getName() + "' not found")))
                .collect(Collectors.toSet());

        Set<Actor> actors = movie.getActors().stream()
                .map(actor -> actorRepository.findByName(actor.getName())
                        .orElseThrow(() -> new ResourceNotFoundException("Actor '" + actor.getName() + "' not found")))
                .collect(Collectors.toSet());

        movie.setGenres(genres);
        movie.setActors(actors);

        movieRepository.save(movie);
        return new ResponseEntity<>("Movie '" + movie.getTitle() + "' added successfully", HttpStatus.CREATED);
    }


    public void deleteMovie(Long movieId, boolean force) {
        if (movieId < 1) {
            throw new IllegalArgumentException("Movie ID must be greater than 0");
        }
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie with ID " + movieId + " does not exist"));

        if (force) { // if force is true then delete resource regardless of relationships
            movieRepository.deleteById(movieId);
            return;
        }

        int numOfActors = movie.getActors().size();
        int numOfGenres = movie.getGenres().size();

        if (numOfActors > 0 && numOfGenres > 0) { // if force is false and relationships exist then return exception
            throw new IllegalStateException(("Cannot delete movie '" + movie.getTitle() + "' because they are associated with " + numOfGenres + " genre(s) and " + +  numOfActors + " actor(s)"));
        }
        if (numOfActors > 0 && numOfGenres == 0) { // if force is false and relationships exist then return exception
            throw new IllegalStateException(("Cannot delete movie '" + movie.getTitle() + "' because they are associated with " + numOfActors + " actor(s)"));
        }
        if (numOfActors == 0 && numOfGenres > 0) { // if force is false and relationships exist then return exception
            throw new IllegalStateException(("Cannot delete movie '" + movie.getTitle() + "' because they are associated with " + numOfGenres + " genre(s)"));
        }

        movieRepository.deleteById(movieId); // if force is false and relationships do not exist then delete resource

    }


    // update movie
    @Transactional
    public void updateMovie(Long movieId, String title, int releaseYear, int duration, Set<Genre> genres, Set<Actor> actors ) {
        if (movieId < 1) {
            throw new IllegalArgumentException("Movie ID must be greater than 0");
        }

        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie with ID " + movieId + " does not exist in database"));

        if (title != null && !title.isEmpty()) { // update only non-null fields
            movie.setTitle(title);
        }

        if (releaseYear > 0) { // update only non-null fields
            movie.setReleaseYear(releaseYear);
        }

        if (duration > 0) { // update only non-null fields
            movie.setDuration(duration);
        }

        genres = genres.stream()
                .map(genre -> genreRepository.findByName(genre.getName())
                        .orElseThrow(() -> new ResourceNotFoundException("Genre '" + genre.getName() + "' not found")))
                .collect(Collectors.toSet());

        actors = actors.stream()
                .map(actor -> actorRepository.findByName(actor.getName())
                        .orElseThrow(() -> new ResourceNotFoundException("Actor '" + actor.getName() + "' not found")))
                .collect(Collectors.toSet());

        if (!genres.isEmpty()) {
            movie.getGenres().retainAll(genres); // remove genres not in the new list
            movie.getGenres().addAll(genres); // add genres in the new list
        } else {
            movie.setGenres(genres); // assign genres directly
        }

        if (!actors.isEmpty()) {
            movie.getActors().retainAll(actors); // remove actors not in the new list
            movie.getActors().addAll(actors); // add actors in the new list
        } else {
            movie.setActors(actors); // assign actors directly
        }
    }


    // assign genres to movies
    public Movie assignGenresToMovies(Long movieId, Long genreId) {
        Movie movie = movieRepository.findById(movieId) // check movie with that id exists in database
                .orElseThrow(() -> new ResourceNotFoundException("Movie with ID " + movieId + " does not exist in database"));
        Genre genre = genreRepository.findById(genreId) // check genre with that id exists in database
                .orElseThrow(() -> new ResourceNotFoundException("Genre with ID " + genreId + " does not exist in database"));

        if (movie.getGenres().contains(genre)) { // check movie has a relationship with genre
            throw new ResourceAlreadyExistsException("Movie '" + movie.getTitle() + "' is already associated with genre '" + genre.getName() + "'");
        } else {
            movie.setGenre(genre);
            return movieRepository.save(movie);
        }
    }

    // remove genres from movies
    public void removeGenreFromMovie(Long movieId, Long genreId) {
        Movie movie = movieRepository.findById(movieId) // check movie with that id exists in database
                .orElseThrow(() -> new ResourceNotFoundException("Movie with ID " + movieId + " does not exist in database"));
        Genre genre = genreRepository.findById(genreId) // check genre with that id exists in database
                .orElseThrow(() -> new ResourceNotFoundException("Genre with ID " + genreId + " does not exist in database"));

        if (!movie.getGenres().contains(genre)) { // check movie has a relationship with genre
            throw new ResourceAlreadyExistsException("Movie '" + movie.getTitle() + "' is not associated with genre '" + genre.getName() + "'");
        } else {
            movie.removeGenre(genre);
            movieRepository.save(movie);
        }
    }

    // assign actors to movies
    public Movie assignActorToMovie(Long movieId, Long actorId) {
        Movie movie = movieRepository.findById(movieId) // check movie with that id exists in database
                .orElseThrow(() -> new ResourceNotFoundException("Movie with ID " + movieId + " does not exist in database"));
        Actor actor = actorRepository.findById(actorId) // check actor with that id exists in database
                .orElseThrow(() -> new ResourceNotFoundException("Actor with ID " + actorId + " does not exist in database"));

        if (movie.getActors().contains(actor)) { // check movie has a relationship with actor
            throw new ResourceAlreadyExistsException("Movie '" + movie.getTitle() + "' is already associated with actor '" + actor.getName() + "'");
        } else {
            movie.setActor(actor);
            return movieRepository.save(movie);
        }
    }

    // remove actors from movies
    public void removeActorFromMovie(Long movieId, Long actorId) {
        Movie movie = movieRepository.findById(movieId) // check movie with that id exists in database
                .orElseThrow(() -> new ResourceNotFoundException("Movie with ID " + movieId + " does not exist in database"));
        Actor actor = actorRepository.findById(actorId) // check actor with that id exists in database
                .orElseThrow(() -> new ResourceNotFoundException("Actor with ID " + actorId + " does not exist in database"));

        if (!movie.getActors().contains(actor)) { // check movie has a relationship with genre
            throw new ResourceAlreadyExistsException("Movie '" + movie.getTitle() + "' is not associated with actor '" + actor.getName() + "'");
        } else {
            movie.removeActor(actor);
            movieRepository.save(movie);
        }
    }
}
