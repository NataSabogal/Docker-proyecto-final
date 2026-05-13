CREATE DATABASE IF NOT EXISTS university;

USE university;

CREATE TABLE IF NOT EXISTS students (
                                        id INT AUTO_INCREMENT PRIMARY KEY,
                                        name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    career VARCHAR(100) NOT NULL
    );

INSERT INTO students(name, age, career)
VALUES
    ('Mario', 21, 'Systems Engineering'),
    ('Laura', 24, 'Industrial Engineering');