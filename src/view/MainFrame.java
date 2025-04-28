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
        getContentPane().setBackground(new Color(243, 248, 255)); // Fond bleu très clair

        // Récupérer les informations du vétérinaire connecté
        Veterinarian vet = null;
        try {
            vet = client.findVeterinarianByEmail(loggedInUsername);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors de la récupération du profil : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }

        // Créer un panneau pour le profil en haut à droite
        JPanel profilePanel = new JPanel();
        profilePanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        profilePanel.setBackground(new Color(243, 248, 255)); // Bleu professionnel
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




                } else {
                    photoLabel.setText("Pas de photo");
                    photoLabel.setForeground(Color.black);
                }
            } catch (IOException e) {
                photoLabel.setText("Erreur photo");
                photoLabel.setForeground(Color.black);
            }

            // Ajouter le nom à côté de la photo
            JLabel nameLabel = new JLabel("Dr. " + vet.getFullName());
            nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
            nameLabel.setForeground(Color.black);

            // Ajouter un effet de survol
            MouseAdapter hoverEffect = new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    nameLabel.setForeground(new Color(132, 192, 255));
                    setCursor(new Cursor(Cursor.HAND_CURSOR));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    nameLabel.setForeground(Color.black);
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                }
            };

            // Ajouter un clic sur la photo ou le nom pour ouvrir ProfileFrame
            Veterinarian finalVet = vet;
            MouseAdapter openProfileListener = new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    new ProfileFrame( MainFrame.this).setVisible(true);
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
            chatButton.setBackground(new Color(243, 248, 255));
            chatButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
            chatButton.setFocusPainted(false);
            chatButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            chatButton.setToolTipText("Ouvrir le chat");

            // Effet de survol pour le bouton
            MouseAdapter buttonHoverEffect = new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    chatButton.setBackground(new Color(243, 248, 255));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    chatButton.setBackground(new Color(243, 248, 255));
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
        topPanel.setBackground(new Color(243, 248, 255)); // Même bleu que le profilePanel
        topPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(243, 248, 255))); // Bordure plus foncée
        topPanel.add(profilePanel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // Onglets pour les fonctionnalités principales
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.PLAIN, 14)); // Augmenter la taille de la police des onglets
        tabbedPane.setBackground(new Color(236, 242, 255)); // Fond clair
        tabbedPane.setForeground(new Color(44, 62, 80)); // Texte sombre pour contraster avec le fond

// Style personnalisé pour les onglets
        UIManager.put("TabbedPane.selected", new Color(79, 129, 189)); // Couleur de l'onglet sélectionné
        UIManager.put("TabbedPane.contentAreaColor", new Color(236, 242, 255)); // Couleur de l'arrière-plan des panneaux d'onglets
        UIManager.put("TabbedPane.tabAreaBackground", new Color(236, 242, 255)); // Fond de la zone des onglets

// Augmenter la hauteur des onglets
        UIManager.put("TabbedPane.tabHeight", 50); // Hauteur des onglets
        UIManager.put("TabbedPane.tabInsets", new Insets(10, 20, 10, 20)); // Marge des onglets pour augmenter la taille des zones de texte

// Appliquer une bordure plus marquée sur l'onglet sélectionné pour un effet plus net
        UIManager.put("TabbedPane.borderColor", new Color(79, 129, 189));

// Ajouter des effets de survol (hover)
        tabbedPane.addChangeListener(e -> {
            int selectedIndex = tabbedPane.getSelectedIndex();
            if (selectedIndex != -1) {
                tabbedPane.setBackgroundAt(selectedIndex, new Color(132, 192, 255)); // Couleur de fond au survol
            }
        });

// Ajouter les onglets
        tabbedPane.addTab("Propriétaires", new OwnerPanel());
        tabbedPane.addTab("Animaux", new AnimalPanel());
        tabbedPane.addTab("Vaccins", new VaccinationPanel());
        tabbedPane.addTab("Visites", new VisitPanel());


// Ajouter le tabbedPane au centre
        add(tabbedPane, BorderLayout.CENTER);

    }
}