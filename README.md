# MovieHub — Movie Management REST API

MovieHub is a backend REST API built using Java and Spring Boot for managing movies, user accounts, reviews, and personal watchlists. It includes JWT-based authentication, role-based authorization, input validation, and centralized exception handling.

## Features

- **Movie Management:** Create, retrieve, update, and delete movies.
- **User Management:** Register users and manage user information.
- **Authentication:** Login using email and password with JWT token generation.
- **Role-Based Authorization:** Restrict operations based on user roles.
- **Reviews:** Add, retrieve, update, and delete movie reviews.
- **Watchlists:** Add movies to and remove movies from a user's watchlist.
- **Search and Filtering:** Search movies by title and filter by genre or minimum rating.
- **Pagination:** Retrieve movies in pages.
- **Validation:** Validate incoming request data.
- **Exception Handling:** Return structured error responses.
- **API Documentation:** Explore endpoints through Swagger UI.
- **Unit and Controller Tests:** Test service and controller behavior.
- **Docker Support:** Includes a Dockerfile for containerizing the application.

## Technology Stack

| Technology | Purpose |
|---|---|
| Java 21 | Programming language target |
| Spring Boot | Application framework |
| Spring Web MVC | REST API development |
| Spring Data JPA | Database access |
| MySQL 8 | Relational database |
| Spring Security | Authentication and authorization |
| JSON Web Token (JWT) | Token-based authentication |
| BCrypt | Password hashing |
| Bean Validation | Request validation |
| JUnit and Mockito | Automated testing |
| Maven | Dependency management and build |
| Springdoc OpenAPI | API documentation |
| Docker | Application containerization |

## Project Structure

```text
moviehub/
├── .mvn/
├── src/
│   ├── main/
│   │   ├── java/com/kushal/moviehub/
│   │   │   ├── config/
│   │   │   ├── controller/
│   │   │   ├── dto/
│   │   │   ├── entity/
│   │   │   ├── exception/
│   │   │   ├── repository/
│   │   │   ├── security/
│   │   │   ├── service/
│   │   │   └── service/impl/
│   │   └── resources/
│   │       └── application.properties
│   └── test/
│       └── java/com/kushal/moviehub/
├── Dockerfile
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
```

## Prerequisites

Install the following before running MovieHub:

- JDK 21 or a compatible Java runtime for your configured build
- MySQL 8
- Git
- An IDE such as Eclipse or IntelliJ IDEA (optional)

The project uses Maven Wrapper, so a separate Maven installation is not normally required.

## Database Setup

1. Start your MySQL server.
2. Create the database:

```sql
CREATE DATABASE moviehub;
```

3. Configure the database credentials using environment variables.

The application reads its configuration from `src/main/resources/application.properties`.

Example configuration:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/moviehub
spring.datasource.username=root
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=update

jwt.secret=${JWT_SECRET}
jwt.expiration=86400000
```

Set `DB_PASSWORD` to your MySQL password and `JWT_SECRET` to a strong, securely generated JWT signing secret.

**Security note:** Never commit real database passwords, JWT secrets, access tokens, or other credentials to GitHub.

## Run the Application

### Windows

Open Command Prompt in the project root directory and configure the environment variables for the current terminal session:

```bat
set DB_PASSWORD=your_mysql_password
set JWT_SECRET=your_generated_jwt_secret
```

Replace the example values with your actual local values. Do not share or commit them.

Run the tests:

```bat
mvnw.cmd test
```

Start the application:

```bat
mvnw.cmd spring-boot:run
```

The API will be available at:

```text
http://localhost:8080
```

The environment variables above are temporary and apply only to that Command Prompt session.

## Swagger API Documentation

After starting the application, open:

[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

OpenAPI specification:

[http://localhost:8080/v3/api-docs](http://localhost:8080/v3/api-docs)

Swagger UI allows you to inspect endpoints, provide request bodies, and execute API requests.

For protected endpoints, log in to obtain a JWT token. Use the Swagger **Authorize** dialog to supply the token as a bearer token.

## API Endpoints

The following table summarizes the implemented endpoint groups. Consult Swagger UI for the exact request schemas and response formats.

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/auth/login` | Authenticate a user |
| POST | `/api/users` | Register a user |
| GET | `/api/users` | Retrieve users (admin access) |
| GET | `/api/users/{id}` | Retrieve a user (admin access) |
| PUT | `/api/users/{id}` | Update a user (admin access) |
| DELETE | `/api/users/{id}` | Delete a user (admin access) |
| POST | `/api/movies` | Create a movie (admin access) |
| GET | `/api/movies` | Retrieve movies |
| GET | `/api/movies/{id}` | Retrieve a movie by ID |
| GET | `/api/movies/page?page=0&size=10` | Retrieve a page of movies |
| GET | `/api/movies/search?title=inception` | Search by title |
| GET | `/api/movies/genre?genre=Sci-Fi` | Filter by genre |
| GET | `/api/movies/rating?rating=8` | Filter by minimum rating |
| PUT | `/api/movies/{id}` | Update a movie (admin access) |
| DELETE | `/api/movies/{id}` | Delete a movie (admin access) |
| POST | `/api/movies/{movieId}/reviews` | Add a review |
| GET | `/api/movies/{movieId}/reviews` | Retrieve reviews for a movie |
| GET | `/api/reviews/{id}` | Retrieve a review |
| PUT | `/api/reviews/{id}` | Update a review |
| DELETE | `/api/reviews/{id}` | Delete a review |
| POST | `/api/users/{userId}/watchlist/{movieId}` | Add a movie to a watchlist |
| GET | `/api/users/{userId}/watchlist` | Retrieve a watchlist |
| DELETE | `/api/users/{userId}/watchlist/{movieId}` | Remove a movie from a watchlist |

Protected operations require authentication and appropriate authorization. Review and watchlist ownership rules are enforced by the service layer.

## Automated Testing

Run the complete test suite with:

```bat
mvnw.cmd test
```

The project includes service-layer and controller tests for authentication, movies, users, reviews, and watchlists.

The test suite previously completed successfully with **63 tests, zero failures, and zero errors** in the development environment. Run the command again to verify the current state.

## Docker

A Dockerfile is included in the repository.

Build the image from the project root:

```bat
docker build -t moviehub:1.0 .
```

Docker setup and execution may require additional configuration for the MySQL database and environment variables. The Docker image build should be verified before treating containerized execution as complete.

## Future Improvements

- Add continuous integration using GitHub Actions.
- Run the application and MySQL using Docker Compose.
- Add integration tests with a dedicated test database.
- Improve production database migration management.
- Add monitoring, structured logging, and deployment configuration.

## Author

**Kushal Agrawal**

GitHub: [Kushal682002](https://github.com/Kushal682002)

Repository: [MovieHub](https://github.com/Kushal682002/moviehub)
