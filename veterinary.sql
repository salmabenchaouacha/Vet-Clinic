CREATE DATABASE IF NOT EXISTS veterinary_db;
USE veterinary_db;

CREATE TABLE IF NOT EXISTS owners (
    id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    phone VARCHAR(20),
    email VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS animals (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    species VARCHAR(255) NOT NULL,
    breed VARCHAR(255),
    age VARCHAR(50),
    sex VARCHAR(50),
    chip_number VARCHAR(255),
    owner_id INT,
    FOREIGN KEY (owner_id) REFERENCES owners(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS vaccinations (
    id INT AUTO_INCREMENT PRIMARY KEY,
    animal_id INT,
    vaccine_name VARCHAR(255) NOT NULL,
    date VARCHAR(10) NOT NULL,
    FOREIGN KEY (animal_id) REFERENCES animals(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS visits (
    id INT AUTO_INCREMENT PRIMARY KEY,
    animal_id INT,
    date VARCHAR(10) NOT NULL,
    reason VARCHAR(255) NOT NULL,
    notes TEXT,
    FOREIGN KEY (animal_id) REFERENCES animals(id) ON DELETE CASCADE
);