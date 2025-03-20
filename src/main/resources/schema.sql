CREATE TABLE IF NOT EXISTS users
(
    id                   SERIAL PRIMARY KEY,
    name                 VARCHAR(256)     NOT NULL,
    email                VARCHAR(256)     NOT NULL,
    age                  INT              NOT NULL CHECK (age > 0),
    weight               DOUBLE PRECISION NOT NULL CHECK (weight > 0),
    height               DOUBLE PRECISION NOT NULL CHECK (height > 0),
    weight_goal           VARCHAR(256)      NOT NULL,
    daily_calorie_intake INT              NOT NULL
);

CREATE TABLE IF NOT EXISTS dishes
(
    id            SERIAL PRIMARY KEY,
    name          VARCHAR(255) NOT NULL,
    calories      INT          NOT NULL,
    proteins      INT          NOT NULL,
    fats          INT          NOT NULL,
    carbohydrates INT          NOT NULL
);

CREATE TABLE IF NOT EXISTS meals
(
    id        SERIAL PRIMARY KEY,
    user_id   INT       NOT NULL,
    meal_time TIMESTAMP NOT NULL CHECK (meal_time <= NOW()) DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS meal_dishes
(
    meal_id INT NOT NULL,
    dish_id INT NOT NULL,
    PRIMARY KEY (meal_id, dish_id),
    FOREIGN KEY (meal_id) REFERENCES meals (id) ON DELETE CASCADE,
    FOREIGN KEY (dish_id) REFERENCES dishes (id) ON DELETE CASCADE
);
