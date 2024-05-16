# Transaction Management System

## Overview

Welcome to Transaction Management System, a robust solution for handling credit card transactions efficiently. This system provides a REST API to process large XML files containing credit card transactions and offers various endpoints to manage transaction data effectively.

## Features

- **XML Processing API**: Efficiently process large XML files containing credit card transactions.
- **Spring Boot Application**: Built using the Spring Boot framework for rapid development and deployment.
- **MySQL Database**: Utilizes MySQL as the underlying database for persistent storage of transaction data.
- **Database Schema Migration**: Database schema is managed and migrated using either Flyway.
- **Docker Deployment**: Includes a Docker Compose file for easy deployment of the application, ensuring consistency across environments.
- **Memory Limitation**: Application's memory usage is restricted to 256MB.

## Endpoints

1. **Receive XML and Persist Data**:
   - Endpoint: `/api/transactions/upload`
   - Description: Receives XML files containing credit card transactions and persists the data into the database.

2. **Query Amount to Receive**:
   - Endpoint: `/api/transactions/amount-to-receive`
   - Description: Allows the recipient of the money to query how much money they are expected to receive.

3. **Get Transactions by Credit Card**:
   - Endpoint: `/api/transactions/{creditCardNumber}`
   - Description: Retrieves transactions made by a specific credit card.

## Setup

### Prerequisites

- Java Development Kit (JDK) - version 17 or higher
- Apache Maven
- Docker

### Steps

1. **Clone the Repository**:

    ```bash
    git clone https://github.com/your-username/transaction-management.git
    cd transaction-management
    ```

2. **Build the Project**:

    ```bash
    mvn clean install
    ```

3. **Start MySQL Server**:

    Ensure MySQL server is running and accessible. If using Docker, you can start a MySQL container with:

    ```bash
    docker run -d --name mysql-server -e MYSQL_ROOT_PASSWORD=rootpassword -p 3306:3306 mysql:latest
    ```

4. **Start the Application**:

    Use Docker Compose to start the application along with MySQL database:

    ```bash
    MYSQL_DATABASE=mydatabase MYSQL_PASSWORD=rootpassword docker-compose up
    ```

5. **Access the Application**:

    Once the setup is successful, access the following URLs in any web browser:

    - **Backend Health Check**: [http://localhost:8080/actuator](http://localhost:8080/actuator)
    - **API Documentation (Swagger UI)**: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)



## Support

For any queries or assistance, please contact [fady.raafatfarouk@gmail.com](mailto:your-email@example.com).
