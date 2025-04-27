package dao;

import model.Veterinarian;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VeterinarianDAOImpl implements VeterinarianDAO {
    @Override
    public void save(Veterinarian veterinarian) throws Exception {
        String query = "INSERT INTO veterinarians (username, password, full_name, email, phone, photo_path) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, veterinarian.getUsername());
            stmt.setString(2, veterinarian.getPassword());
            stmt.setString(3, veterinarian.getFullName());
            stmt.setString(4, veterinarian.getEmail());
            stmt.setString(5, veterinarian.getPhone());
            stmt.setString(6, veterinarian.getPhotoPath());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Erreur lors de l'inscription", e);
        }
    }

    @Override
    public void update(Veterinarian veterinarian) throws Exception {
        String query = "UPDATE veterinarians SET full_name = ?, email = ?, phone = ?, photo_path = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, veterinarian.getFullName());
            stmt.setString(2, veterinarian.getEmail());
            stmt.setString(3, veterinarian.getPhone());
            stmt.setString(4, veterinarian.getPhotoPath());
            stmt.setInt(5, veterinarian.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Erreur lors de la mise à jour du vétérinaire", e);
        }
    }

    @Override
    public Veterinarian findByUsername(String username) throws Exception {
        String query = "SELECT * FROM veterinarians WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Veterinarian(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getString("full_name"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            rs.getString("photo_path")
                    );
                }
                throw new Exception("Vétérinaire non trouvé avec le nom d'utilisateur : " + username);
            }
        } catch (SQLException e) {
            throw new Exception("Erreur lors de la récupération du vétérinaire", e);
        }
    }

    @Override
    public List<Veterinarian> findAll() throws Exception {
        List<Veterinarian> vets = new ArrayList<>();
        String query = "SELECT * FROM veterinarians";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Veterinarian vet = new Veterinarian(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("full_name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("photo_path")
                );
                vets.add(vet);
            }
            return vets;
        } catch (SQLException e) {
            throw new Exception("Erreur lors de la récupération des vétérinaires", e);
        }
    }

    @Override
    public boolean authenticate(String username, String password) throws Exception {
        String query = "SELECT * FROM veterinarians WHERE username = ? AND password = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new Exception("Erreur lors de la connexion", e);
        }
    }
}
