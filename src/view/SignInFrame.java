package view;

import service.VeterinarianServiceClient;

import javax.swing.*;
import java.awt.*;
import java.rmi.RemoteException;

public class SignInFrame extends JFrame {
    private VeterinarianServiceClient service;

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
                g2.setFont(getFont().deriveFont(Font.PLAIN).deriveFont(12f));
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(placeholder)) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(placeholder, x, y);
                g2.dispose();
            }
        }
    }

    private static class PlaceholderPasswordField extends JPasswordField {
        private String placeholder;
        private Color placeholderColor = new Color(171, 180, 199);

        public PlaceholderPasswordField(String placeholder) {
            this.placeholder = placeholder;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            if (getPassword().length == 0) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(placeholderColor);
                g2.setFont(getFont().deriveFont(Font.PLAIN).deriveFont(12f));
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(placeholder)) / 2;
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();
                g2.drawString(placeholder, x, y);
                g2.dispose();
            }
        }
    }

    public SignInFrame(  ) {
        this.service = new VeterinarianServiceClient();
        setTitle("Connexion - Clinique Vétérinaire 🐾");
        setSize(420, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(243, 248, 255)); // Couleur pastel

        // Titre
        JLabel titleLabel = new JLabel("Bienvenue à la Clinique Vétérinaire 🐾", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Serif", Font.BOLD, 20));
        titleLabel.setForeground(new Color(35, 35, 110));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 10, 10));
        add(titleLabel, BorderLayout.NORTH);

        // Panel principal
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(243, 248, 255));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 30, 20, 30));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST; // Alignement à gauche des labels

        // Email
        JLabel emailLabel = new JLabel("📧 Email :");
        emailLabel.setHorizontalAlignment(SwingConstants.LEFT);
        gbc.gridx = 0;
        gbc.gridy = 0;
        mainPanel.add(emailLabel, gbc);

        JTextField emailField = new PlaceholderTextField("Entrez votre email");
        styleTextField(emailField);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER; // Champ centré
        mainPanel.add(emailField, gbc);

        // Mot de passe
        JLabel passwordLabel = new JLabel("🔒 Mot de passe :");
        passwordLabel.setHorizontalAlignment(SwingConstants.LEFT);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        mainPanel.add(passwordLabel, gbc);

        JPasswordField passwordField = new PlaceholderPasswordField("Entrez votre mot de passe");
        styleTextField(passwordField);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(passwordField, gbc);

        // Panel boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(243, 248, 255));

        JButton signInButton = new JButton("🚪 Se connecter");
        JButton signUpButton = new JButton("📝 S'inscrire");

        styleButton(signInButton, new Color(202, 255, 141));
        styleButton(signUpButton, new Color(206, 215, 255));

        buttonPanel.add(signInButton);
        buttonPanel.add(signUpButton);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.CENTER;
        mainPanel.add(buttonPanel, gbc);

        add(mainPanel, BorderLayout.CENTER);

        // Action bouton connexion
        signInButton.addActionListener(e -> {
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();

            if (email.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "⚠️ Veuillez remplir tous les champs", "Erreur", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Vérification basique de l'email
            if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                JOptionPane.showMessageDialog(this, "⚠️ Veuillez entrer une adresse email valide", "Erreur", JOptionPane.WARNING_MESSAGE);
                return;
            }

            try {
                boolean authenticated = service.authenticateVeterinarian(email, password);
                if (authenticated) {
                    JOptionPane.showMessageDialog(this, "✅ Connexion réussie !", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    dispose();
                    new MainFrame( email).setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(this, "❌ Email ou mot de passe incorrect", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ Erreur lors de la connexion : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Action bouton inscription
        signUpButton.addActionListener(e -> new SignUpFrame().setVisible(true));
    }

    private void styleTextField(JTextField field) {
        field.setPreferredSize(new Dimension(200, 30));
        field.setFont(new Font("Arial", Font.PLAIN, 14));
        field.setHorizontalAlignment(JTextField.CENTER); // Texte centré dans l'input
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
