# Movie Database API

## 1. Project Overview

This project is a RESTful API for managing a movie database. It allows users to perform CRUD operations on movies, actors, and genres. The API is built using Spring Boot and uses a SQLite database for storing the data.

## 2. Setup and Installation Instructions
1. Clone the repository: `git clone https://gitea.kood.tech/andreberezin/kmdb.git`
2. Navigate to the project directory using `cd` 
3. Build the application with all dependencies: `mvn package`
   * In case "command not found: mvn" \
       install Maven: https://maven.apache.org/install.html \
       or on MacOs/Linux use brew for installation: `brew install maven`
4. Ensure you have Java 21 installed. You can check the version with: `java -version`
   * * If you don't have Java 21 installed, download and install it from: https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html
5. Run the application: `java -jar target/movies_api-0.0.1.jar`
6. End the application: `pkill -f movies_api-0.0.1.jar`

## 3. Usage Guide
* To test the API, you can use the provided Postman workspace: \
    https://www.postman.com/andreberezin/kmdb/collection/6vs7gbp/movie-database-api?action=share&creator=38674842 \
    The workspace contains collections for testing different endpoints, such as creating, reading, updating, and deleting movies, actors, and genres.
  &nbsp;


 * Also, you can use the added springdoc-openapi dependency by going to: \
    `http://localhost:8080/swagger-ui/index.html#/` \
    This will allow you to see which paths and parameters are expected.

## 4. Any Additional Features or Bonus Functionality Implemented
- Functionality for getting the number of movies, actors and genres in database
- Case-insensitive search functionality for finding movies by partial title, actors by partial name and genres by partial name.
- Pagination functionality for GET requests to retrieve movies, genres and actors by page number and page size.
- Added springdoc-openapi dependency so Swagger UI can be used to see expected paths and parameters for the API.