CREATE TABLE students(
    id SERIAL NOT NULL PRIMARY KEY,
    first_name VARCHAR(64) NOT NULL,
    last_name VARCHAR(64) NOT NULL,
    age INT NOT NULL,
    group_id INT
)