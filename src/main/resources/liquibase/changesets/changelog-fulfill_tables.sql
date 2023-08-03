--changeset Nikita:1 tags=lot_fulfill rollbackTag=lot_fulfill
INSERT INTO lot (title, description, start_price, bid_price)
VALUES ('Lot 1', 'Random 1', 100, 10),
       ('Lot 2', 'Random 2', 200, 20);

--changeset Nikita:2 tags=bid_fulfill rollbackTag=bid_fulfill
INSERT INTO bid (bidder_name, bid_date, lot_id)
VALUES ('Участник 1', NOW(), 1),
       ('Участник 2', NOW(), 1),
       ('Участник 3', NOW(), 2);
