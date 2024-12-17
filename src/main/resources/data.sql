CREATE TABLE IF NOT EXISTS users
(
    id
    bigint
    NOT
    NULL
    AUTO_INCREMENT,
    username
    VARCHAR
(
    255
) NOT NULL UNIQUE,
    password VARCHAR
(
    255
) NOT NULL,
    PRIMARY KEY
(
    id
)
    );


CREATE TABLE expenses
(
    id       bigint         NOT NULL AUTO_INCREMENT,
    user_id  bigint         NOT NULL,
    title    VARCHAR(255)   NOT NULL,
    amount   decimal(10, 2) NOT NULL,
    category VARCHAR(255)   NOT NULL,
    date     DATE,
    PRIMARY KEY (id),
    FOREIGN KEY (user_id) REFERENCES users (id)
);

INSERT INTO users (id, username, password)
VALUES (1, 'chandrakant', 'chandu@123');

INSERT INTO users (id, username, password)
VALUES (2, 'pradnya', 'pradnya@123');



INSERT INTO expenses (user_id, id, title, amount, category, date)
VALUES (1, 1, 'Groceries', 80.50, 'Shopping', '2023-05-04'),
       (2, 2, 'Oil', 80.50, 'Shopping', '2023-05-04')

