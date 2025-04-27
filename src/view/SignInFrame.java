package view;

import service.VeterinarianServiceClient;
import service.VisitServiceClient;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;

public class SignInFrame extends JFrame {
    private VeterinarianServiceClient service;

    public SignInFrame() {
        this.service = new VeterinarianServiceClient() ;
        setTitle("Connexion - Gestion Vétérinaire");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(240, 248, 255));

        // Panel pour le formulaire de connexion
        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(new Color(240, 248, 255));

        JLabel usernameLabel = new JLabel("Nom d'utilisateur :");
        JTextField usernameField = new JTextField();
        JLabel passwordLabel = new JLabel("Mot de passe :");
        JPasswordField passwordField = new JPasswordField();

        formPanel.add(usernameLabel);
        formPanel.add(usernameField);
        formPanel.add(passwordLabel);
        formPanel.add(passwordField);

        // Boutons de connexion et d'inscription
        JButton signInButton = new JButton("Se connecter");
        JButton signUpButton = new JButton("S'inscrire");
        styleButton(signInButton);
        styleButton(signUpButton);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(240, 248, 255));
        buttonPanel.add(signInButton);
        buttonPanel.add(signUpButton);

        add(formPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        // Action du bouton de connexion
        signInButton.addActionListener(e -> {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs", "Erreur", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                boolean authenticated = service.authenticateVeterinarian(username, password);
                if (authenticated) {
                    JOptionPane.showMessageDialog(this, "Connexion réussie !", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    new MainFrame( username).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "Nom d'utilisateur ou mot de passe incorrect", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la connexion : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Action du bouton d'inscription
        signUpButton.addActionListener(e -> new SignUpFrame().setVisible(true));
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(60, 179, 113));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }
}