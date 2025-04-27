package service;

import rmi.ChatMessageRemote;
import model.ChatMessage;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.List;

public class ChatMessageServiceClient {
    private ChatMessageRemote chatMessageService;

    public ChatMessageServiceClient() {
        try {
            chatMessageService = (ChatMessageRemote) Naming.lookup("rmi://localhost:1099/chatMessageService");
            System.out.println("Connected to ChatMessageService");
        } catch (Exception e) {
            System.err.println("Error connecting to ChatMessageRemote: " + e.getMessage());
        }
    }

    public void sendChatMessage(String senderUsername, String receiverUsername, String content) {
        try {
            chatMessageService.sendChatMessage(senderUsername, receiverUsername, content);
            System.out.println("Message saved successfully.");
        } catch (RemoteException e) {
            System.err.println("RemoteException during save: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Exception during save: " + e.getMessage());
        }
    }

    public List<ChatMessage> getMessagesBetweenUsers(String user1, String user2) {
        try {
            return chatMessageService.getMessagesBetweenUsers(user1, user2);
        } catch (RemoteException e) {
            System.err.println("RemoteException during getMessagesBetweenUsers: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Exception during getMessagesBetweenUsers: " + e.getMessage());
            return null;
        }
    }

    public List<ChatMessage> getUnreadMessages(String username) {
        try {
            return chatMessageService.getUnreadMessages(username);
        } catch (RemoteException e) {
            System.err.println("RemoteException during getUnreadMessages: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Exception during getUnreadMessages: " + e.getMessage());
            return null;
        }
    }

    public boolean markMessagesAsRead(String receiverUsername, String senderUsername) {
        try {
            chatMessageService.markMessagesAsRead(receiverUsername, senderUsername);
            System.out.println("Messages marked as read.");
            return true;
        } catch (RemoteException e) {
            System.err.println("RemoteException during markMessagesAsRead: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Exception during markMessagesAsRead: " + e.getMessage());
            return false;
        }
    }
}
