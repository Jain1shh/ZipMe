# ZipMe – URL Shortener (Java + Spring Boot)

ZipMe is a URL-shortening backend service built using Java and Spring Boot.  
It generates short codes using Base62 encoding, supports optional custom aliases, handles redirects, tracks click counts, and stores data in an H2 file-based database with caching support for efficiency.

---

## Features

- Shorten long URLs using Base62 codes  
- Optional custom alias support (validated to prevent duplicates)  
- Redirect from short URL to long URL  
- Click count tracking  
- Spring Cache & Caffeine for faster redirects  
- Uses H2 database in file mode (persistent)  
- Simple Controller–Service–Repository structure  

---

## Tech Stack

- Java  
- Spring Boot  
- Spring Web  
- Spring Data JPA  
- Spring Cache & caffeine 
- H2 Database (file mode)  
- Lombok  

---

## Base62 Collision Handling

Base62 encoding itself does **not** guarantee uniqueness.  
Collisions normally happen when:

- Base62 is used on random numbers  
- Base62 is used on hashes of URLs  
- The same input accidentally appears twice  

However, ZipMe avoids this problem entirely by **not using Base62 on random or arbitrary data**.

## How It Works

1. **When creating a short URL:**
   - If the user provides an alias:
     - ZipMe checks if the alias is free.
     - If available, it uses the alias as the short code.
   - If no alias is given:
     - A temporary entry is saved to generate an auto-increment ID.
     - The ID is encoded using Base62.
     - The short code is updated in the database.

2. **When accessing a short code:**
   - ZipMe fetches the original URL (from cache or DB).
   - Increments the click count.
   - Redirects the user to the long URL.

---

## API Endpoints

### Create Short URL
```http
POST /zipme?longUrl={URL}&alias={optionalAlias}
```

**Example Response:**
```
Short Url: http://localhost:8080/ziped/abc
```

**If alias already exists:**
```
Alias myalias is already in use. Try another alias or go for default zipme.
```

---

### Redirect to Original URL
```http
GET /ziped/{code}
```

Redirects the user to the full long URL.

---

### Get URL Info
```http
GET /info/{code}
```

**Example Response:**
```json
{
  "id": 1,
  "longUrl": "https://example.com",
  "shortCode": "abc",
  "clickCount": 4
}
```

---

## Running the Project

1. **Clone the repository:**
```bash
   git clone https://github.com/Jain1shh/ZipMe
```

2. **Run the application:**
```bash
   ./mvnw spring-boot:run
```
   (or run from your IDE)

3. **Access the H2 Console:**
```
   http://localhost:8080/h2-console
```
   
   **JDBC URL:**
```
   jdbc:h2:file:./data/urlsdb
```

---

## Purpose

ZipMe was built to understand backend development fundamentals using Spring Boot, including ID-based short code generation, request handling, caching, and persistent storage.