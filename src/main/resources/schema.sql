CREATE TABLE IF NOT EXISTS mpa_rating (
    id BIGINT PRIMARY KEY,
    name VARCHAR(20) NOT NULL
);
CREATE TABLE IF NOT EXISTS films (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    description VARCHAR(200) NOT NULL,
    release_date DATE NOT NULL,
    duration INTEGER NOT NULL,
    mpa_rating_id INTEGER REFERENCES mpa_rating(id)
);
CREATE TABLE IF NOT EXISTS genre (
    id BIGINT PRIMARY KEY,
    name VARCHAR(20) NOT NULL
);
CREATE TABLE IF NOT EXISTS film_genre (
    film_id BIGINT REFERENCES films(id),
    genre_id BIGINT REFERENCES genre(id)
);
CREATE TABLE IF NOT EXISTS users (
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    email VARCHAR(30) NOT NULL,
    login VARCHAR(20) NOT NULL,
    name VARCHAR(20) NOT NULL,
    birthday DATE
);
CREATE TABLE IF NOT EXISTS film_likes (
    film_id BIGINT REFERENCES films(id),
    user_id BIGINT REFERENCES users(id)
);
CREATE TABLE IF NOT EXISTS friends (
    sender_id BIGINT REFERENCES users(id),
    receiver_id BIGINT REFERENCES users(id),
    is_approved BOOL
);