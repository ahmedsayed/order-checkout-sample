INSERT INTO users (id, name, CREATED_AT, CREATED_BY) VALUES
  (1, 'Ahmed', CURRENT_TIMESTAMP, 'DUMMY'),
  (2, 'Hany', CURRENT_TIMESTAMP, 'DUMMY');

INSERT INTO partners (id, name, CREATED_AT, CREATED_BY) VALUES
  (1, 'Cook Door', CURRENT_TIMESTAMP, 'DUMMY'),
  (2, 'Primo''s Pizza', CURRENT_TIMESTAMP, 'DUMMY'),
  (3, 'Burger King', CURRENT_TIMESTAMP, 'DUMMY');

INSERT INTO items (id, name, price, seller_id, available, CREATED_AT, CREATED_BY) VALUES
  (1, 'Super Sandwich', 50, 1, true, CURRENT_TIMESTAMP, 'DUMMY'),
  (2, 'Normal Sandwich', 65.5, 1, true, CURRENT_TIMESTAMP, 'DUMMY'),
  (3, 'Sandwich X', 100, 1, false, CURRENT_TIMESTAMP, 'DUMMY'),
  (4, 'Chicken Pizza', 75, 2, true, CURRENT_TIMESTAMP, 'DUMMY'),
  (5, 'Cheese Pizza', 80, 2, true, CURRENT_TIMESTAMP, 'DUMMY'),
  (6, 'Burger Sandwich', 70, 1, true, CURRENT_TIMESTAMP, 'DUMMY'),
  (7, 'Double Burger Sandwich', 120, 1, true, CURRENT_TIMESTAMP, 'DUMMY');
