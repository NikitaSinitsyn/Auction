--changeset Nikita:1 tags=lot rollbackTag=lot_rollback
CREATE TABLE lot (
                     id BIGSERIAL PRIMARY KEY,
                     title VARCHAR(64) NOT NULL,
                     description VARCHAR(4096) NOT NULL,
                     start_price INTEGER NOT NULL CHECK (start_price >= 1),
                     bid_price INTEGER NOT NULL CHECK (bid_price >= 1),
                     status VARCHAR(255),
                     last_bidder VARCHAR(255),
                     current_price DECIMAL(19,2)
);
--changeset Nikita:2 tags=bid rollbackTag=bid_rollback
CREATE TABLE bid (
                     id BIGSERIAL PRIMARY KEY,
                     bidder_name VARCHAR(255) NOT NULL,
                     bid_date TIMESTAMPTZ NOT NULL,
                     lot_id BIGINT,
                     FOREIGN KEY (lot_id) REFERENCES lot (id) ON DELETE CASCADE ON UPDATE CASCADE
);
