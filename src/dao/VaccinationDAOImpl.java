package dao;

import model.Vaccination;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VaccinationDAOImpl implements VaccinationDAO{

    @Override
    public List<Vaccination> getAllVaccinations() throws Exception {
        List<Vaccination> vaccinations = new ArrayList<>();
        String query = "SELECT * FROM vaccinations";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                vaccinations.add(new Vaccination(
                        rs.getInt("id"),
                        rs.getInt("animal_id"),
                        rs.getString("vaccine_name"),
                        rs.getString("date")
                ));
            }
        }
        return vaccinations;
    }

    @Override
    public void addVaccination(Vaccination vaccination) throws Exception {
        String query = "INSERT INTO vaccinations (animal_id, vaccine_name, date) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, vaccination.getAnimalId());
            stmt.setString(2, vaccination.getVaccineName());
            stmt.setString(3, vaccination.getDate());
            stmt.executeUpdate();
        }
    }

    @Override
    public void updateVaccination(Vaccination vaccination) throws Exception {
        String query = "UPDATE vaccinations SET animal_id = ?, vaccine_name = ?, date = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, vaccination.getAnimalId());
            stmt.setString(2, vaccination.getVaccineName());
            stmt.setString(3, vaccination.getDate());
            stmt.setInt(4, vaccination.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteVaccination(int id) throws Exception {
        String query = "DELETE FROM vaccinations WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
