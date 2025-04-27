package dao;

import model.ChatMessage;
import util.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChatMessageDAOImpl implements ChatMessageDAO {

    @Override
    public void sendChatMessage(String senderUsername, String receiverUsername, String content) throws Exception {
        String query = "INSERT INTO chat_messages (sender_username, receiver_username, content, timestamp, is_read) VALUES (?, ?, ?, NOW(), false)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, senderUsername);
            stmt.setString(2, receiverUsername);
            stmt.setString(3, content);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new Exception("Erreur lors de l'envoi du message", e);
        }
    }

    @Override
    public List<ChatMessage> getMessagesBetweenUsers(String user1, String user2) throws Exception {
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
            return messages;
        } catch (SQLException e) {
            throw new Exception("Erreur lors de la récupération des messages", e);
        }
    }

    @Override
    public List<ChatMessage> getUnreadMessages(String username) throws Exception {
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
            throw new Exception("Erreur lors de la récupération des messages non lus", e);
        }
    }

    @Override
    public void markMessagesAsRead(String receiverUsername, String senderUsername) throws Exception {
        String query = "UPDATE chat_messages SET is_read = true WHERE receiver_username = ? AND sender_username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, receiverUsername);
            stmt.setString(2, senderUsername);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new Exception("Erreur lors de la mise à jour des messages", e);
        }
    }
}