package dao;

import model.Animal;
import model.Owner;
import util.DatabaseConnection;

import java.rmi.RemoteException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnimalDAOImpl implements AnimalDAO{

    @Override
    public void addAnimal(Animal animal) throws Exception {
        String sql = "INSERT INTO animals (name, species, breed, age, sex, chip_number, ownerId, photo_path) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, animal.getName());
            stmt.setString(2, animal.getSpecies());
            stmt.setString(3, animal.getBreed());
            stmt.setString(4, animal.getAge());
            stmt.setString(5, animal.getSex());
            stmt.setString(6, animal.getChipNumber());
            stmt.setInt(7, animal.getOwnerId());
            stmt.setString(8, animal.getPhotoPath());
            stmt.executeUpdate();
        }
    }

    @Override
    public List<Animal> getAllAnimalsByOwner() throws Exception {
        List<Animal> animals = new ArrayList<>();
        String sql = "SELECT a.*, o.full_name FROM animals a JOIN owners o ON a.ownerId = o.id";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                animals.add(new Animal(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("species"),
                        rs.getString("breed"),
                        rs.getString("age"),
                        rs.getString("sex"),
                        rs.getString("chip_number"),
                        rs.getInt("ownerId"),
                        rs.getString("photo_path")
                ));
            }
        }
        return animals;
    }

    @Override
    public void updateAnimal(Animal animal) throws Exception {
        String sql = "UPDATE animals SET name = ?, species = ?, breed = ?, age = ?, sex = ?, chip_number = ?, ownerId = ?, photo_path = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, animal.getName());
            stmt.setString(2, animal.getSpecies());
            stmt.setString(3, animal.getBreed());
            stmt.setString(4, animal.getAge());
            stmt.setString(5, animal.getSex());
            stmt.setString(6, animal.getChipNumber());
            stmt.setInt(7, animal.getOwnerId());
            stmt.setString(8, animal.getPhotoPath());
            stmt.setInt(9, animal.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteAnimal(int id) throws Exception {
        String sql = "DELETE FROM animals WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }


    @Override
    public List<Animal> getAllAnimals() throws RemoteException {
        List<Animal> animals = new ArrayList<>();
        String query = "SELECT * FROM animals";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                Animal animal = new Animal(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("species"),
                        rs.getString("breed"),
                        rs.getString("age"),
                        rs.getString("sex"),
                        rs.getString("chip_number"),
                        rs.getInt("owner_id"),
                        rs.getString("photoPath")
                );
                animals.add(animal);
            }
        } catch (SQLException e) {
            throw new RemoteException("Erreur lors de la récupération des animaux : " + e.getMessage());
        }
        return animals;
    }
}
