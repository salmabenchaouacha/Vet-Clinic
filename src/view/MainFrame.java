package view;

import model.Veterinarian;
import service.VeterinarianServiceClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import javax.imageio.ImageIO;

public class MainFrame extends JFrame {
    private VeterinarianServiceClient client;

    private String loggedInUsername;

    public MainFrame(String loggedInUsername) {
        this.client = new VeterinarianServiceClient();
        this.loggedInUsername = loggedInUsername;
        setTitle("Gestion Vétérinaire");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(236, 242, 255)); // Fond bleu très clair

        // Récupérer les informations du vétérinaire connecté
        Veterinarian vet = null;
        try {
            vet = client.findVeterinarianByUsername(loggedInUsername);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la récupération du profil : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        // Créer un panneau pour le profil en haut à droite
        JPanel profilePanel = new JPanel();
        profilePanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        profilePanel.setBackground(new Color(79, 129, 189)); // Bleu professionnel
        profilePanel.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 25));

        if (vet != null) {
            // Charger et afficher la photo ronde
            JLabel photoLabel = new JLabel();
            try {
                BufferedImage originalImage = vet.getPhotoPath() != null ? ImageIO.read(new File(vet.getPhotoPath())) : null;
                if (originalImage != null) {
                    int size = 45;
                    // Redimensionner l'image avec une meilleure qualité
                    BufferedImage resizedImage = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
                    Graphics2D g2d = resizedImage.createGraphics();
                    g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                    g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    // Calculer le facteur de mise à l'échelle
                    double scale = Math.min((double) size / originalImage.getWidth(), (double) size / originalImage.getHeight());
                    int scaledWidth = (int) (originalImage.getWidth() * scale);
                    int scaledHeight = (int) (originalImage.getHeight() * scale);
                    int x = (size - scaledWidth) / 2;
                    int y = (size - scaledHeight) / 2;

                    // Appliquer la transformation
                    AffineTransform transform = new AffineTransform();
                    transform.scale(scale, scale);
                    transform.translate(x / scale, y / scale);
                    g2d.setTransform(transform);

                    // Dessiner l'image dans un cercle avec une bordure blanche
                    g2d.setClip(new Ellipse2D.Float(0, 0, size, size));
                    g2d.setColor(Color.WHITE);
                    g2d.fillOval(0, 0, size, size);
                    g2d.drawImage(originalImage, 0, 0, null);
                    g2d.dispose();

                    photoLabel.setIcon(new ImageIcon(resizedImage));
                } else {
                    photoLabel.setText("Pas de photo");
                    photoLabel.setForeground(Color.WHITE);
                }
            } catch (IOException e) {
                photoLabel.setText("Erreur photo");
                photoLabel.setForeground(Color.WHITE);
            }

            // Ajouter le nom à côté de la photo
            JLabel nameLabel = new JLabel("Dr. " + vet.getFullName());
            nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            nameLabel.setForeground(Color.WHITE);

            // Ajouter un effet de survol
            MouseAdapter hoverEffect = new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    nameLabel.setForeground(new Color(255, 255, 200));
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    nameLabel.setForeground(Color.WHITE);
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            };

            // Ajouter un clic sur la photo ou le nom pour ouvrir ProfileFrame
            Veterinarian finalVet = vet;
            MouseAdapter openProfileListener = new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    new ProfileFrame(finalVet, MainFrame.this).setVisible(true);
                }
            };
            photoLabel.addMouseListener(openProfileListener);
            photoLabel.addMouseListener(hoverEffect);
            nameLabel.addMouseListener(openProfileListener);
            nameLabel.addMouseListener(hoverEffect);

            profilePanel.add(photoLabel);
            profilePanel.add(nameLabel);

            // Ajouter le bouton de chat
            JButton chatButton = new JButton("💬");
            chatButton.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            chatButton.setForeground(Color.WHITE);
            chatButton.setBackground(new Color(79, 129, 189));
            chatButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            chatButton.setFocusPainted(false);
            chatButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            chatButton.setToolTipText("Ouvrir le chat");

            // Effet de survol pour le bouton
            MouseAdapter buttonHoverEffect = new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    chatButton.setBackground(new Color(59, 89, 152));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    chatButton.setBackground(new Color(79, 129, 189));
                }
            };

            chatButton.addMouseListener(buttonHoverEffect);

            // Action du bouton de chat
            chatButton.addActionListener(e -> new ChatFrame(loggedInUsername).setVisible(true));

            profilePanel.add(chatButton);
        } else {
            JLabel errorLabel = new JLabel("Erreur de profil");
            errorLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            errorLabel.setForeground(Color.WHITE);
            profilePanel.add(errorLabel);
        }

        // Ajouter le panneau de profil en haut à droite
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(new Color(79, 129, 189)); // Même bleu que le profilePanel
        topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(59, 89, 152))); // Bordure plus foncée
        topPanel.add(profilePanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Onglets pour les fonctionnalités principales
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        tabbedPane.setBackground(new Color(236, 242, 255));
        tabbedPane.setForeground(new Color(44, 62, 80));
        
        // Style personnalisé pour les onglets
        UIManager.put("TabbedPane.selected", new Color(79, 129, 189));
        UIManager.put("TabbedPane.contentAreaColor", new Color(236, 242, 255));
        
        tabbedPane.addTab("Animaux", new AnimalPanel());
        tabbedPane.addTab("Vaccins", new VaccinationPanel());
        tabbedPane.addTab("Visites", new VisitPanel());
        tabbedPane.addTab("Propriétaires", new OwnerPanel());

        add(tabbedPane, BorderLayout.CENTER);
    }
}