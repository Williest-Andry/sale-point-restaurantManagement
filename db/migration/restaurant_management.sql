DROP DATABASE IF EXISTS restaurant_management;
CREATE DATABASE restaurant_management;

\c restaurant_management

DROP TYPE IF EXISTS unit;
CREATE TYPE unit AS ENUM ('G', 'L', 'U');

CREATE TABLE ingredient (
                            id SERIAL PRIMARY KEY,
                            name VARCHAR(50) NOT NULL,
                            latest_modification TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                            unit UNIT NOT NULL
);

CREATE TABLE dish (
                      id SERIAL PRIMARY KEY,
                      name VARCHAR(50) NOT NULL,
                      unit_price DECIMAL NOT NULL
);

CREATE TABLE dish_ingredient (
                                 dish_id SERIAL REFERENCES dish(id) NOT NULL,
                                 ingredient_id SERIAL REFERENCES ingredient(id) NOT NULL,
                                 ingredient_quantity DECIMAL NOT NULL
);

CREATE TABLE price (
                       id SERIAL PRIMARY KEY,
                       ingredient_id SERIAL REFERENCES ingredient(id),
                       amount DECIMAL NOT NULL,
                       begin_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TYPE move_type AS ENUM ('entree', 'sortie');  -- refactor in line 101

CREATE TABLE ingredient_move (
                                 id SERIAL PRIMARY KEY,
                                 ingredient_id SERIAL REFERENCES ingredient(id),
                                 type MOVE_TYPE NOT NULL,
                                 ingredient_quantity DECIMAL NOT NULL,
                                 unit UNIT NOT NULL,
                                 move_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

UPDATE price SET begin_date = '2025-02-24 13:58:22';

UPDATE ingredient SET latest_modification = '2025-02-27 12:07:14' WHERE id = 5;
UPDATE ingredient SET latest_modification = '2025-02-27 12:07:14' WHERE id = 6;

UPDATE price SET begin_date = '2025-02-27 12:08:05' WHERE id = 9;
UPDATE price SET begin_date = '2025-02-27 12:08:05' WHERE id = 10;

DELETE FROM ingredient_move WHERE id = 10;
DELETE FROM ingredient_move WHERE id = 11;

UPDATE ingredient_move SET move_date= '2025-03-02 07:00:00' WHERE id=14;

CREATE TYPE status AS ENUM ('CREATED', 'VERIFIED', 'IN_PREPARATION', 'DONE', 'SERVED');  -- refactor in line 103

CREATE TABLE "order" (
                         id SERIAL PRIMARY KEY,
                         order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL,
                         reference SERIAL NOT NULL
);

CREATE TABLE order_status (
                              id SERIAL PRIMARY KEY,
                              order_id SERIAL REFERENCES "order"(id),
                              order_status status NOT NULL,
                              order_status_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE dish_order (
                            id SERIAL PRIMARY KEY,
                            order_id SERIAL REFERENCES "order"(id),
                            dish_id SERIAL REFERENCES dish(id),
                            dish_quantity INTEGER NOT NULL,
                            dish_creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

CREATE TABLE dish_order_status (
                                   id SERIAL PRIMARY KEY,
                                   dish_order_id SERIAL REFERENCES dish_order(id),
                                   dish_order_status status NOT NULL,
                                   dish_order_status_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);

ALTER TABLE dish_ingredient ADD COLUMN id SERIAL PRIMARY KEY;
ALTER TABLE dish_ingredient ADD COLUMN unit UNIT;
UPDATE dish_ingredient SET unit='G' WHERE id=1;
UPDATE dish_ingredient SET unit='L' WHERE id=2;
UPDATE dish_ingredient SET unit='U' WHERE id=3;
UPDATE dish_ingredient SET unit='U' WHERE id=4;
ALTER TABLE dish_ingredient ALTER COLUMN unit SET NOT NULL;

ALTER TABLE ingredient_move ALTER COLUMN type SET DATA TYPE VARCHAR;
UPDATE ingredient_move SET type = 'IN' WHERE type='entree';
UPDATE ingredient_move SET type = 'OUT' WHERE type='sortie';
ALTER TYPE move_type RENAME TO move_type_old;
CREATE TYPE move_type AS ENUM ('IN', 'OUT');
ALTER TABLE ingredient_move ALTER COLUMN type SET DATA TYPE move_type USING type::move_type;

ALTER TYPE status RENAME TO old_status;
CREATE TYPE status AS ENUM ('CREATED', 'CONFIRMED', 'IN_PREPARATION', 'DONE', 'SERVED');
ALTER TABLE dish_order_status ALTER COLUMN dish_order_status SET DATA TYPE VARCHAR;
ALTER TABLE dish_order_status ALTER COLUMN dish_order_status SET DATA TYPE status USING dish_order_status::status;
ALTER TABLE order_status ALTER COLUMN order_status SET DATA TYPE VARCHAR;
ALTER TABLE order_status ALTER COLUMN order_status SET DATA TYPE status USING order_status::status;