# Video Streaming REST API

A video streaming RESTful API built with **Spring Boot** that connects to a **MySQL** database.  
This project manage Videos, associated Meta Data and engagement stats using **Spring Data JPA**. 
Integrations tests are included which are done using test containers and require Docker on system.
Maven is used for dependency Management.
It also includes a **Postman collection** to help you quickly test the endpoints.

---

## Table of Contents

1. [Features](#features)
2. [Assumptions/Decisions](#assumptions)
2. [Technologies Used](#technologies-used)
3. [Prerequisites](#prerequisites)
4. [Getting Started](#getting-started)
5. [Database Configuration](#database-configuration)
6. [Postman Collection](#postman-collection)
7. [Project Structure](#project-structure)

---

## Features

- **CRUD Operations** for managing Videos
- **Spring Data JPA** for database interaction
- **MySQL** running on Docker
- Unit and Integration tests using junit and test containers
- **REST Endpoints** that can be tested with Postman or any REST client

---

## Assumptions

- Video creation is split in two parts video content and metadata creations
- Video content is string that acts as mock for actual video content
- In Production, it should be path to video file and video content
  should be served in chunks(I assumed here that it should be string column here as mentioned in requirements for mocking purpose)
- Video can only be published if metadata is there for that video
- For Meta Data all fields are mandatory
- For Stats testing purpose user id is hardcoded to avoid implementation of user module
- For Audit also auditor is hardcoded,so implementations of access and privileges can be avoided
- Get all videos can be served both to content managers and users so published bit is added to serve published videos only to users
- Get all videos by director is assumed to be only used by end users so only published videos will be returned
- Video publish and delist can only be done if video has it's associated metadata


---

## Technologies Used

- **Java** 
- **Spring Boot**
- **MySQL** (via Docker)
- **Maven**
- **Postman** (for testing functionality)
- **Junit and test containers** (unit and integration tests)

---

## Prerequisites

- **Java** 
- **Maven**
- **Docker**
- **Git**

---

## Getting Started

1. **Clone the Repository**
   ```bash
   git clone https://github.com/.git
   cd video-streaming-rest-api
   ```
2. Run MySQL in Docker (Optional) If you don’t have MySQL installed locally, spin up a container:
   ```bash
   docker run -d \
    --name dev-mysql \
    -e MYSQL_ROOT_PASSWORD=secret \
    -e MYSQL_DATABASE=videostreaming \
    -p 3306:3306 \
    mysql:8
   ```
3. Configure Application Properties
   Check src/main/resources/application.properties to ensure the database URL, username, and password match your MySQL
   instance.
4. Build and Run the Application
   ```bash
   mvn spring-boot:run
   ```

The application will start on http://localhost:8080.

## Database Configuration

In application.properties, you can customize the following properties:

```bash
spring.datasource.url=jdbc:mysql://localhost:3306/videostreaming
spring.datasource.username=root
spring.datasource.password=secret

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
```

## Postman Collection

A Postman collection is provided in the src/main/resources folder:
Video API.postman_collection.json

### How to Use

1. Open Postman and click Import.
2. Choose the file Video API.postman_collection.json.
3. You’ll see the collection with pre-configured endpoints under Collections in Postman.
4. Make sure your server is running locally on port 8080.
5. Send requests in following sequence to create and publish video 
    1) Create a new Video (this will create new video with content)
    2) Update Video Meta Data by ID (this will create or update video meta data) 
    3) Publish Video by ID (this will update published bit for both video and it's meta data)
6. Other endpoints can be used in any sequence

## Project Structure

The project is structured as follows:

```scssspring-video-streaming-restapi
├── src
│   ├── main
│   │   ├── java
│   │   │   └── video.streaming.api
│   │   │       ├── VideoApiApplication.java
│   │   │       ├── controller        # RestControllers
│   │   │       ├── entity            # JPA Entities
│   │   │       ├── config            # Configurations
│   │   │       ├── models            # Models
│   │   │       ├── repository        # JPA Repositories
│   │   │       └── service           # Services
│   │   └── resources
│   │       ├── application.properties
│   │       ├── openapi.yaml
│   │       └── Video API.postman_collection.json
│   ├── test
│   │   ├── java
│   │         ├── Integration              # Integration tests
│   │         ├── video.streaming.ap        # Unit tests
├── pom.xml
└── README.md
```



