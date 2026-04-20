# Employee Management REST API

A practical API for managing employees and their departments in a business context. The API is implemented in Java using Spring Boot.

---

## Key Features

- Add, update, and delete employee records.
- Manage departments, including checking if a department accepts interns.
- Prevent duplicate entries (e.g., emails must be unique for each employee).
- Strict validation and descriptive error responses

---

## Getting Started

### Prerequisites

- Java 17 or later installed.
- Maven for build/dependency management.
- A relational database (H2 for testing, or configure MySQL/PostgreSQL in `application.yaml`).

### Run the Application

```bash
mvnw spring-boot:run
```
The API will start locally at `http://localhost:8080`.

---

## REST API Endpoints: Business-Driven Usage

### 1. Department Management

#### Create a Department
- **POST** `/api/v1/departments`
- Adds a new department. Useful for onboarding a new line of business.

**Request Example**

```json
{
  "departmentName": "Engineering",
  "isAcceptingIntern": true
}
```

#### Get All Departments

- **GET** `/api/v1/departments`
- Retrieve a list of all departments (e.g., when allocating a new hire).

#### Get a Department by ID

- **GET** `/api/v1/departments/{id}`
- Use this to fetch information for a specific department (e.g., verifying its status).

#### Update a Department

- **PUT** `/api/v1/departments/{id}`
- Change department details, like renaming or changing intern acceptance.

**Request Example**

```json
{
  "departmentName": "Finance",
  "isAcceptingIntern": false
}
```

#### Delete a Department

- **DELETE** `/api/v1/departments/{id}`
- Remove a department no longer in use (e.g., after company restructuring).

---

### 2. Employee Management
**NOTE: ** Make sure the department has been created before creating a new Employee.

- `firstName` (max 50 chars)
- `lastName` (max 50 chars)
- `email` (must be valid and unique)
- `department` (must exist)
- `salary` (positive decimal)
- `dateOfJoining` (cannot be in the future)
- `active` (boolean: true/false)
- `isAnIntern` (boolean: true/false)

#### Example: Add a New Employee

```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "departmentName": "Software developer",
  "salary": 35000.00,
  "dateOfJoining": "2023-11-01",
  "active": true,
  "isAnIntern": false
}
```
- All fields are required except `id`, `createdAt`, `updatedAt` which are handled by the system.

#### Update, Patch, Delete, and Fetch Employee

- Use the relevant HTTP verbs (`PUT`, `PATCH`, `DELETE`, `GET`).
### Fetch Employee

- Get details for a specific employee
- Endpoint: GET /api/v1/employees/{id}
- Description: Retrieve a single employee’s profile by their unique ID.
- Request Body: None required
- Response: Returns employee details.

---
### Update Employee (Full Update)

- Completely replaces the employee’s data with new values. All fields must be provided.
- Endpoint: PUT /api/v1/employees/{id}
- Description: Useful for updating all fields for a staff record.
- Request Body:
```json
{
  "firstName": "John",
  "lastName": "Doe",
  "email": "john.doe@example.com",
  "departmentName": "Finance",
  "salary": 35000.00,
  "dateOfJoining": "2023-11-01",
  "active": true,
  "isAnIntern": false
}
```
- Notes: Every field is required. The request will fail if any field is missing.
---
### Patch Employee (Partial Update)

- Updates only specific fields (salary, departmentName, or active status).
- Endpoint: PATCH /api/v1/employees/{id}
- Description: Changing salary or department without touching other info.
- Example Request Body (any combination of the below):
```json
{
  "salary": 40000.00,
  "departmentName": "Engineering",
  "active": false
}
```
---
### Delete Employee

**Soft delete**: Marks an employee as deleted (retained for audit/compliance). 
- Endpoint: DELETE /api/v1/employees/{id}

**Hard delete**: Permanently removes an employee(The Employee must have active = false for hard delete to be successful)
- Endpoint: DELETE /api/v1/employees/{id}/hard

No request body required for either delete action.
## Validation & Errors

- All endpoints respond with relevant HTTP status codes and detailed error messages.
- Common business cases handled:
    - Duplicate email: returns a conflict (409) error.
    - Missing fields: returns a bad request (400).
    - Department/Employee not found: returns not found (404).

---

## Example Errors

**Duplicate Email:**
```json
{
  "httpStatus": "CONFLICT",
  "message": "Email already exists",
  "timestamp": "2026-04-20T12:30:00"
}
```

**Department Not Found:**
```json
{
  "httpStatus": "NOT_FOUND",
  "message": "Department does not exist for ID: 99",
  "timestamp": "2026-04-20T12:31:00"
}
```

---

## Technical Notes

- All validation is enforced with clear constraints (e.g., email format, salary not negative, joining date not in future). This means you must provide correct data; the API will reject incorrect or incomplete requests.
- Department references must use correct IDs.
- All timestamps use ISO 8601 format (e.g., `"2023-11-01"` for dates).
