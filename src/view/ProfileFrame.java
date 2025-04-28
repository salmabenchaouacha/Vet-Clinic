package view;

import model.Veterinarian;
import service.VeterinarianServiceClient;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ProfileFrame extends JFrame {
    private VeterinarianServiceClient service;
    private Veterinarian veterinarian;
    private JTextField fullNameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JTextField photoPathField;

    public ProfileFrame(Veterinarian veterinarian, MainFrame mainFrame) {
        this.service = new VeterinarianServiceClient();
        this.veterinarian = veterinarian;
        setTitle("Profil du Vétérinaire");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(new Color(240, 248, 255));

        // Photo panel
        JPanel photoPanel = new JPanel();
        photoPanel.setBackground(new Color(240, 248, 255));
        JLabel photoLabel = new JLabel();
        try {
            BufferedImage originalImage = veterinarian.getPhotoPath() != null ? ImageIO.read(new File(veterinarian.getPhotoPath())) : null;
            if (originalImage != null) {
                int size = 100;
                BufferedImage circularImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = circularImage.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setClip(new java.awt.geom.Ellipse2D.Float(0, 0, size, size));
                g2.drawImage(originalImage.getScaledInstance(size, size, Image.SCALE_SMOOTH), 0, 0, null);
                g2.dispose();
                photoLabel.setIcon(new ImageIcon(circularImage));
            } else {
                photoLabel.setText("Pas de photo");
            }
        } catch (IOException e) {
            photoLabel.setText("Erreur photo");
        }
        photoPanel.add(photoLabel);
        add(photoPanel, BorderLayout.NORTH);

        // Form panel
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        formPanel.setBackground(new Color(240, 248, 255));

        JLabel fullNameLabel = new JLabel("Nom complet :");
        fullNameField = new JTextField(veterinarian.getFullName());
        JLabel emailLabel = new JLabel("Email :");
        emailField = new JTextField(veterinarian.getEmail());
        JLabel phoneLabel = new JLabel("Téléphone :");
        phoneField = new JTextField(veterinarian.getPhone() != null ? veterinarian.getPhone() : "");
        JLabel photoPathLabel = new JLabel("Photo :");
        photoPathField = new JTextField(veterinarian.getPhotoPath() != null ? veterinarian.getPhotoPath() : "");
        JButton browseButton = new JButton("Parcourir");

        formPanel.add(fullNameLabel);
        formPanel.add(fullNameField);
        formPanel.add(emailLabel);
        formPanel.add(emailField);
        formPanel.add(phoneLabel);
        formPanel.add(phoneField);
        formPanel.add(photoPathLabel);
        JPanel photoInputPanel = new JPanel(new BorderLayout());
        photoInputPanel.add(photoPathField, BorderLayout.CENTER);
        photoInputPanel.add(browseButton, BorderLayout.EAST);
        formPanel.add(photoInputPanel);

        add(formPanel, BorderLayout.CENTER);

        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(240, 248, 255));
        JButton saveButton = new JButton("Enregistrer");
        JButton logoutButton = new JButton("Déconnexion");
        styleButton(saveButton, new Color(60, 179, 113));
        styleButton(logoutButton, new Color(255, 69, 58));
        buttonPanel.add(saveButton);
        buttonPanel.add(logoutButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Browse button action
        browseButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                photoPathField.setText(selectedFile.getAbsolutePath());
            }
        });

        // Save button action
        saveButton.addActionListener(e -> {
            try {
                veterinarian.setFullName(fullNameField.getText().trim());
                veterinarian.setEmail(emailField.getText().trim());
                veterinarian.setPhone(phoneField.getText().trim());
                veterinarian.setPhotoPath(photoPathField.getText().trim());

                service.updateVeterinarian(veterinarian);

                JOptionPane.showMessageDialog(this, "Profil mis à jour avec succès !", "Succès", JOptionPane.INFORMATION_MESSAGE);
                dispose();
                mainFrame.dispose();
                new MainFrame(veterinarian.getUsername()).setVisible(true);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors de la mise à jour du profil : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Logout button action
        logoutButton.addActionListener(e -> {
            dispose();
            mainFrame.dispose();
            try {
                VeterinarianServiceClient serviceClient = new VeterinarianServiceClient();
                new SignInFrame(serviceClient).setVisible(true);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }
}
