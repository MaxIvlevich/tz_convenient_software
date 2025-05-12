# XLSX Nth Minimum Finder Service

A Spring Boot application that provides a REST API endpoint to find the Nth smallest numerical value from the first column of an XLSX file.

## Features

*   Reads XLSX files using Apache POI.
*   Extracts numerical values from the first column of the first sheet.
*   Calculates the Nth minimum number efficiently using a Max-Heap (`PriorityQueue`).
*   Provides a REST API endpoint (`GET /api/xlsx/nth-min`).
*   Includes input validation for file path (existence, type, read permissions, `.xlsx` extension) and the value of 'n'.
*   Containerized using Docker and Docker Compose for easy deployment.
*   Uses SLF4J for logging.

## Prerequisites

*   Java 21 JDK
*   Maven (for building)
*   Docker
*   Docker Compose

## Getting Started

### 1. Clone the Repository

git clone https://github.com/MaxIvlevich/tz_convenient_software.git
cd tz_convenient_software 
### 2. Prepare Input Data
*The repository includes an empty data directory in the project root (tracked via .gitkeep). This directory is mapped to /data inside the Docker container when using docker-compose.

*Place your XLSX files inside this local ./data directory before starting the service, or copy them into the running container later using docker cp (see step 5).

*Input File Format: The service reads numerical values only from the first column (column A) of the first sheet in the XLSX file. Non-numeric cells in this column are ignored.

### 3. Build the Application
# Using Maven Wrapper (recommended)
./mvnw clean package

# Or using system Maven
# mvn clean package

This will create a .jar file in the target/ directory.
### 4. Run using Docker Compose (Recommended)
This is the easiest way to run the application with the correct file path mapping. Ensure Docker Desktop or Docker Engine with Compose is running.
docker-compose up --build -d
* --build: Builds the Docker image based on the Dockerfile if it doesn't exist or needs updating.

* -d: Runs the container in detached mode (in the background).
 The service will be available at http://localhost:8080. Any files placed in the local ./data directory before starting will be available inside the container at /data.

### 5. Adding Files to a Running Container
If the container is already running (started via docker-compose up -d) and you want to add a new XLSX file without stopping the service, you can use the docker cp command:
docker cp <path/to/your/local/file.xlsx> spring-xlsx-service:/data/

* Replace <path/to/your/local/file.xlsx> with the actual path to the XLSX file on your host machine.
* spring-xlsx-service is the container name defined in the docker-compose.yml file.
## Example:
* To copy a file named new_report.xlsx from your current directory into the container's /data directory:
docker cp ./new_report.xlsx spring-xlsx-service:/data/new_report.xlsx
fter copying, the file new_report.xlsx will be accessible to the service inside the container at the path /data/new_report.xlsx, and you can use this path in your API calls.
### 6. Run Natively (Alternative)
You can also run the JAR file directly. Make sure you provide the correct absolute or relative path to your XLSX files from the location where you run the command.
java -jar target/*.jar
##(Note: When running natively, the file path parameter in the API call should be the actual path on your host machine accessible by the running process, unlike the /data/... path used with Docker Compose.)

## API Usage
Endpoint
GET /api/xlsx/nth-min
Parameters
* path (string, required): The absolute path to the XLSX file inside the container. When using Docker Compose as described above, this path should start with /data/. Example: /data/numbers.xlsx or /data/new_report.xlsx (if copied later using docker cp).
* n (integer, required): The position of the minimum number to find (e.g., 1 for the minimum, 2 for the second minimum, etc.). Must be greater than 0 and not exceed the count of valid numbers in the file's first column.
## Technology Stack
* Java 21
* Spring Boot 3.x
* Apache POI (for XLSX handling)
* Lombok
* SLF4J + Logback (Logging)
* Docker
* Docker Compose
* Maven
