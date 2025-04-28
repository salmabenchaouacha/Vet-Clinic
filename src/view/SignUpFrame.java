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

    // PlaceholderTextField class from SignInFrame
    private static class PlaceholderTextField extends JTextField {
        private String placeholder;
        private Color placeholderColor = new Color(171, 180, 199, 255); // Bleu très clair

        public PlaceholderTextField(String placeholder) {
            this.placeholder = placeholder;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (getText().isEmpty()) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(placeholderColor);
                g2.setFont(getFont().deriveFont(Font.PLAIN).deriveFont(12f)); // Texte fin
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(placeholder)) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(placeholder, x, y);
                g2.dispose();
            }
        }
    }

    // PlaceholderPasswordField class from SignInFrame
    private static class PlaceholderPasswordField extends JPasswordField {
        private String placeholder;
        private Color placeholderColor = new Color(171, 180, 199); // Bleu très clair

        public PlaceholderPasswordField(String placeholder) {
            this.placeholder = placeholder;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (getPassword().length == 0) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(placeholderColor);
                g2.setFont(getFont().deriveFont(Font.PLAIN).deriveFont(12f)); // Texte fin
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(placeholder)) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(placeholder, x, y);
                g2.dispose();
            }
        }
    }

    public SignUpFrame() {
        this.service = new VeterinarianServiceClient();
        setTitle("Inscription Vétérinaire 🐾");
        setSize(420, 450); // Frame width remains 420px
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(243, 248, 255)); // Pastel beige

        // Titre de bienvenue
        JLabel titleLabel = new JLabel("Inscription à la Clinique Vétérinaire 🐾", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 20));
        titleLabel.setForeground(new Color(35, 35, 110)); // Bleu pastel doux
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        add(titleLabel, BorderLayout.NORTH);

        // Panel principal pour le formulaire
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(243, 248, 255));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 5, 20, 5)); // Minimal side margins

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.WEST;

        // Nom d'utilisateur
        JLabel usernameLabel = new JLabel("👤 Nom d'utilisateur :");
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0; // Label column doesn't expand
        mainPanel.add(usernameLabel, gbc);

        JTextField usernameField = new PlaceholderTextField("Entrez votre nom d'utilisateur");
        styleTextField(usernameField);
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1; // Input column expands to take available space
        gbc.fill = GridBagConstraints.NONE; // Use preferred size
        mainPanel.add(usernameField, gbc);

        // Mot de passe
        JLabel passwordLabel = new JLabel("🔒 Mot de passe :");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(passwordLabel, gbc);

        JPasswordField passwordField = new PlaceholderPasswordField("Entrez votre mot de passe");
        styleTextField(passwordField);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(passwordField, gbc);

        // Nom complet
        JLabel fullNameLabel = new JLabel("📛 Nom complet :");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(fullNameLabel, gbc);

        JTextField fullNameField = new PlaceholderTextField("Entrez votre nom complet");
        styleTextField(fullNameField);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(fullNameField, gbc);

        // Email
        JLabel emailLabel = new JLabel("📧 Email :");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(emailLabel, gbc);

        JTextField emailField = new PlaceholderTextField("Entrez votre email");
        styleTextField(emailField);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(emailField, gbc);

        // Téléphone
        JLabel phoneLabel = new JLabel("📱 Téléphone :");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(phoneLabel, gbc);

        JTextField phoneField = new PlaceholderTextField("Entrez votre numéro");
        styleTextField(phoneField);
        gbc.gridx = 1;
        gbc.gridy = 4;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(phoneField, gbc);

        // Photo
        JLabel photoLabel = new JLabel("📸 Photo :");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(photoLabel, gbc);

        photoPathField = new PlaceholderTextField("Sélectionnez une photo");
        styleTextField(photoPathField);
        JButton browseButton = new JButton("Parcourir");
        styleButton(browseButton, new Color(254, 224, 230)); // Rose pastel
        JPanel photoPanel = new JPanel(new BorderLayout(5, 0));
        photoPanel.setBackground(new Color(243, 248, 255));
        photoPanel.add(photoPathField, BorderLayout.CENTER);
        photoPanel.add(browseButton, BorderLayout.EAST);
        gbc.gridx = 1;
        gbc.gridy = 5;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.NONE;
        mainPanel.add(photoPanel, gbc);

        // Bouton d'inscription
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(243, 248, 255));
        JButton signUpButton = new JButton("📝 S'inscrire");
        styleButton(signUpButton, new Color(202, 255, 141)); // Vert pastel
        buttonPanel.add(signUpButton);
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(buttonPanel, gbc);

        add(mainPanel, BorderLayout.CENTER);

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
                JOptionPane.showMessageDialog(this, "⚠️ Veuillez remplir tous les champs obligatoires", "Erreur", JOptionPane.WARNING_MESSAGE);
                return;
            }

            Veterinarian vet = new Veterinarian(0, username, password, fullName, email, phone, photoPath.isEmpty() ? null : photoPath);
            try {
                service.saveVeterinarian(vet);
                JOptionPane.showMessageDialog(this, "✅ Inscription réussie ! Vous pouvez maintenant vous connecter.", "Succès", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Erreur lors de l'inscription : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void styleTextField(JTextField field) {
        field.setPreferredSize(new Dimension(250, 30)); // Set preferred width to 300px
        field.setMinimumSize(new Dimension(250, 30));   // Ensure minimum width is also 300px
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setHorizontalAlignment(JTextField.CENTER);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(173, 216, 230), 2),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
    }

    private void styleButton(JButton button, Color backgroundColor) {
        button.setBackground(backgroundColor);
        button.setForeground(Color.BLACK);
        button.setFont(new Font("SansSerif", Font.BOLD, 13));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.WHITE, 2),
                BorderFactory.createEmptyBorder(6, 15, 6, 15)
        ));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
}