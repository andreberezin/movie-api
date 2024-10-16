# Movie Database API

## 1. Project Overview

This project is a RESTful API for managing a movie database. It allows users to perform CRUD operations on movies, actors, and genres. The API is built using Spring Boot and uses a SQLite database for storing the data.

## 2. Setup and Installation Instructions

1. Clone the repository: `git clone https://gitea.kood.tech/andreberezin/kmdb.git`
2. Navigate to the project directory
3. Run the application

## 3. Usage Guide

To test the API, you can use the provided Postman workspace:
https://www.postman.com/andreberezin/kmdb/collection/6vs7gbp/movie-database-api?action=share&creator=38674842

The workspace contains collections for testing different endpoints, such as creating, reading, updating, and deleting movies, actors, and genres.

## 4. Any Additional Features or Bonus Functionality Implemented
- Functionality for getting the number of movies, actors and genres in database
- Search functionality for finding movies by partial title, actors by partial name and genres by partial name.
- Pagination functionality for GET requests to retrieve movies, genres and actors by page number and page size.