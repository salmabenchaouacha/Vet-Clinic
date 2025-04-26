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
    public List<Owner> searchOwnersByLastName(String lastName) throws Exception {
        List<Owner> owners = new ArrayList<>();
        String query = "SELECT id, CONCAT(first_name, ' ', last_name) AS full_name, phone, email " +
                "FROM owners WHERE last_name LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, "%" + lastName + "%");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Owner owner = new Owner(
                        rs.getInt("id"),
                        rs.getString("full_name"),
                        rs.getString("phone"),
                        rs.getString("email")
                );
                owners.add(owner);
            }
        } catch (SQLException e) {
            throw new Exception("Erreur lors de la recherche des propriétaires : " + e.getMessage());
        }
        return owners;
    }

    @Override
    public List<Owner> getAllOwners() throws Exception {
        List<Owner> owners = new ArrayList<>();
        String query = "SELECT id, CONCAT(first_name, ' ', last_name) AS full_name, phone, email FROM owners";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Owner owner = new Owner(
                        rs.getInt("id"),
                        rs.getString("full_name"),
                        rs.getString("phone"),
                        rs.getString("email")
                );
                owners.add(owner);
            }
        } catch (SQLException e) {
            throw new RemoteException("Erreur lors de la récupération des propriétaires : " + e.getMessage());
        }
        return owners;
    }
}
