INSERT INTO ingredient (name, latest_modification, unit) VALUES ('Saucisse', '2025-01-01 00:00', 'G'),
                                                                ('Huile', '2025-01-01 00:00','L'),
                                                                ('Oeuf', '2025-01-01 00:00','U'),
                                                                ('Pain', '2025-01-01 00:00','U');

INSERT INTO dish (name, unit_price) VALUES ('Hot dog', 15000);

INSERT INTO dish_ingredient VALUES (1, 1, 100),
                                   (1, 2, 0.15),
                                   (1, 3, 1),
                                   (1, 4, 1);

INSERT INTO price (amount, ingredient_id) VALUES (20,1),
                                                 (10000,2),
                                                 (1000,3),
                                                 (1000,4);

INSERT INTO price (ingredient_id, amount, begin_date) VALUES (1, 25, '2025-02-24 19:20'),
                                                             (2, 10500, '2025-02-24 19:20'),
                                                             (3, 1100, '2025-02-24 19:20'),
                                                             (4, 1200, '2025-02-24 19:20');

INSERT INTO ingredient_move (ingredient_id, type, ingredient_quantity, unit, move_date) VALUES
                                                                                            (3, 'IN', 100, 'U', '2025-02-01 08:00'),
                                                                                            (4, 'IN', 50, 'U', '2025-02-01 08:00'),
                                                                                            (1, 'IN', 10000, 'G', '2025-02-01 08:00'),
                                                                                            (2, 'IN', 20, 'L', '2025-02-01 08:00');

INSERT INTO ingredient_move (ingredient_id, type, ingredient_quantity, unit, move_date) VALUES
                                                                                            (3, 'OUT', 10, 'U', '2025-02-02 10:00'),
                                                                                            (3, 'OUT', 10, 'U', '2025-02-03 15:00'),
                                                                                            (4, 'OUT', 20, 'U', '2025-02-05 16:00');

INSERT INTO ingredient (name, unit) VALUES ('Sel', 'G'),
                                           ('Riz', 'G');

INSERT INTO price (amount, ingredient_id) VALUES (2.5,5),
                                                 (3.5,6);

INSERT INTO "order"(order_date) VALUES ('2025-03-21 07:00:00');

INSERT INTO dish_order(order_id, dish_id, dish_quantity, dish_creation_date) VALUES(1, 1, 3, '2025-03-21 07:10:00');

INSERT INTO  dish_order_status (dish_order_id, dish_order_status, dish_order_status_date) VALUES (1, 'CREATED',  '2025-03-21 07:10:00');

INSERT INTO order_status (order_id, order_status, order_status_date) VALUES (1, 'CREATED', '2025-03-21 07:00:00');