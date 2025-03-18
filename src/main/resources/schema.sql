CREATE TABLE IF NOT EXISTS users
(
    id                   SERIAL PRIMARY KEY,
    name                 VARCHAR(256)     NOT NULL,
    email                VARCHAR(256)     NOT NULL,
    age                  INT              NOT NULL CHECK (age > 0),
    weight               DOUBLE PRECISION NOT NULL CHECK (weight > 0),
    height               DOUBLE PRECISION NOT NULL CHECK (height > 0),
    weightGoal           VARCHAR(32)      NOT NULL,
    daily_calorie_intake INT              NOT NULL
);
