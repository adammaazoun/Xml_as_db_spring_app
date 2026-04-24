# EquipSync

> **Equipment & Project Management Platform** — A full-stack web application that uses **BaseX (native XML database)** as its data store, powered by a **Spring Boot 3** REST backend and an **Angular 19** frontend with Server-Side Rendering.

---

## Table of Contents

- [Overview](#overview)
- [Architecture](#architecture)
- [Tech Stack](#tech-stack)
- [Features](#features)
- [Project Structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Getting Started](#getting-started)
  - [Backend Setup](#backend-setup)
  - [Frontend Setup](#frontend-setup)
- [Configuration](#configuration)
- [API Reference](#api-reference)
- [User Roles](#user-roles)
- [Data Model](#data-model)
- [Security](#security)


---

## Overview

EquipSync is an enterprise-grade equipment and project management system that deliberately replaces a relational database with **BaseX**, a high-performance native XML database. All entities — users, equipment, projects, and tasks — are persisted as structured XML documents and queried using **XQuery**. Data integrity is enforced by **XSD schemas** at the application layer before persistence.

### Key Design Decision

| Concern | Approach |
|---|---|
| Persistence | BaseX XML database (no SQL) |
| Serialization | JAXB marshalling → XML strings |
| Querying | XQuery executed via BaseX Java API |
| Schema validation | XSD files per entity |
| Auth | Stateless JWT (access + refresh tokens) |

---

## Architecture

```
┌─────────────────────────────────────────────────────┐
│                  Angular 19 Frontend                │
│   (SSR · Standalone Components · HTTP Interceptors) │
└─────────────────────────┬───────────────────────────┘
                          │ REST / JSON  (HTTP)
                          ▼
┌─────────────────────────────────────────────────────┐
│           Spring Boot 3.3 REST API  (:8081)         │
│                  /api/v1                            │
│  ┌───────────┐  ┌──────────┐  ┌──────────────────┐ │
│  │Controllers│→ │ Services │→ │  BaseXService     │ │
│  └───────────┘  └──────────┘  │  (XQuery engine)  │ │
│                               └────────┬─────────┘ │
│   Spring Security · JWT · JAXB         │            │
└────────────────────────────────────────┼────────────┘
                                         │ BaseX protocol (:1984)
                                         ▼
                          ┌──────────────────────────┐
                          │   BaseX XML Database      │
                          │  equipsync_db/            │
                          │   ├─ Users.xml            │
                          │   ├─ Equipments.xml       │
                          │   ├─ Projects.xml         │
                          │   └─ Tasks.xml            │
                          └──────────────────────────┘
```

---

## Tech Stack

### Backend
| Technology | Version | Purpose |
|---|---|---|
| Java | 17 | Language |
| Spring Boot | 3.3.2 | Application framework |
| Spring Security | 6.x | Authentication & authorization |
| BaseX | 11.5 | Native XML database |
| JAXB | 4.0 | XML serialization / deserialization |
| JJWT | 0.11.5 | JWT access & refresh tokens |
| Lombok | latest | Boilerplate reduction |
| Spring Mail | 3.x | Email notifications |
| Maven | 3.x | Build tool |

### Frontend
| Technology | Version | Purpose |
|---|---|---|
| Angular | 19.0 | SPA framework |
| Angular SSR | 19.0 | Server-side rendering |
| TypeScript | 5.5 | Language |
| RxJS | 7.8 | Reactive streams |
| Font Awesome | 4.7 | Icon library |

---

## Features

- **Role-Based Access Control** — Four distinct user roles (ADMIN, HR, EM, OM, EMPLOYEE) with route-level and method-level authorization.
- **XML-Native Persistence** — All CRUD operations are implemented as XQuery statements executed against a live BaseX instance. No ORM, no SQL.
- **XSD Schema Validation** — Each entity (`user`, `equipment`, `project`, `task`) has a corresponding `.xsd` schema for structural validation before insertion.
- **Stateless JWT Authentication** — Access tokens with configurable expiry and refresh token rotation stored in the XML database.
- **Equipment Assignment Workflow** — Tracks which equipment is assigned to which employee; prevents double-assignment.
- **Task Notification Emails** — Automatically sends Gmail SMTP notifications when a task is assigned to an employee.
- **Google OAuth2** — Preconfigured OAuth2 client registration for Google sign-in.
- **Angular SSR** — Server-side rendering with Express engine for improved SEO and first-paint performance.
- **Multipart File Upload** — Configurable upload limits (default 10 MB).
- **Global Exception Handling** — Centralized `GlobalExceptionHandler` for consistent error responses across all controllers.

---

## Project Structure

```
Xml_as_db_spring_app-main/
│
├── equipsync2/                          # Spring Boot backend
│   ├── pom.xml
│   └── src/main/java/projetxml/equipsync/
│       ├── EquipsyncApplication.java
│       ├── config/
│       │   ├── AppConfig.java
│       │   ├── BaseXConfig.java          # BaseX connection bean
│       │   ├── PasswordEncoderConfig.java
│       │   ├── SecurityConfig.java       # JWT filter chain
│       │   └── WebConfig.java            # CORS configuration
│       ├── controllers/
│       │   ├── AuthController.java       # /auth/login, /refreshToken, /logout
│       │   ├── UserController.java
│       │   ├── EquipmentController.java
│       │   ├── ProjectController.java
│       │   ├── TaskController.java
│       │   └── XQueryController.java     # Raw XQuery execution endpoint
│       ├── Services/
│       │   ├── BaseXService.java         # Low-level BaseX client wrapper
│       │   ├── XmlService.java           # Generic JAXB marshal/unmarshal
│       │   ├── UserService.java
│       │   ├── EquipmentService.java
│       │   ├── ProjectService.java
│       │   ├── TaskService.java
│       │   └── JwtService.java
│       ├── entities/
│       │   ├── User.java
│       │   ├── Equipment.java
│       │   ├── Project.java
│       │   └── Task.java
│       ├── security/
│       │   ├── JwtRequestFilter.java
│       │   ├── AuthRequest.java / AuthResponse.java
│       │   └── TokenRefreshRequest.java / TokenRefreshResponse.java
│       ├── xml_shemas/
│       │   ├── user.xsd
│       │   ├── equipment.xsd
│       │   ├── project.xsd
│       │   └── task.xsd
│       └── exeption/
│           ├── GlobalExceptionHandler.java
│           └── TokenRefreshException.java
│
└── front/equipSync2/                    # Angular 19 frontend
    ├── src/app/
    │   ├── login/                       # Login page + service
    │   ├── hr/                          # HR dashboard (user management)
    │   ├── em/                          # Equipment Manager dashboard
    │   ├── hr-equipment/               # HR equipment view
    │   └── interceptors/
    │       └── auth.interceptor.ts      # Attaches JWT to every request
    └── angular.json
```

---

## Prerequisites

| Requirement | Version |
|---|---|
| Java JDK | 17+ |
| Maven | 3.8+ |
| Node.js | 18+ |
| Angular CLI | 19+ |
| BaseX Server | 11.5 |

### Installing BaseX

Download BaseX from [https://basex.org/download](https://basex.org/download) and start the server:

```bash
# Start BaseX server (default port 1984)
basexserver
```

Then create the database and seed the XML collections inside the BaseX GUI or client:

```xquery
CREATE DB equipsync_db
CREATE DOCUMENT equipsync_db/Users.xml     "<users/>"
CREATE DOCUMENT equipsync_db/Equipments.xml "<equipments/>"
CREATE DOCUMENT equipsync_db/Projects.xml  "<projects/>"
CREATE DOCUMENT equipsync_db/Tasks.xml     "<tasks/>"
```

---

## Getting Started

### Backend Setup

```bash
# 1. Clone the repository
git clone https://github.com/your-username/equipsync.git
cd equipsync/equipsync2

# 2. Configure application properties (see Configuration section)
cp src/main/resources/application.properties src/main/resources/application-local.properties
# Edit application-local.properties with your values

# 3. Build & run
./mvnw spring-boot:run
```

The API will be available at `http://localhost:8081/api/v1`.

### Frontend Setup

```bash
cd ../front/equipSync2

# Install dependencies
npm install

# Start development server
npm start
```

The Angular app will be available at `http://localhost:4200`.

To build for production with SSR:

```bash
npm run build
npm run serve:ssr:equipSync2
```

---

## Configuration

All backend configuration lives in `equipsync2/src/main/resources/application.properties`:

```properties
# Server
server.port=8081
server.servlet.context-path=/api/v1

# BaseX Connection
basex.host=localhost
basex.port=1984
basex.username=admin
basex.password=<your-basex-password>

# JWT
security.jwt.secret-key=<your-256-bit-secret>
security.jwt.expiration-time=7200000          # 2 hours in ms
security.jwt.refresh-expiration-time=7200000

# Google OAuth2
spring.security.oauth2.client.registration.google.client-id=<your-client-id>
spring.security.oauth2.client.registration.google.client-secret=<your-client-secret>

# Email (Gmail SMTP)
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=<your-email@gmail.com>
spring.mail.password=<your-app-password>
spring.mail.properties.mail.smtp.auth=true
spring.mail.properties.mail.smtp.starttls.enable=true

# File Upload
spring.servlet.multipart.max-file-size=10MB
spring.servlet.multipart.max-request-size=10MB
```

> ⚠️ **Never commit real credentials.** Use environment variables or a secrets manager in production.

---

## API Reference

All endpoints are prefixed with `/api/v1`.

### Authentication — `/auth`

| Method | Endpoint | Description | Auth Required |
|---|---|---|---|
| `POST` | `/auth/login` | Authenticate and receive access + refresh tokens | No |
| `POST` | `/auth/refreshToken` | Exchange a refresh token for a new access token | No |
| `POST` | `/auth/logout` | Invalidate the current refresh token | No |

**Login request body:**
```json
{
  "username": "john.doe",
  "password": "secret"
}
```

**Login response:**
```json
{
  "accessToken": "<jwt>",
  "expiresIn": "<date>",
  "refreshToken": "<uuid>"
}
```

### Users — `/users`

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/users` | Get all users |
| `GET` | `/users/{id}` | Get user by ID |
| `POST` | `/users` | Create a new user |
| `PUT` | `/users/{id}` | Update a user |
| `DELETE` | `/users/{id}` | Delete a user |

### Equipment — `/equipments`

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/equipments` | Get all equipment |
| `GET` | `/equipments/{id}` | Get equipment by ID |
| `POST` | `/equipments` | Add new equipment |
| `PUT` | `/equipments/{id}` | Update equipment |
| `DELETE` | `/equipments/{id}` | Delete equipment |
| `POST` | `/equipments/affect` | Assign equipment to an employee |

### Projects — `/projects`

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/projects` | Get all projects |
| `GET` | `/projects/{id}` | Get project by ID |
| `POST` | `/projects` | Create a project |
| `PUT` | `/projects/{id}` | Update a project |
| `DELETE` | `/projects/{id}` | Delete a project |

### Tasks — `/tasks`

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/tasks` | Get all tasks |
| `GET` | `/tasks/{id}` | Get task by ID |
| `GET` | `/tasks/user/{userId}` | Get tasks assigned to a user |
| `POST` | `/tasks` | Create a task |
| `PUT` | `/tasks/{id}` | Update a task |
| `DELETE` | `/tasks/{id}` | Delete a task |

---

## User Roles

| Role | Description | Access |
|---|---|---|
| `ADMIN` | Full system access | All endpoints |
| `HR` | Human resources | User management |
| `EM` | Equipment Manager | Equipment CRUD & assignment |
| `OM` | Operations Manager | Project & task management |
| `EMPLOYEE` | End user | View own tasks & assigned equipment |

---

## Data Model

Entities are serialized to XML via JAXB. Below are representative XML snippets stored in BaseX.

**User**
```xml
<user>
  <userId>uuid</userId>
  <username>john.doe</username>
  <password>$2a$10$...</password>  <!-- BCrypt hashed -->
  <email>john@example.com</email>
  <role>EMPLOYEE</role>
  <refreshToken>uuid</refreshToken>
  <refreshToken_expiryDate>2024-12-11T12:00:00Z</refreshToken_expiryDate>
</user>
```

**Equipment**
```xml
<equipment>
  <equipmentId>uuid</equipmentId>
  <name>Laptop Dell XPS</name>
  <type>LAPTOP</type>
  <employeeId>user-uuid</employeeId>
</equipment>
```

**Task**
```xml
<task>
  <taskId>uuid</taskId>
  <title>Deploy v2.0</title>
  <description>...</description>
  <status>IN_PROGRESS</status>
  <equipmentId>equipment-uuid</equipmentId>
  <projectId>project-uuid</projectId>
</task>
```

---

## Security

- Passwords are hashed with **BCrypt** before being written to the XML database.
- Every protected request must carry a valid `Authorization: Bearer <token>` header, which is validated by `JwtRequestFilter` before reaching any controller.
- Refresh tokens are stored as XML fields on the User entity and are rotated on every refresh call.
- CSRF is disabled (stateless REST API).
- Sessions are `STATELESS` — no server-side session state.

<p align="center">Built with ☕ Java, 🌿 Spring Boot, and the unusual power of native XML databases.</p>
