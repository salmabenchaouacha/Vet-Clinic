package rmi;

import model.ChatMessage;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ChatMessageRemote extends Remote {
    public void sendChatMessage(String senderUsername, String receiverUsername, String content) throws Exception;

    List<ChatMessage> getMessagesBetweenUsers(String user1, String user2) throws RemoteException, Exception;

    List<ChatMessage> getUnreadMessages(String username) throws RemoteException, Exception;

    void markMessagesAsRead(String receiverUsername, String senderUsername) throws RemoteException, Exception;
}
