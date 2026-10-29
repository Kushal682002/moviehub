# MovieHub — Movie Management REST API

MovieHub is a backend REST API built with Java and Spring Boot for managing movies, user accounts, reviews, and personal watchlists. It includes JWT-based authentication, role-based authorization, input validation, centralized exception handling, and automated tests.

## Features

- **Movie management:** Create, retrieve, update, and delete movies.
- **User management:** Register users and manage user information.
- **JWT authentication:** Authenticate users using email and password.
- **Role-based authorization:** Restrict operations according to user roles.
- **Reviews:** Create, retrieve, update, and delete movie reviews.
- **Watchlists:** Add movies to and remove movies from personal watchlists.
- **Search and filtering:** Search movies by title and filter by genre or minimum rating.
- **Pagination:** Retrieve movie records in pages.
- **Input validation:** Validate incoming request data.
- **Exception handling:** Return structured error responses.
- **API documentation:** Explore endpoints using Swagger UI.
- **Automated testing:** Test application behavior using JUnit and Mockito.
- **Docker support:** Build and run the application in a container.
- **Continuous integration:** Run automated Maven tests through GitHub Actions.

## Technology stack

| Technology | Purpose |
|---|---|
| Java 21 | Programming language |
| Spring Boot | Backend application framework |
| Spring Web MVC | REST API development |
| Spring Data JPA | Database access |
| MySQL 8 | Relational database |
| Spring Security | Authentication and authorization |
| JWT | Token-based authentication |
| BCrypt | Password hashing |
| Bean Validation | Request validation |
| JUnit and Mockito | Automated testing |
| Maven | Build and dependency management |
| Springdoc OpenAPI | API documentation |
| Docker | Application containerization |
| GitHub Actions | Continuous integration |

## Project structure

```text
moviehub/
├── .github/
│   └── workflows/
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

Before running MovieHub, install or configure:

- JDK 21
- MySQL 8
- Git
- Docker Desktop (for containerized execution)

An IDE such as Eclipse or IntelliJ IDEA is optional. The project includes the Maven Wrapper, so a separate Maven installation is normally unnecessary.

## Getting started

### 1. Clone the repository

```bash
git clone https://github.com/Kushal682002/moviehub.git
cd moviehub
```

### 2. Configure MySQL

Start your MySQL server and create the database:

```sql
CREATE DATABASE moviehub;
```

Configure the database connection using environment variables. The application reads its configuration from `src/main/resources/application.properties`.

Example properties:

```properties
spring.datasource.url=${SPRING_DATASOURCE_URL:jdbc:mysql://localhost:3306/moviehub}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME:root}
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=update

jwt.secret=${JWT_SECRET}
jwt.expiration=86400000
```

Use the properties above as a reference and retain any additional settings already required by your application.

**Security:** Never commit actual database passwords, production JWT secrets, or access tokens to GitHub.

### 3. Generate a Base64-encoded JWT secret

The current JWT implementation expects the configured secret to be Base64-encoded. Generate a random value instead of using the example CI secret.

In PowerShell:

```powershell
$env:JWT_SECRET = [Convert]::ToBase64String(
    [System.Security.Cryptography.RandomNumberGenerator]::GetBytes(32)
)
```

Keep this value private. Use a securely managed secret in production.

### 4. Run tests

On Windows, from the project root:

```powershell
.\mvnw.cmd test
```

On Linux or macOS:

```bash
./mvnw test
```

### 5. Run the application locally

In PowerShell, set your database credentials:

```powershell
$env:DB_PASSWORD = "your_mysql_password"
$env:SPRING_DATASOURCE_URL = "jdbc:mysql://localhost:3306/moviehub"
$env:SPRING_DATASOURCE_USERNAME = "root"
```

Generate or set `JWT_SECRET` as described above, then run:

```powershell
.\mvnw.cmd spring-boot:run
```

The API should be available at:

`http://localhost:8080`

The environment variables set in PowerShell apply only to that terminal session.

## Docker

The repository includes a multi-stage Dockerfile that builds the Spring Boot application using Java 21 and runs it using a Java 21 runtime.

### 1. Build the Docker image

From the project root:

```powershell
docker build -t moviehub:1.0 .
```

### 2. Start the application container

This example assumes MySQL is already running on your Windows host, the `moviehub` database exists, and the MySQL account has the required permissions.

Set the variables in PowerShell:

```powershell
$env:DB_PASSWORD = "your_mysql_password"
$env:SPRING_DATASOURCE_URL = "jdbc:mysql://host.docker.internal:3306/moviehub"
$env:SPRING_DATASOURCE_USERNAME = "root"

$env:JWT_SECRET = [Convert]::ToBase64String(
    [System.Security.Cryptography.RandomNumberGenerator]::GetBytes(32)
)
```

Start the container:

```powershell
docker run -d `
  --name moviehub-app `
  -p 8080:8080 `
  -e DB_PASSWORD="$env:DB_PASSWORD" `
  -e SPRING_DATASOURCE_URL="$env:SPRING_DATASOURCE_URL" `
  -e SPRING_DATASOURCE_USERNAME="$env:SPRING_DATASOURCE_USERNAME" `
  -e JWT_SECRET="$env:JWT_SECRET" `
  moviehub:1.0
```

The `host.docker.internal` hostname allows the application container to connect to MySQL running on the Windows host. Ensure your MySQL server accepts the connection.

### 3. Check the container

View its status:

```powershell
docker ps
```

View application logs:

```powershell
docker logs -f moviehub-app
```

Stop the container:

```powershell
docker stop moviehub-app
```

Remove the stopped container:

```powershell
docker rm moviehub-app
```

After startup, open `http://localhost:8080` to access the API. The application container and MySQL database are separate; the database must be available for the application to operate correctly.

## Swagger API documentation

After starting the application, open:

- **Swagger UI:** http://localhost:8080/swagger-ui/index.html
- **OpenAPI specification:** http://localhost:8080/v3/api-docs

Swagger UI lets you inspect endpoints, submit request bodies, and execute API requests.

For protected endpoints, log in to obtain a JWT token and use the Swagger **Authorize** dialog to supply the bearer token.

## API endpoints

The following table summarizes the implemented endpoint groups. Use Swagger UI to verify exact request schemas and response formats.

### Authentication and users

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/auth/login` | Authenticate a user |
| POST | `/api/users` | Register a user |
| GET | `/api/users` | Retrieve users (admin access) |
| GET | `/api/users/{id}` | Retrieve a user (admin access) |
| PUT | `/api/users/{id}` | Update a user (admin access) |
| DELETE | `/api/users/{id}` | Delete a user (admin access) |

### Movies

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/movies` | Create a movie (admin access) |
| GET | `/api/movies` | Retrieve movies |
| GET | `/api/movies/{id}` | Retrieve a movie by ID |
| GET | `/api/movies/page?page=0&size=10` | Retrieve a page of movies |
| GET | `/api/movies/search?title=inception` | Search by title |
| GET | `/api/movies/genre?genre=Sci-Fi` | Filter by genre |
| GET | `/api/movies/rating?rating=8` | Filter by minimum rating |
| PUT | `/api/movies/{id}` | Update a movie (admin access) |
| DELETE | `/api/movies/{id}` | Delete a movie (admin access) |

### Reviews

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/movies/{movieId}/reviews` | Add a review |
| GET | `/api/movies/{movieId}/reviews` | Retrieve reviews for a movie |
| GET | `/api/reviews/{id}` | Retrieve a review |
| PUT | `/api/reviews/{id}` | Update a review |
| DELETE | `/api/reviews/{id}` | Delete a review |

### Watchlists

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/users/{userId}/watchlist/{movieId}` | Add a movie to a watchlist |
| GET | `/api/users/{userId}/watchlist` | Retrieve a watchlist |
| DELETE | `/api/users/{userId}/watchlist/{movieId}` | Remove a movie from a watchlist |

Protected operations require authentication and the appropriate authorization. Review and watchlist ownership rules are enforced by the application.

## Automated testing and CI

Run the test suite locally:

```powershell
.\mvnw.cmd test
```

The project includes service-layer and controller tests for authentication, movies, users, reviews, and watchlists.

GitHub Actions runs the Maven test suite when changes are pushed to the configured branches or pull requests are opened. The CI workflow provisions a MySQL service for testing.

Check the **Actions** tab in the repository to view the latest workflow status and test results.

## Future improvements

Potential future enhancements include:

- Run the application and MySQL together using Docker Compose.
- Add more integration tests with a dedicated test database.
- Introduce database migrations using Flyway or Liquibase.
- Add monitoring, structured logging, and production deployment configuration.

## Author

**Kushal Agrawal**

- GitHub: https://github.com/Kushal682002
- Repository: https://github.com/Kushal682002/moviehub
