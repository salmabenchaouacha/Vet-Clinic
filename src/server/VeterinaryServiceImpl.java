package server;

import model.*;
import rmi.VeterinaryService;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VeterinaryServiceImpl extends UnicastRemoteObject implements VeterinaryService {
    private Connection connection;



    @Override
    public List<Animal> getAllAnimals() throws RemoteException {
        List<Animal> animals = new ArrayList<>();
        String query = "SELECT * FROM animals";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
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







    // Exemple pour une recherche par nom



    @Override
    public void addOwner(Owner owner) throws RemoteException {
        // Séparation du full_name en first_name et last_name
        String[] names = splitFullName(owner.getFullName());

        String query = "INSERT INTO owners (first_name, last_name, phone, email) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, names[0]);
            stmt.setString(2, names[1]);
            stmt.setString(3, owner.getPhone());
            stmt.setString(4, owner.getEmail());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RemoteException("Erreur lors de l'ajout du propriétaire : " + e.getMessage());
        }
    }

    @Override
    public void updateOwner(Owner owner) throws RemoteException {
        // Séparation du full_name en first_name et last_name
        String[] names = splitFullName(owner.getFullName());

        String query = "UPDATE owners SET first_name = ?, last_name = ?, phone = ?, email = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, names[0]);
            stmt.setString(2, names[1]);
            stmt.setString(3, owner.getPhone());
            stmt.setString(4, owner.getEmail());
            stmt.setInt(5, owner.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RemoteException("Erreur lors de la mise à jour du propriétaire : " + e.getMessage());
        }
    }

    @Override
    public void deleteOwner(int id) throws RemoteException {

    }

    // Méthode utilitaire pour séparer le nom complet
    private String[] splitFullName(String fullName) {
        String[] names = fullName.split(" ", 2);
        if (names.length == 1) {
            return new String[]{names[0], ""}; // Si pas de nom de famille
        }
        return names;
    }

    @Override
    public List<Vaccination> getAllVaccinations() throws RemoteException {
        List<Vaccination> vaccinations = new ArrayList<>();
        String query = "SELECT * FROM vaccinations";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Vaccination vaccination = new Vaccination(
                        rs.getInt("id"),
                        rs.getInt("animal_id"),
                        rs.getString("vaccine_name"),
                        rs.getString("date")
                );
                vaccinations.add(vaccination);
            }
        } catch (SQLException e) {
            throw new RemoteException("Erreur lors de la récupération des vaccins : " + e.getMessage());
        }
        return vaccinations;
    }

    @Override
    public void addVaccination(Vaccination vaccination) throws RemoteException {
        String query = "INSERT INTO vaccinations (animal_id, vaccine_name, date) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, vaccination.getAnimalId());
            stmt.setString(2, vaccination.getVaccineName());
            stmt.setString(3, vaccination.getDate());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RemoteException("Erreur lors de l'ajout du vaccin : " + e.getMessage());
        }
    }

    @Override
    public void updateVaccination(Vaccination vaccination) throws RemoteException {
        String query = "UPDATE vaccinations SET animal_id = ?, vaccine_name = ?, date = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, vaccination.getAnimalId());
            stmt.setString(2, vaccination.getVaccineName());
            stmt.setString(3, vaccination.getDate());
            stmt.setInt(4, vaccination.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RemoteException("Erreur lors de la mise à jour du vaccin : " + e.getMessage());
        }
    }

    @Override
    public void deleteVaccination(int id) throws RemoteException {
        String query = "DELETE FROM vaccinations WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RemoteException("Erreur lors de la suppression du vaccin : " + e.getMessage());
        }
    }

    @Override
    public List<Visit> getAllVisits() throws RemoteException {
        List<Visit> visits = new ArrayList<>();
        String query = "SELECT * FROM visits";
        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Visit visit = new Visit(
                        rs.getInt("id"),
                        rs.getInt("animal_id"),
                        rs.getString("date"),
                        rs.getString("reason"),
                        rs.getString("notes")
                );
                visits.add(visit);
            }
        } catch (SQLException e) {
            throw new RemoteException("Erreur lors de la récupération des visites : " + e.getMessage());
        }
        return visits;
    }

    @Override
    public void addVisit(Visit visit) throws RemoteException {
        String query = "INSERT INTO visits (animal_id, date, reason, notes) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, visit.getAnimalId());
            stmt.setString(2, visit.getDate());
            stmt.setString(3, visit.getReason());
            stmt.setString(4, visit.getNotes());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RemoteException("Erreur lors de l'ajout de la visite : " + e.getMessage());
        }
    }

    @Override
    public void updateVisit(Visit visit) throws RemoteException {
        String query = "UPDATE visits SET animal_id = ?, date = ?, reason = ?, notes = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, visit.getAnimalId());
            stmt.setString(2, visit.getDate());
            stmt.setString(3, visit.getReason());
            stmt.setString(4, visit.getNotes());
            stmt.setInt(5, visit.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RemoteException("Erreur lors de la mise à jour de la visite : " + e.getMessage());
        }
    }

    @Override
    public void deleteVisit(int id) throws RemoteException {
        String query = "DELETE FROM visits WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RemoteException("Erreur lors de la suppression de la visite : " + e.getMessage());
        }
    }

    @Override
    public boolean signIn(String username, String password) throws RemoteException {
        String query = "SELECT * FROM veterinarians WHERE username = ? AND password = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RemoteException("Erreur lors de la connexion : " + e.getMessage());
        }
    }

    @Override
    public void signUp(Veterinarian veterinarian) throws RemoteException {
        String query = "INSERT INTO veterinarians (username, password, full_name, email, phone, photo_path) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, veterinarian.getUsername());
            stmt.setString(2, veterinarian.getPassword());
            stmt.setString(3, veterinarian.getFullName());
            stmt.setString(4, veterinarian.getEmail());
            stmt.setString(5, veterinarian.getPhone());
            stmt.setString(6, veterinarian.getPhotoPath());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RemoteException("Erreur lors de l'inscription : " + e.getMessage());
        }
    }

    @Override
    public Veterinarian getVeterinarianByUsername(String username) throws RemoteException {
        String query = "SELECT * FROM veterinarians WHERE username = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
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
                } else {
                    throw new RemoteException("Vétérinaire non trouvé avec le nom d'utilisateur : " + username);
                }
            }
        } catch (SQLException e) {
            throw new RemoteException("Erreur lors de la récupération du vétérinaire : " + e.getMessage());
        }
    }

    @Override
    public void updateVeterinarian(Veterinarian veterinarian) throws RemoteException {
        String query = "UPDATE veterinarians SET full_name = ?, email = ?, phone = ?, photo_path = ? WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, veterinarian.getFullName());
            stmt.setString(2, veterinarian.getEmail());
            stmt.setString(3, veterinarian.getPhone());
            stmt.setString(4, veterinarian.getPhotoPath());
            stmt.setInt(5, veterinarian.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RemoteException("Erreur lors de la mise à jour du vétérinaire : " + e.getMessage());
        }
    }

    @Override
    public List<Veterinarian> getAllVeterinarians() throws RemoteException {
        List<Veterinarian> vets = new ArrayList<>();
        String query = "SELECT * FROM veterinarians";
        
        try (PreparedStatement stmt = connection.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            
            System.out.println("Exécution de la requête : " + query);
            
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
                System.out.println("Vétérinaire trouvé : " + vet.getUsername());
            }
            
            System.out.println("Nombre total de vétérinaires : " + vets.size());
            return vets;
            
        } catch (SQLException e) {
            System.err.println("Erreur SQL : " + e.getMessage());
            throw new RemoteException("Erreur lors de la récupération des vétérinaires : " + e.getMessage());
        }
    }

    @Override
    public void sendChatMessage(String senderUsername, String receiverUsername, String content) throws RemoteException {
        String query = "INSERT INTO chat_messages (sender_username, receiver_username, content, timestamp, is_read) VALUES (?, ?, ?, NOW(), false)";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, senderUsername);
            stmt.setString(2, receiverUsername);
            stmt.setString(3, content);
            stmt.executeUpdate();
            
        } catch (SQLException e) {
            throw new RemoteException("Erreur lors de l'envoi du message", e);
        }
    }

    @Override
    public List<ChatMessage> getChatMessages(String user1, String user2) throws RemoteException {
        List<ChatMessage> messages = new ArrayList<>();
        String query = "SELECT * FROM chat_messages WHERE (sender_username = ? AND receiver_username = ?) " +
                      "OR (sender_username = ? AND receiver_username = ?) ORDER BY timestamp ASC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, user1);
            stmt.setString(2, user2);
            stmt.setString(3, user2);
            stmt.setString(4, user1);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ChatMessage message = new ChatMessage(
                        rs.getInt("id"),
                        rs.getString("sender_username"),
                        rs.getString("receiver_username"),
                        rs.getString("content")
                    );
                    message.setTimestamp(rs.getTimestamp("timestamp"));
                    message.setRead(rs.getBoolean("is_read"));
                    messages.add(message);
                }
            }
            
            // Marquer les messages comme lus
            String updateQuery = "UPDATE chat_messages SET is_read = true WHERE receiver_username = ? AND sender_username = ?";
            try (PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                updateStmt.setString(1, user1);
                updateStmt.setString(2, user2);
                updateStmt.executeUpdate();
            }
            
            return messages;
        } catch (SQLException e) {
            throw new RemoteException("Erreur lors de la récupération des messages", e);
        }
    }

    @Override
    public List<ChatMessage> getUnreadMessages(String username) throws RemoteException {
        List<ChatMessage> messages = new ArrayList<>();
        String query = "SELECT * FROM chat_messages WHERE receiver_username = ? AND is_read = false ORDER BY timestamp ASC";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, username);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ChatMessage message = new ChatMessage(
                        rs.getInt("id"),
                        rs.getString("sender_username"),
                        rs.getString("receiver_username"),
                        rs.getString("content")
                    );
                    message.setTimestamp(rs.getTimestamp("timestamp"));
                    message.setRead(false);
                    messages.add(message);
                }
            }
            return messages;
        } catch (SQLException e) {
            throw new RemoteException("Erreur lors de la récupération des messages non lus", e);
        }
    }
}