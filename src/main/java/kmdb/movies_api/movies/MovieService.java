package kmdb.movies_api.movies;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import kmdb.movies_api.actors.Actor;
import kmdb.movies_api.actors.ActorRepository;
import kmdb.movies_api.exception.ResourceAlreadyExistsException;
import kmdb.movies_api.exception.ResourceNotFoundException;
import kmdb.movies_api.genres.Genre;
import kmdb.movies_api.genres.GenreRepository;
import kmdb.movies_api.genres.GenreService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final ActorRepository actorRepository;
    private final GenreService genreService;

    public MovieService(MovieRepository movieRepository, GenreRepository genreRepository, ActorRepository actorRepository, GenreService genreService) {
        this.movieRepository = movieRepository;
        this.genreRepository = genreRepository;
        this.actorRepository = actorRepository;
        this.genreService = genreService;
    }

    // get number of movies
    public String getMovieCount() {
        return "Movies in database: " + movieRepository.count();
    }

    // get movies by page and page size
    public Optional<List<Movie>> getMoviesByPage(int page, int size) {
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
        if (movieId < 1) {
            throw new IllegalArgumentException("Movie ID must be greater than 0");
        }

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

    // add movie (cannot add genres and actors)
/*    public ResponseEntity<String> addMovie(Movie movie) {
        Optional<Movie> movieOptional = movieRepository
                .findByTitle(movie.getTitle());
        if(movieOptional.isPresent()) {
            throw new ResourceAlreadyExistsException("Movie '" + movie.getTitle() + "' already exists in database");
        } else {
            movieRepository.save(movie);
            return new ResponseEntity<>("Movie '" + movie.getTitle() + "' added successfully", HttpStatus.CREATED);
        }
    }*/

        // add movies with genre and actor ids
/*    public void addMovie(String title, int releaseYear, int duration, List<Long> genreIds, List<Long> actorIds) {
        Movie movie = new Movie(title, releaseYear, duration);

        Set<Genre> genres = genreIds.stream()
                .map(genreId -> genreRepository.findById(genreId)
                        .orElseThrow(() -> new ResourceNotFoundException("Genre with ID " + genreId + " not found")))
                .collect(Collectors.toSet());

        Set<Actor> actors = actorIds.stream()
                .map(actorId -> actorRepository.findById(actorId)
                        .orElseThrow(() -> new ResourceNotFoundException("Actor with ID " + actorId + " not found")))
                .collect(Collectors.toSet());

        movie.setGenres(genres);
        movie.setActors(actors);

        movieRepository.save(movie);
    }*/

/*    public void addMovie(String title, int releaseYear, int duration, List<String> genreNames, List<String> actorNames) {
        Movie movie = new Movie(title, releaseYear, duration);

        Set<Genre> genres = genreNames.stream()
                .map(genreName -> genreRepository.findByName(genreName)
                        .orElseThrow(() -> new ResourceNotFoundException("Genre '" + genreName + "' not found")))
                .collect(Collectors.toSet());

        Set<Actor> actors = actorNames.stream()
                .map(actorName -> actorRepository.findByName(actorName)
                        .orElseThrow(() -> new ResourceNotFoundException("Actor '" + actorName + "' not found")))
                .collect(Collectors.toSet());

        movie.setGenres(genres);
        movie.setActors(actors);

        movieRepository.save(movie);
    }*/

    public void addMovie(String title, int releaseYear, int duration, Set<Genre> genres, Set<Actor> actors) {
        Movie movie = new Movie(title, releaseYear, duration);

        genres = genres.stream()
                .map(genre -> genreRepository.findByName(genre.getName())
                        .orElseThrow(() -> new ResourceNotFoundException("Genre '" + genre.getName() + "' not found")))
                .collect(Collectors.toSet());

        actors = actors.stream()
                .map(actor -> actorRepository.findByName(actor.getName())
                        .orElseThrow(() -> new ResourceNotFoundException("Actor '" + actor.getName() + "' not found")))
                .collect(Collectors.toSet());

        movie.setGenres(genres);
        movie.setActors(actors);

        movieRepository.save(movie);
    }

    // delete movie
    public void deleteMovie(Long movieId) {
        if (movieId < 1) {
            throw new IllegalArgumentException("Movie ID must be greater than 0");
        }
        movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("Movie with ID " + movieId + " does not exist"));

        movieRepository.deleteById(movieId);
    }

    // update movie
    @Transactional
    public void updateMovie(Long movieId, String title, int releaseYear, int duration, Set<Genre> genres, Set<Actor> actors ) {
        if (movieId < 1) {
            throw new IllegalArgumentException("Movie ID must be greater than 0");
        }
        Movie movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Movie with ID " + movieId + " does not exist in database"
                ));

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
        }

        if (!actors.isEmpty()) {
            movie.getActors().retainAll(actors); // remove actors not in the new list
            movie.getActors().addAll(actors); // add actors in the new list
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
