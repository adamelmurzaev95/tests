CREATE TABLE groups(
    id SERIAL NOT NULL PRIMARY KEY,
    specialization VARCHAR(128) NOT NULL,
    education_start_year INTEGER NOT NULL
)