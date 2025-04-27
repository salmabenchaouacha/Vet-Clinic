package dao;

import model.ChatMessage;
import java.util.List;

public interface ChatMessageDAO {
    public void sendChatMessage(String senderUsername, String receiverUsername, String content) throws Exception;
    List<ChatMessage> getMessagesBetweenUsers(String user1, String user2) throws Exception;
    List<ChatMessage> getUnreadMessages(String username) throws Exception;
    void markMessagesAsRead(String receiverUsername, String senderUsername) throws Exception;
}