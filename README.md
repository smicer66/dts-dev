
# HMCTS

HMCTS is a system developed to help caseworkers efficiently track and manage their tasks. This project provides a backend API for creating, updating, and retrieving tasks, ensuring a streamlined workflow for caseworkers.
## Features

[Features]
Create, update, and retrieve caseworker tasks
RESTful API endpoints for task management
Data persistence with JPA / Hibernate
Unit testing with JUnit5


## Tech Stack
Backend: Spring Boot
Database: JPA / Hibernate
Testing: JUnit5
Build Tool: Gradle
## Setup & Installation
Prerequisites
Java 17 or above
Gradle
IDE (IntelliJ, Eclipse, or VS Code)

Steps
Clone the repository
git clone https://github.com/smicer66/dts-dev.git
cd dts-dev
Build the project
./gradlew build
Run the application
./gradlew bootRun
Access endpoints

The application runs by default on:

http://localhost:8080
## API Endpoints & Examples
1. Get All Tasks
GET /api/v1/tasks

cURL:

curl -X GET http://localhost:8080/api/v1/tasks

Response Example:

[
  {
    "taskId": 1,
    "title": "Review case files",
    "description": "Check pending documents",
    "dueDateTime": "2026-04-10T12:00:00",
    "taskStatus": "PENDING"
  }
]
2. Create a Task
POST /api/v1/tasks
Content-Type: application/json

cURL:

curl -X POST http://localhost:8080/api/v1/tasks \
-H "Content-Type: application/json" \
-d '{
  "title": "New Task",
  "description": "Task description here",
  "dueDateTime": "2026-04-10T12:00:00",
  "taskStatus": "PENDING"
}'
3. Update a Task
POST /api/v1/case-worker-tasks/update-task
Content-Type: application/json

cURL:

curl -X POST http://localhost:8080/api/v1/case-worker-tasks/update-task \
-H "Content-Type: application/json" \
-d '{
  "taskId": 1,
  "title": "Updated Task Title",
  "description": "Updated description",
  "dueDateTime": "2026-04-11T12:00:00",
  "taskStatus": "COMPLETED"
}'
4. Get Task by ID
GET /api/v1/tasks/{id}

cURL:

curl -X GET http://localhost:8080/api/v1/tasks/1

Response Example:

{
  "taskId": 1,
  "title": "Updated Task Title",
  "description": "Updated description",
  "dueDateTime": "2026-04-11T12:00:00",
  "taskStatus": "COMPLETED"
}
## Running Tests

Run unit tests with:

./gradlew test
## Contributing
Contributions are welcome! Fork the repository and submit pull requests for improvements or bug fixes.