package kmdb.movies_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication

public class MoviesApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoviesApiApplication.class, args);
	}

}

// x TODO Student can describe the structure of a REST API URL, including base URL, resource, and query parameters.
// x TODO Student can explain what dependency injection is and how it's used in this project.
// x TODO Student can explain what a JpaRepository is and at least three methods it provides out of the box.
// TODO Student can explain the purpose of the @SpringBootApplication annotation
// TODO Student can explain the purpose of the @Entity annotation.
// TODO Student can describe the difference between eager and lazy loading in JPA and which is the default for @ManyToMany relationships.
// TODO Student can explain the benefits of using collections for organized API testing.
// TODO Student can explain what a 404 HTTP status code means and when it should be used in this project.
// TODO Student can explain the purpose of HTTP status codes in API responses.
// TODO Student can explain the purpose of the application.properties file in a Spring Boot project.
// TODO Ask the student to explain the contents of application.properties file for SQLite configuration
// TODO SQLite JDBC driver dependency with the correct version is added in pom.xml.

// TODO A new movie with associated genre and actors can be created in a single POST request.