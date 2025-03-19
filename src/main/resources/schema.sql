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
