# Use the official MySQL Docker image as the base image
FROM mysql:latest

# Set environment variables
ENV MYSQL_ROOT_PASSWORD=admin
ENV MYSQL_DATABASE=transactionmanger
ENV MYSQL_USER=root
ENV MYSQL_PASSWORD=admin

# Copy initialization scripts into the Docker image
COPY ../init.sql /docker-entrypoint-initdb.d/

# Expose MySQL port
EXPOSE 3306

