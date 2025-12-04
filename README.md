# User Microservice

## Overview
This microservice handles all user-related operations for the Beauty Salon application.  
It provides REST API endpoints for user registration, authentication, profile management, and role-based access control.

---

## Tech Stack
- Java 17
- Spring Boot
- Spring MVC
- Spring Security
- Spring Data JPA
- REST API
- MySQL / H2 (for testing)
- Maven
- JUnit 5 + Mockito (for testing)

---

## Features
- User registration and login
- Role-based access control (USER vs ADMIN)
- Fetch user details
- Update user profile
- Input validation and error handling

---

## API Endpoints

| Method | Endpoint            | Description                     | Request Body Example                          |
|--------|-------------------|---------------------------------|----------------------------------------------|
| POST   | `/api/users/register` | Register a new user             | `{ "username": "testuser", "email": "test@example.com", "password": "123456" }` |
| POST   | `/api/users/login`    | Authenticate a user             | `{ "username": "testuser", "password": "123456" }` |
| GET    | `/api/users/{id}`     | Get user details by ID          | N/A                                          |
| PUT    | `/api/users/{id}`     | Update user profile            | `{ "firstName": "John", "lastName": "Doe", "email": "john@example.com" }` |
| DELETE | `/api/users/{id}`     | Delete a user account          | N/A                                          |

---

## Security
- Role-based authorization ensures that only authorized users can perform certain actions

---

## Testing
- Use **Postman** to test all endpoints
- Example registration request:
  ```bash
  GET http://localhost:8081/api/v1/users?username=vankata8563

