package view;

import model.ChatMessage;
import model.Veterinarian;
import service.ChatMessageServiceClient;
import service.VeterinarianServiceClient;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.border.EmptyBorder;

public class ChatFrame extends JFrame {
    public ChatMessageServiceClient chatService;
    private VeterinarianServiceClient veterinarianService;
    private String currentUsername;
    private String selectedVetUsername;
    private JTextArea chatArea;
    private JTextField messageField;
    private Timer refreshTimer;
    private JComboBox<String> veterinarianCombo;
    String user;



    public ChatFrame(String currentUsername) {
        this.veterinarianService = new VeterinarianServiceClient();
        this.user = String.valueOf(veterinarianService.findVeterinarianByEmail(currentUsername));
        this.chatService = new ChatMessageServiceClient();
        this.currentUsername = currentUsername;

        setTitle("Chat Vétérinaire");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        // Panel principal avec bordure
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(236, 242, 255));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // Panel supérieur pour la sélection du vétérinaire
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        topPanel.setBackground(new Color(79, 129, 189));
        topPanel.setBorder(new EmptyBorder(5, 5, 5, 5));

        JLabel selectLabel = new JLabel("Chatter avec :");
        selectLabel.setForeground(Color.WHITE);
        selectLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        topPanel.add(selectLabel);

        veterinarianCombo = new JComboBox<>();
        veterinarianCombo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        loadVeterinarians();
        topPanel.add(veterinarianCombo);

        // Zone de chat
        chatArea = new JTextArea();
        chatArea.setEditable(false);
        chatArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chatArea.setLineWrap(true);
        chatArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(chatArea);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(79, 129, 189)));

        // Panel inférieur pour l'envoi de messages
        JPanel bottomPanel = new JPanel(new BorderLayout(5, 0));
        bottomPanel.setBackground(new Color(236, 242, 255));

        messageField = new JTextField();
        messageField.setFont(new Font("Segoe UI", Font.PLAIN, 12));

        JButton sendButton = new JButton("Envoyer");
        sendButton.setBackground(new Color(79, 129, 189));
        sendButton.setForeground(Color.WHITE);
        sendButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        sendButton.setFocusPainted(false);

        bottomPanel.add(messageField, BorderLayout.CENTER);
        bottomPanel.add(sendButton, BorderLayout.EAST);

        // Ajout des composants au panel principal
        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Gestionnaires d'événements
        veterinarianCombo.addActionListener(e -> {
            if (veterinarianCombo.getSelectedItem() != null) {
                selectedVetUsername = veterinarianCombo.getSelectedItem().toString();
                refreshChat();
            }
        });

        sendButton.addActionListener(e -> sendMessage());
        messageField.addActionListener(e -> sendMessage());

        // Timer pour rafraîchir le chat
        refreshTimer = new Timer(2000, e -> refreshChat());
        refreshTimer.start();
    }

    private void loadVeterinarians() {
        try {
            List<Veterinarian> vets = veterinarianService.findAllVeterinarians();
            veterinarianCombo.removeAllItems();

            if (vets == null || vets.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Aucun autre vétérinaire n'est disponible pour le chat.",
                        "Information",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }


            for (Veterinarian vet : vets) {

                System.out.println("******************** " + vet.getUsername());
                System.out.println("***currentUsername**"+ user);
                if (!vet.getUsername().equals(user)) {
                    veterinarianCombo.addItem(vet.getUsername());
                    System.out.println("Ajout du vétérinaire à la liste : " + vet.getUsername());
                }else {
                    System.out.println("Vet actuel "+vet.getUsername());
                }
            }

            if (veterinarianCombo.getItemCount() == 0) {
                JOptionPane.showMessageDialog(this,
                        "Aucun autre vétérinaire n'est disponible pour le chat.",
                        "Information",
                        JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception e) {
            System.err.println("Erreur lors du chargement des vétérinaires : " + e.getMessage());
            JOptionPane.showMessageDialog(this,
                    "Erreur lors du chargement des vétérinaires : " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void sendMessage() {
        String message = messageField.getText().trim();

        if (!message.isEmpty() && selectedVetUsername != null) {
            try {
                chatService.sendChatMessage(user, selectedVetUsername, message);
                messageField.setText("");
                refreshChat();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'envoi du message : " + e.getMessage());
            }
        }
    }

    private void refreshChat() {
        this.chatService = new ChatMessageServiceClient();
        if (selectedVetUsername != null) {
            System.out.println("SELECTED "+selectedVetUsername);
            try {
                List<ChatMessage> messages = chatService.getMessagesBetweenUsers(user, selectedVetUsername);

                for (ChatMessage message : messages) {
                    System.out.println("From: " + message.getSenderUsername() + ", To: " + message.getReceiverUsername() + ", Content: " + message.getContent());
                }
                updateChatArea(messages);
            } catch (Exception e) {
                System.err.println("Erreur lors du rafraîchissement du chat : " + e.getMessage());
            }
        }
    }

    private void updateChatArea(List<ChatMessage> messages) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        StringBuilder chat = new StringBuilder();

        for (ChatMessage message : messages) {
            String sender = message.getSenderUsername().equals(currentUsername) ? "Vous" : message.getSenderUsername();
            chat.append(String.format("[%s] %s : %s%n",
                    sdf.format(message.getTimestamp()),
                    sender,
                    message.getContent()));
        }

        chatArea.setText(chat.toString());
        chatArea.setCaretPosition(chatArea.getDocument().getLength());
    }

    @Override
    public void dispose() {
        refreshTimer.stop();
        super.dispose();
    }
}