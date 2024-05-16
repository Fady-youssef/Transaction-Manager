
-- Table for storing credit card information
CREATE TABLE IF NOT EXISTS credit_cards (
                              id BIGINT NOT NULL AUTO_INCREMENT,
                              card_number VARCHAR(255) NOT NULL UNIQUE,
                              card_holder_name VARCHAR(255),
                              expiry_date VARCHAR(255),
                              cvv VARCHAR(255),
                              PRIMARY KEY (id),
                              INDEX idx_card_number (card_number)
);
