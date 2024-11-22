CREATE TABLE Book (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      title VARCHAR(255) NOT NULL,
                      author VARCHAR(255) NOT NULL,
                      isbn VARCHAR(13) UNIQUE,
                      available_copies INT DEFAULT 0,
                      total_copies INT NOT NULL
);

CREATE TABLE AppUser (
                      id BIGINT AUTO_INCREMENT PRIMARY KEY,
                      name VARCHAR(255) NOT NULL,
                      email VARCHAR(255) UNIQUE NOT NULL,
                      password VARCHAR(255) NOT NULL
);

CREATE TABLE BorrowedBook (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              user_id BIGINT NOT NULL,
                              book_id BIGINT NOT NULL,
                              borrowed_date DATE NOT NULL,
                              return_date DATE,
                              CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES User(id) ON DELETE CASCADE,
                              CONSTRAINT fk_book FOREIGN KEY (book_id) REFERENCES Book(id) ON DELETE CASCADE
);
