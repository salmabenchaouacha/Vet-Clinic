package view;

import model.Veterinarian;
import service.VeterinarianServiceClient;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.rmi.RemoteException;

public class SignUpFrame extends JFrame {
    private VeterinarianServiceClient service;
    private JTextField photoPathField;

    public SignUpFrame() {
        this.service = new VeterinarianServiceClient();
        setTitle("Inscription - Gestion Vétérinaire");
        setSize(400, 450); // Augmenter la taille pour le champ photo
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(240, 248, 255));

        // Panel pour le formulaire d'inscription
        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10)); // Ajouter une ligne pour la photo
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(new Color(240, 248, 255));

        JLabel usernameLabel = new JLabel("Nom d'utilisateur :");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Mot de passe :");
        JPasswordField passwordField = new JPasswordField();
        JLabel fullNameLabel = new JLabel("Nom complet :");
        JTextField fullNameField = new JTextField();
        JLabel emailLabel = new JLabel("Email :");
        JTextField emailField = new JTextField();
        JLabel phoneLabel = new JLabel("Téléphone :");
        JTextField phoneField = new JTextField();
        JLabel photoLabel = new JLabel("Photo :");
        photoPathField = new JTextField();
        JButton browseButton = new JButton("Parcourir");

        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);
        formPanel.add(fullNameLabel);
        formPanel.add(fullNameField);
        formPanel.add(emailLabel);
        formPanel.add(emailField);
        formPanel.add(phoneLabel);
        formPanel.add(phoneField);
        formPanel.add(photoLabel);
        JPanel photoPanel = new JPanel(new BorderLayout());
        photoPanel.add(photoPathField, BorderLayout.CENTER);
        photoPanel.add(browseButton, BorderLayout.EAST);
        formPanel.add(photoPanel);

        // Bouton d'inscription
        JButton signUpButton = new JButton("S'inscrire");
        styleButton(signUpButton);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(240, 248, 255));
        buttonPanel.add(signUpButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Action du bouton Parcourir
        browseButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                photoPathField.setText(selectedFile.getAbsolutePath());
            }
        });

        // Action du bouton d'inscription
        signUpButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String fullName = fullNameField.getText().trim();
            String email = emailField.getText().trim();
            String phone = phoneField.getText().trim();
            String photoPath = photoPathField.getText().trim();

            if (username.isEmpty() || password.isEmpty() || fullName.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs obligatoires", "Erreur", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Veterinarian vet = new Veterinarian(0, username, password, fullName, email, phone, photoPath.isEmpty() ? null : photoPath);
            try {
                service.saveVeterinarian(vet);
                JOptionPane.showMessageDialog(this, "Inscription réussie ! Vous pouvez maintenant vous connecter.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de l'inscription : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(60, 179, 113));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }
}