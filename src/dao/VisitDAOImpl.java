package dao;

import model.Visit;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class VisitDAOImpl implements VisitDAO{

    @Override
    public List<Visit> getAllVisits() throws Exception {
        List<Visit> visits = new ArrayList<>();
        String query = "SELECT * FROM visits";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                visits.add(new Visit(
                        rs.getInt("id"),
                        rs.getInt("animal_id"),
                        rs.getString("date"),
                        rs.getString("reason"),
                        rs.getString("notes")
                ));
            }
        }
        return visits;
    }

    @Override
    public void addVisit(Visit visit) throws Exception {
        String query = "INSERT INTO visits (animal_id, date, reason, notes) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, visit.getAnimalId());
            stmt.setString(2, visit.getDate());
            stmt.setString(3, visit.getReason());
            stmt.setString(4, visit.getNotes());
            stmt.executeUpdate();
        }
    }

    @Override
    public void updateVisit(Visit visit) throws Exception {
        String query = "UPDATE visits SET animal_id = ?, date = ?, reason = ?, notes = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, visit.getAnimalId());
            stmt.setString(2, visit.getDate());
            stmt.setString(3, visit.getReason());
            stmt.setString(4, visit.getNotes());
            stmt.setInt(5, visit.getId());
            stmt.executeUpdate();
        }
    }

    @Override
    public void deleteVisit(int id) throws Exception {
        String query = "DELETE FROM visits WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
