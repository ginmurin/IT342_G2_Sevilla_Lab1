# Backend Setup Documentation - User Authentication System

## Overview
This is a Spring Boot backend application with H2 database for user authentication and authorization.

## Architecture & Design Patterns

The backend follows the **MVC architecture** pattern as outlined in the provided diagrams:

### Entity-Relationship Diagram (ERD)
- **User Table**: Stores user information with columns matching the database schema
- Primary Key: `user_id` (auto-increment)
- Fields: username, email, password_hash, first_name, last_name, role (enum), is_active, timestamps

### Class Diagram Components
1. **User Model**: JPA entity representing users
2. **UserRepository**: Spring Data JPA repository for database operations
3. **UserService**: Business logic layer handling registration, login, authentication
4. **Controllers**:
   - `LoginController`: Handles login requests
   - `RegistrationController`: Manages user registration
   - `DashboardController`: Protected endpoint for authenticated users
   - `ProfileController`: User profile management (view/update)
   - `ProtectedPagesController`: Access control verification
   - `LogoutController`: Session invalidation

### Sequence Diagrams Implementation
- **Registration Flow**: Validates data → Checks email/username uniqueness → Creates user → Saves to database
- **Login Flow**: Validates credentials → Generates session token → Updates last_login → Returns response
- **Logout Flow**: Clears session token → Redirects to login page

## Technology Stack

- **Framework**: Spring Boot 3.5.10
- **Java Version**: Java 17
- **Database**: H2 (in-memory)
- **ORM**: Spring Data JPA + Hibernate
- **Security**: Spring Security + BCrypt password encoding
- **Build Tool**: Maven
- **Validation**: Jakarta Validation (Bean Validation)
- **Additional**: Lombok for reducing boilerplate

## Project Structure

```
backend/UserAuth/
├── src/main/java/com/sevilla/userauth/
│   ├── config/
│   │   └── SecurityConfig.java         # PasswordEncoder bean configuration
│   ├── controller/
│   │   ├── LoginController.java        # POST /auth/login
│   │   ├── RegistrationController.java # POST /auth/register
│   │   ├── DashboardController.java    # GET /dashboard
│   │   ├── ProfileController.java      # GET/PUT /user/profile/{userId}
│   │   ├── ProtectedPagesController.java # GET /protected/pages
│   │   └── LogoutController.java       # POST /auth/logout
│   ├── dto/
│   │   ├── LoginRequest.java
│   │   ├── LoginResponse.java
│   │   ├── RegisterRequest.java
│   │   └── UserProfileDTO.java
│   ├── model/
│   │   └── User.java                   # User entity with UserRole enum
│   ├── repository/
│   │   └── UserRepository.java         # JPA repository
│   ├── service/
│   │   └── UserService.java            # Business logic & session management
│   └── UserauthApplication.java        # Main Spring Boot class
├── src/main/resources/
│   └── application.properties          # Configuration
└── pom.xml                             # Maven dependencies
```

## Configuration

### H2 Database
The application uses an **in-memory H2 database** configured in `application.properties`:

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
```

**Access H2 Console**: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (leave blank)

### Hibernate DDL
Database tables are automatically created and dropped on each startup:
```properties
spring.jpa.hibernate.ddl-auto=create-drop
```

Change to `update` for production to preserve data across restarts.

## API Endpoints

### Authentication Endpoints

#### 1. Register User
```
POST /api/auth/register
Content-Type: application/json

{
  "username": "johndoe",
  "email": "john@example.com",
  "password": "securePassword123",
  "firstName": "John",
  "lastName": "Doe"
}

Response (201 Created):
{
  "success": true,
  "message": "Registration successful",
  "username": "johndoe",
  "email": "john@example.com"
}
```

#### 2. Login User
```
POST /api/auth/login
Content-Type: application/json

{
  "username": "johndoe",
  "password": "securePassword123"
}

Response (200 OK):
{
  "isAuthenticated": true,
  "sessionToken": "uuid-string",
  "username": "johndoe",
  "email": "john@example.com",
  "firstName": "John",
  "lastName": "Doe"
}
```

#### 3. Logout User
```
POST /api/auth/logout
Authorization: Bearer <sessionToken>

Response (200 OK):
{
  "success": true,
  "message": "Logout successful"
}
```

### Protected Endpoints

#### 4. Get Dashboard
```
GET /api/dashboard
Authorization: Bearer <sessionToken>

Response (200 OK):
{
  "greeting": "Welcome, John",
  "title": "User Dashboard",
  "content": "You have access to your dashboard"
}
```

#### 5. Get User Profile
```
GET /api/user/profile/{userId}
Authorization: Bearer <sessionToken>

Response (200 OK):
{
  "userId": 1,
  "username": "johndoe",
  "email": "john@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "role": "USER",
  "isActive": true
}
```

#### 6. Update User Profile
```
PUT /api/user/profile/{userId}
Authorization: Bearer <sessionToken>
Content-Type: application/json

{
  "userId": 1,
  "username": "johndoe",
  "email": "john@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "role": "USER",
  "isActive": true
}

Response (200 OK):
{
  "userId": 1,
  "username": "johndoe",
  "email": "john@example.com",
  "firstName": "John",
  "lastName": "Doe",
  "role": "USER",
  "isActive": true
}
```

#### 7. View Protected Pages
```
GET /api/protected/pages
Authorization: Bearer <sessionToken>

Response (200 OK):
{
  "hasAccess": true,
  "status": "Access Granted",
  "message": "You are accessing protected content",
  "username": "johndoe"
}
```

#### 8. Check Login Status
```
GET /api/protected/check-status
Authorization: Bearer <sessionToken>

Response (200 OK):
{
  "loggedIn": true,
  "username": "johndoe"
}
```

## Building and Running

### Prerequisites
- Java 17+
- Maven 3.8+

### Build Project
```bash
cd backend/UserAuth
mvn clean install
```

### Run Application
```bash
mvn spring-boot:run
```

The application will start on **http://localhost:8080**

### Access Points
- **API Base**: http://localhost:8080/api
- **H2 Console**: http://localhost:8080/h2-console

## Security Features

1. **Password Hashing**: BCrypt algorithm with salt
2. **Session Management**: UUID-based session tokens with 24-hour expiration
3. **Input Validation**: Jakarta Validation annotations
4. **CORS Support**: Configured for frontend at `http://localhost:5173` and `http://localhost:3000`
5. **Authorization**: Token-based access control on protected endpoints

## Data Models

### User Entity
```java
- user_id (Integer, PK, Auto-increment)
- username (String, unique, not null)
- email (String, unique, not null)
- password_hash (String, not null)
- first_name (String)
- last_name (String)
- role (Enum: ADMIN, USER, GUEST)
- is_active (Boolean, default: true)
- created_at (Timestamp, auto-set)
- updated_at (Timestamp, auto-updated)
- last_login (Timestamp)
```

### User Roles
- **ADMIN**: Full system access
- **USER**: Standard user with profile access
- **GUEST**: Limited read-only access

## Error Handling

The API returns appropriate HTTP status codes:
- `200 OK`: Successful GET/PUT request
- `201 Created`: Successful POST for resource creation
- `400 Bad Request`: Invalid input or validation failure
- `401 Unauthorized`: Missing or invalid authentication token
- `403 Forbidden`: Access denied (insufficient permissions)
- `404 Not Found`: Resource not found
- `500 Internal Server Error`: Server-side error

## Next Steps

1. **Configure for Production**:
   - Change `ddl-auto` to `update`
   - Use persistent database (MySQL/PostgreSQL)
   - Update `jwt.secret` in properties
   - Remove H2 console access

2. **Enhanced Security**:
   - Implement JWT tokens instead of UUID sessions
   - Add refresh token mechanism
   - Implement rate limiting
   - Add audit logging

3. **Frontend Integration**:
   - Connect React frontend at `http://localhost:5173`
   - Configure CORS headers properly
   - Implement token persistence in localStorage

## Testing

Run tests with:
```bash
mvn test
```

Current test setup includes:
- Spring Boot application tests in `src/test/java`
- Security test dependency available for integration tests

## Troubleshooting

**Issue**: Table not found when accessing endpoints
- **Solution**: Ensure `spring.jpa.hibernate.ddl-auto=create-drop` is set

**Issue**: BCrypt encoding not working
- **Solution**: Verify `SecurityConfig.java` is in a package scanned by `@SpringBootApplication`

**Issue**: CORS errors from frontend
- **Solution**: Update `cors.allowedOrigins` in `application.properties`

## Dependencies

Key dependencies included:
- `spring-boot-starter-data-jpa`: ORM and database access
- `spring-boot-starter-security`: Security features
- `spring-boot-starter-web`: REST controller support
- `spring-boot-starter-validation`: Input validation
- `h2`: In-memory database
- `lombok`: Boilerplate reduction
- `spring-security-test`: Security testing

---

**Author**: Group 2, IT342
**Date Created**: February 2026
**Last Updated**: February 15, 2026
