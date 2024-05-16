-- Table for storing transactions
CREATE TABLE IF NOT EXISTS transactions (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              amount DECIMAL(19, 2),
                              currency VARCHAR(255),
                              transaction_timestamp TIMESTAMP,
                              credit_card_id BIGINT,
                              recipient_id BIGINT,
                              FOREIGN KEY (credit_card_id) REFERENCES credit_cards(id),
                              FOREIGN KEY (recipient_id) REFERENCES recipients(id)
);

