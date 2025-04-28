package dao;

import model.Owner;
import util.DatabaseConnection;

import java.rmi.RemoteException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OwnerDAOImpl implements OwnerDAO{

    @Override
    public void addOwner(Owner owner) throws Exception {
        String sql = "INSERT INTO owners (full_name, phone, email) VALUES ( ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, owner.getFullName());

            stmt.setString(2, owner.getPhone());
            stmt.setString(3, owner.getEmail());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("L'ajout du propriétaire a échoué, aucune ligne affectée.");
            }

        }
    }

    @Override
    public List<Owner> getAllOwners() throws Exception {
        List<Owner> owners = new ArrayList<>();
        String sql = "SELECT id,  full_name, phone, email FROM owners";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                owners.add(new Owner(
                        rs.getInt("id"),
                        rs.getString("full_name"),
                        rs.getString("phone"),
                        rs.getString("email")
                ));
            }
        }
        return owners;
    }

    @Override
    public void updateOwner(Owner owner) throws Exception {
        String sql = "UPDATE owners SET full_name = ?, phone = ?, email = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, owner.getFullName());

            stmt.setString(2, owner.getPhone());
            stmt.setString(3, owner.getEmail());
            stmt.setInt(4, owner.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteOwner(int id) throws Exception {
        String sql = "DELETE FROM owners WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    @Override
    public Owner getOwnerById(int id) throws Exception {
        String sql = "SELECT id, full_name,  phone, email FROM owners WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Owner(
                            rs.getInt("id"),
                            rs.getString("full_name"),

                            rs.getString("phone"),
                            rs.getString("email")
                    );
                }
            }
        }
        return null;
    }

    @Override
    public List<Owner> searchOwnersByLastName(String lastName) throws Exception {
        List<Owner> owners = new ArrayList<>();
        String query = "SELECT id, full_name, phone, email " +
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

}
