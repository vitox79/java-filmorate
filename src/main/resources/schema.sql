CREATE TABLE IF NOT EXISTS users (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  email VARCHAR(255) NOT NULL,
  login VARCHAR(255) NOT NULL,
  name VARCHAR(255),
  birthday DATE
);

CREATE TABLE IF NOT EXISTS films (
  id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  description VARCHAR(200),
  release_date DATE,
  duration BIGINT NOT NULL,
  mpa_id INT
);





CREATE TABLE IF NOT EXISTS FRIENDS (
  user_id INT NOT NULL,
  friend_id INT NOT NULL,
  status VARCHAR(20) NOT NULL,
  PRIMARY KEY (user_id, friend_id),
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
  FOREIGN KEY (friend_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS LIKES (
  film_id INT NOT NULL,
  user_id INT NOT NULL,
  PRIMARY KEY (film_id, user_id),
  FOREIGN KEY (film_id) REFERENCES films(id) ON DELETE CASCADE,
  FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);
CREATE TABLE IF NOT EXISTS Genres (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);
CREATE TABLE IF NOT EXISTS film_genre (
  film_id INTEGER NOT NULL,
  genre_id INTEGER NOT NULL,
  FOREIGN KEY (film_id) REFERENCES films(id),
  FOREIGN KEY (genre_id) REFERENCES genres(id),
  PRIMARY KEY (film_id, genre_id)
);
CREATE TABLE IF NOT EXISTS MPA (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);