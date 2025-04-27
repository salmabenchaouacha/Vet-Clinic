package rmi;

import dao.ChatMessageDAO;
import dao.ChatMessageDAOImpl;
import model.ChatMessage;

import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class ChatMessageRemoteImpl extends UnicastRemoteObject implements ChatMessageRemote {

    private ChatMessageDAO chatMessageDAO;

    public ChatMessageRemoteImpl() throws Exception {
        super();
        chatMessageDAO = new ChatMessageDAOImpl();
    }

    @Override
    public void sendChatMessage(String senderUsername, String receiverUsername, String content) throws Exception {
        chatMessageDAO.sendChatMessage(senderUsername, receiverUsername, content);
    }

    @Override
    public List<ChatMessage> getMessagesBetweenUsers(String user1, String user2) throws Exception {
        return chatMessageDAO.getMessagesBetweenUsers(user1, user2);
    }

    @Override
    public List<ChatMessage> getUnreadMessages(String username) throws Exception {
        return chatMessageDAO.getUnreadMessages(username);
    }

    @Override
    public void markMessagesAsRead(String receiverUsername, String senderUsername) throws Exception {
        chatMessageDAO.markMessagesAsRead(receiverUsername, senderUsername);
    }
}
