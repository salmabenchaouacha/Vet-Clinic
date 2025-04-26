-- Création de la table si elle n'existe pas
CREATE TABLE IF NOT EXISTS veterinarians (
    id INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    phone VARCHAR(20),
    photo_path VARCHAR(255)
);

-- Insertion de quelques vétérinaires de test
INSERT INTO veterinarians (username, password, full_name, email, phone) VALUES
('dr.sophie', '123456', 'Dr. Sophie Martin', 'sophie.martin@vet.com', '0612345678'),
('dr.thomas', '123456', 'Dr. Thomas Dubois', 'thomas.dubois@vet.com', '0623456789'),
('dr.marie', '123456', 'Dr. Marie Lambert', 'marie.lambert@vet.com', '0634567890'),
('dr.pierre', '123456', 'Dr. Pierre Bernard', 'pierre.bernard@vet.com', '0645678901');

-- Mise à jour des mots de passe si nécessaire
-- UPDATE veterinarians SET password = '123456' WHERE password IS NULL; 