# Smart Canteen / MealBridge Backend

Spring Boot 3 + Java 17 + MySQL REST API for the React frontend prototype.

## Prerequisites
- Java 17+
- Maven 3.8+
- MySQL 8+

## Run
1. Create a MySQL database (optional because `createDatabaseIfNotExist=true`):
   ```sql
   CREATE DATABASE mealbridge;
   ```
2. Configure credentials in `src/main/resources/application.properties` or environment variables:
   - `DB_URL` (default `jdbc:mysql://localhost:3306/mealbridge?...`)
   - `DB_USERNAME` (default `root`)
   - `DB_PASSWORD`
   - `ADMIN_EMAIL` (default `admin@mealbridge.local`)
   - `ADMIN_PASSWORD` (default `ChangeMe123!`)
   - `CORS_ALLOWED_ORIGINS` (comma-separated; default localhost Vite/CRA origins)
3. Start the server:
   ```bash
   mvn spring-boot:run
   ```
4. Base URL: `http://localhost:8081`

## API endpoints

### Authentication
- `POST /api/auth/register` — donor or organizer registration
- `POST /api/auth/login` — login; admin credentials are configured through environment variables

Example register body:
```json
{
  "name": "Green Leaf Canteen",
  "organization": "Green Leaf Canteen",
  "email": "donor@example.com",
  "phone": "9876543210",
  "password": "StrongPass123",
  "role": "Donor"
}
```

Example login body:
```json
{"email":"donor@example.com","password":"StrongPass123","role":"Donor"}
```

### Users / Admin
- `GET /api/users`
- `PATCH /api/users/{id}/status` with `{"status":"SUSPENDED"}`

### Donations
- `GET /api/donations`
- `GET /api/donations/available`
- `GET /api/donations/donor/{donorId}`
- `POST /api/donations/donor/{donorId}`
- `PATCH /api/donations/{id}/status?accountId={id}&admin=false` with `{"status":"RESERVED"}`

Example donation body:
```json
{
  "foodName":"Vegetable rice meals",
  "category":"Cooked meals",
  "quantity":25,
  "unit":"meals",
  "pickupDeadline":"2026-12-31T15:30:00",
  "pickupAddress":"Madhapur, Hyderabad",
  "notes":"Contains peanuts"
}
```

### Collection requests
- `GET /api/collection-requests`
- `GET /api/collection-requests/organizer/{organizerId}`
- `POST /api/collection-requests/donation/{donationId}/organizer/{organizerId}`
- `PATCH /api/collection-requests/{id}/status` with `{"status":"APPROVED"}`

### Dashboard
- `GET /api/dashboard/summary`

## Important security note
This is a starter backend for local integration, not production-secure yet. It hashes registered passwords with BCrypt, but the demo login returns user details without issuing a session/JWT, and some admin endpoints are not protected by authorization middleware. Do not deploy publicly or store real user data until JWT/session authentication, role-based authorization, rate limiting, and admin endpoint protection are added. The React frontend must be updated to call these API endpoints and pass actual account IDs."# Canteen-BackEnd" 
