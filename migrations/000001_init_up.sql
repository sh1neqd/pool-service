CREATE TABLE users (
                       id SERIAL PRIMARY KEY,
                       name VARCHAR(100),
                       phone VARCHAR(255) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE orders (
                        id SERIAL PRIMARY KEY,
                        date DATE NOT NULL,
                        time TIME NOT NULL,
                        user_id BIGINT NOT NULL,
                        FOREIGN KEY (user_id) REFERENCES users(id)
);


