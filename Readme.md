# 🤖 API MS - Microservice for User and Item Management

## 📌 Project Description

API MS is a **microservice** based on **Spring Boot** that allows:

- **User registration** (`POST /register`)
- **User login and JWT token generation** (`POST /login`)
- **Adding user items** (`POST /items`)
- **Retrieving user items** (`GET /items`)

The service uses **JWT (JSON Web Token)** for user authentication and **MySQL + Hibernate** for data storage.

---

## 🛠 Technologies

- **Spring Boot 3.4.3**
- **Spring Security + JWT**
- **Hibernate + MySQL**
- **JUnit + Mockito** (unit testing)
- **Lombok**
- **RAML (API specification)**

---

## 🚀 Application Setup

### **1️⃣ Configure MySQL Database**

Create the MySQL database manually:

```sql
CREATE DATABASE microservice_db;
```

Alternatively, use Docker:

```bash
docker run --name mysql-container -e MYSQL_ROOT_PASSWORD=yourpassword -e MYSQL_DATABASE=microservice_db -p 3306:3306 -d mysql:8
```

---

### **2️⃣ Configure the `application.yml` and `application-local.yml` Files**

#### **`application.yml` (General Settings)**

```yaml
spring:
  profiles:
    active: local

  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
```

#### **`application-local.yml` (Local Configuration - Not Committed to Repository)**

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/microservice_db
    username: ${DB_USERNAME:root}
    password: ${DB_PASSWORD:yourpassword}
    driver-class-name: com.mysql.cj.jdbc.Driver

jwt:
  secret: ${JWT_SECRET:your_secret_key}
```

📌 **Ensure that:**

- `jwt.secret` is **at least 32 characters long**.
- `username`, `password`, and `jwt.secret` can be overridden using environment variables (`DB_USERNAME`, `DB_PASSWORD`, and `JWT_SECRET`).

**Add `application-local.yml` to `.gitignore` to prevent it from being committed.**

```bash
echo "src/main/resources/application-local.yml" >> .gitignore
```

---

### **3️⃣ Run the Application**

You can run the application in two ways:

1️⃣ **Using Maven** *(for development)*

```bash
mvn spring-boot:run
```

2️⃣ **As a JAR file** *(for production)*

```bash
mvn clean package
java -jar target/recruitmentTask-0.0.1-SNAPSHOT.jar
```

📌 **The application should now be running at** `http://localhost:8080`.

---

## 📌 **API Endpoints (Testing in Postman or cURL)**

### **1️⃣ Register a User (`POST /register`)**

👉 **No authentication required.**

- **URL:** `http://localhost:8080/register`
- **Body (JSON):**

```json
{
  "login": "user@domain.com",
  "password": "SomePassword1"
}
```

- **Response:** `204 No Content`

---

### **2️⃣ User Login (`POST /login`)**

👉 **No authentication required.**

- **URL:** `http://localhost:8080/login`
- **Body (JSON):**

```json
{
  "login": "user@domain.com",
  "password": "SomePassword1"
}
```

- **Response (`200 OK`):**

```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

📌 **Save the JWT token** for authentication.

---

### **3️⃣ Add an Item (`POST /items`)**

👉 **Requires a JWT token.**

- **URL:** `http://localhost:8080/items`
- **Headers:**
  ```
  Authorization: Bearer <token>
  ```
- **Body (JSON):**

```json
{
  "name": "My new item"
}
```

- **Response:** `204 No Content`

---

### **4️⃣ Retrieve User Items (`GET /items`)**

👉 **Requires a JWT token.**

- **URL:** `http://localhost:8080/items`
- **Headers:**
  ```
  Authorization: Bearer <token>
  ```
- **Response (`200 OK`):**

```json
[
  {
    "id": "6210b1a3-2499-446d-a687-cce010a49864",
    "name": "My item"
  }
]
```

📌 **If the JWT token is invalid or expired, it returns `401 Unauthorized`.**

---

## 🤦️‍♂️ Unit Tests

Run unit tests using:

```bash
mvn test
```

📌 **Expected output if all tests pass:**

```
[INFO] Tests run: 10, Failures: 0, Errors: 0, Skipped: 0
```

---

## 📌 Summary

✔ **Spring Boot + Hibernate + JWT + MySQL**\
✔ **Full authentication system using JWT**\
✔ **Unit tests with JUnit + Mockito**\
✔ **RAML-compliant API**\
✔ **Easy setup and deployment**

📌 **Have questions? Let me know! 🚀**

