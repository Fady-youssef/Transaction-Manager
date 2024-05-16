-- Table for storing recipient information
CREATE TABLE IF NOT EXISTS recipients (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            recipient_name VARCHAR(255) UNIQUE,
                            recipient_email VARCHAR(255),
                            recipient_phone VARCHAR(255)
);
