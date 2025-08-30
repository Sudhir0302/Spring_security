# 🔐 Spring Security REST API with 2FA (Two-Factor Authentication)

This is a **Spring Boot** application that secures RESTful APIs using **Spring Security**, **JWT (JSON Web Tokens)**, and **Two-Factor Authentication (2FA)** using **Google Authenticator** and **TOTP** (Time-Based One-Time Password).

## 🚀 Features

*  User **registration** and **login** functionality
*  Passwords securely hashed with **BCrypt**
*  **JWT**-based authentication for securing endpoints
*  **Two-Factor Authentication (2FA)** using **Google Authenticator**
*  6-digit **OTP** generated every 30 seconds via TOTP
*  JWT token issued only after **valid OTP verification**
*  Secure access to protected REST endpoints

---

## 🔧 How Two-Factor Authentication Works

1. **Registration**

   * When a user registers, the backend:

     * Generates a **unique secret key**.
     * Returns a **QR code URL** that can be scanned using **Google Authenticator** or any TOTP app.

2. **User Setup**

   * The user scans the QR code in **Google Authenticator** (or enters the secret manually).
   * The app will now generate a **new 6-digit OTP every 30 seconds**.

3. **Login Process**

   * Step 1: User logs in with **username and password**.
   * Step 2: If credentials are valid, the server prompts for an **OTP**.
   * Step 3: The user enters the **OTP from the authenticator app**.
   * Step 4: If the OTP is correct, the server responds with a **JWT token**.

4. **Accessing Protected Endpoints**

   * All protected REST endpoints require the **JWT token** in the `Authorization` header.
   * Unauthorized or invalid token access is blocked by Spring Security.

---

## 📦 Tech Stack

* Java 17+
* Spring Boot 3+
* Spring Security
* JWT (JSON Web Tokens)
* BCrypt Password Encoder
* Google Authenticator (TOTP)
* Lombok (for boilerplate reduction)

---

## 🛠️ Getting Started

### Prerequisites

* JDK 17+
* Maven or Gradle
* Postman / curl / any REST client
* Google Authenticator app (iOS/Android)

### Clone the Project

```bash
git clone https://github.com/your-username/spring-security-2fa-jwt.git
cd spring-security-2fa-jwt
```

### Build & Run

```bash
./mvnw spring-boot:run
```

### API Endpoints Overview

| Endpoint        | Method | Description                    |
| --------------- | ------ | ------------------------------ |
| `/register`     | POST   | Register new user              |
| `/login`        | POST   | Login with username & password |
| `/verify-otp`   | POST   | Submit OTP after login         |
| `/protected/**` | GET    | Requires valid JWT token       |

---

## 📱 How to Use with Google Authenticator

1. Register a user via `/register`.
2. Get the **QR code URL** or **secret** in the response.
3. Add the secret to Google Authenticator manually or by scanning the QR.
4. Login via `/login` and then verify OTP using `/verify-otp`.
5. Use the returned JWT token in `Authorization: Bearer <token>` header for secured endpoints.

---

## 📄 License

This project is licensed under the **MIT License**.

---

## 🙌 Contribution

Feel free to fork this repo and submit a pull request for improvements or new features.
