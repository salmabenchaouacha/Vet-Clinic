package view;

import model.Owner;
import service.AnimalServiceClient;
import service.OwnerServiceClient;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.stream.Collectors;

public class OwnerPanel extends JPanel {
    private OwnerServiceClient service;
    private AnimalServiceClient animalService;
    private JTable ownerTable;
    private DefaultTableModel tableModel;

    public OwnerPanel() {
        this.service = new OwnerServiceClient();
        this.animalService = new AnimalServiceClient();
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 220)); // Fond beige clair pour le panneau principal

        // Tableau des propriétaires
        String[] columns = {"ID", "Nom", "Téléphone", "Email"};
        tableModel = new DefaultTableModel(columns, 0);
        ownerTable = new JTable(tableModel);
        ownerTable.setRowHeight(25);
        ownerTable.setFont(new Font("Arial", Font.PLAIN, 12));
        ownerTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(ownerTable);
        add(scrollPane, BorderLayout.CENTER);

        // Boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(245, 245, 220));
        JButton addButton = new JButton("Ajouter");
        JButton updateButton = new JButton("Modifier");
        JButton deleteButton = new JButton("Supprimer");
        styleButton(addButton);
        styleButton(updateButton);
        styleButton(deleteButton);
        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Charger les données
        refreshTable();

        // Action ajouter
        addButton.addActionListener(e -> showOwnerDialog(null));

        // Action modifier
        updateButton.addActionListener(e -> {
            int selectedRow = ownerTable.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                String fullName = (String) tableModel.getValueAt(selectedRow, 1);
                String phone = (String) tableModel.getValueAt(selectedRow, 2);
                String email = (String) tableModel.getValueAt(selectedRow, 3);
                Owner owner = new Owner(id, fullName, phone, email);
                showOwnerDialog(owner);
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner un propriétaire", "Erreur", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Action supprimer
        deleteButton.addActionListener(e -> {
            int selectedRow = ownerTable.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(this, "Supprimer ce propriétaire ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        service.deleteOwner(id);
                        refreshTable();
                        JOptionPane.showMessageDialog(this, "Propriétaire supprimé avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner un propriétaire", "Erreur", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Menu contextuel pour clic droit
        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem viewProfileItem = new JMenuItem("Voir profil");
        viewProfileItem.setFont(new Font("Arial", Font.PLAIN, 12));
        popupMenu.add(viewProfileItem);

        // Action pour "Voir profil"
        viewProfileItem.addActionListener(e -> {
            int selectedRow = ownerTable.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                try {
                    List<Owner> owners = service.getAllOwners();
                    Owner selectedOwner = owners.stream()
                            .filter(o -> o.getId() == id)
                            .findFirst()
                            .orElse(null);
                    if (selectedOwner != null) {
                        showOwnerProfileDialog(selectedOwner);
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(OwnerPanel.this, "Erreur lors du chargement du profil : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Clic droit pour afficher le menu contextuel
        ownerTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    int row = ownerTable.rowAtPoint(e.getPoint());
                    if (row >= 0) {
                        ownerTable.setRowSelectionInterval(row, row);
                        popupMenu.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    int row = ownerTable.rowAtPoint(e.getPoint());
                    if (row >= 0) {
                        ownerTable.setRowSelectionInterval(row, row);
                        popupMenu.show(e.getComponent(), e.getX(), e.getY());
                    }
                }
            }
        });
    }

    private void showOwnerDialog(Owner owner) {
        JDialog dialog = new JDialog((Frame) null, owner == null ? "Ajouter Propriétaire" : "Modifier Propriétaire", true);
        dialog.setSize(400, 250);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.getContentPane().setBackground(new Color(224, 242, 254)); // Bleu pâle pastel (#E0F2FE)

        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formPanel.setBackground(new Color(224, 242, 254));

        JTextField nameField = new JTextField(owner != null ? owner.getFullName() : "");
        JTextField phoneField = new JTextField(owner != null ? owner.getPhone() : "");
        JTextField emailField = new JTextField(owner != null ? owner.getEmail() : "");

        formPanel.add(new JLabel("Nom complet :"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Téléphone :"));
        formPanel.add(phoneField);
        formPanel.add(new JLabel("Email :"));
        formPanel.add(emailField);

        JButton saveButton = new JButton("Enregistrer");
        styleButton(saveButton);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(224, 242, 254));
        buttonPanel.add(saveButton);
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        saveButton.addActionListener(e -> {
            if (nameField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Le nom est obligatoire", "Erreur", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                Owner newOwner = new Owner(
                        owner != null ? owner.getId() : 0,
                        nameField.getText().trim(),
                        phoneField.getText().trim(),
                        emailField.getText().trim()
                );
                if (owner == null) {
                    service.addOwner(newOwner);
                    JOptionPane.showMessageDialog(dialog, "Propriétaire ajouté avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    service.updateOwner(newOwner);
                    JOptionPane.showMessageDialog(dialog, "Propriétaire modifié avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
                }
                refreshTable();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setVisible(true);
    }

    private void showOwnerProfileDialog(Owner owner) {
        JDialog profileDialog = new JDialog((Frame) null, "Profil de " + owner.getFullName(), true);
        profileDialog.setSize(600, 600);
        profileDialog.setLocationRelativeTo(this);
        profileDialog.setLayout(new BorderLayout(10, 10));
        profileDialog.getContentPane().setBackground(new Color(224, 242, 254)); // Bleu pâle pastel (#E0F2FE)

        // Panneau principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(167, 243, 208), 1), // Vert menthe pastel (#A7F3D0)
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        mainPanel.setBackground(new Color(248, 250, 252)); // Blanc cassé (#F8FAFC)

        // Titre
        JLabel titleLabel = new JLabel("Profil de " + owner.getFullName(), SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        titleLabel.setForeground(new Color(165, 180, 252)); // Bleu lavande (#A5B4FC)
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // Contenu principal (détails + animaux)
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(new Color(248, 250, 252));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Détails du propriétaire
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBackground(new Color(248, 250, 252));
        detailsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createTitledBorder(
                        BorderFactory.createLineBorder(new Color(167, 243, 208), 2), // Vert menthe pastel (#A7F3D0)
                        "Informations Personnelles",
                        javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                        javax.swing.border.TitledBorder.DEFAULT_POSITION,
                        new Font("Arial", Font.ITALIC, 16),
                        new Color(165, 180, 252) // Bleu lavande (#A5B4FC)
                ),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        GridBagConstraints detailsGbc = new GridBagConstraints();
        detailsGbc.insets = new Insets(5, 5, 5, 5);
        detailsGbc.anchor = GridBagConstraints.WEST;

        // Ajout des détails
        detailsGbc.gridx = 0;
        detailsGbc.gridy = 0;
        detailsPanel.add(createStyledLabel("ID :", true), detailsGbc);
        detailsGbc.gridx = 1;
        detailsPanel.add(createStyledLabel(String.valueOf(owner.getId()), false), detailsGbc);

        detailsGbc.gridx = 0;
        detailsGbc.gridy = 1;
        detailsPanel.add(createStyledLabel("Nom :", true), detailsGbc);
        detailsGbc.gridx = 1;
        detailsPanel.add(createStyledLabel(owner.getFullName(), false), detailsGbc);

        detailsGbc.gridx = 0;
        detailsGbc.gridy = 2;
        detailsPanel.add(createStyledLabel("Téléphone :", true), detailsGbc);
        detailsGbc.gridx = 1;
        detailsPanel.add(createStyledLabel(owner.getPhone() != null ? owner.getPhone() : "-", false), detailsGbc);

        detailsGbc.gridx = 0;
        detailsGbc.gridy = 3;
        detailsPanel.add(createStyledLabel("Email :", true), detailsGbc);
        detailsGbc.gridx = 1;
        detailsPanel.add(createStyledLabel(owner.getEmail() != null ? owner.getEmail() : "-", false), detailsGbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPanel.add(detailsPanel, gbc);

        // Liste des animaux
        JPanel animalsPanel = new JPanel(new GridBagLayout());
        animalsPanel.setBackground(new Color(248, 250, 252));
        GridBagConstraints animalsGbc = new GridBagConstraints();
        animalsGbc.insets = new Insets(5, 0, 5, 0);
        animalsGbc.fill = GridBagConstraints.HORIZONTAL;
        animalsGbc.weightx = 1.0;

        // Compteur d'animaux
        List<model.Animal> animals;
        int animalCount = 0;
        try {
            animals = animalService.getAllAnimals().stream()
                    .filter(a -> a.getOwnerId() == owner.getId())
                    .collect(Collectors.toList());
            animalCount = animals.size();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des animaux : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        JLabel animalCountLabel = new JLabel("Nombre d’animaux : " + animalCount);
        animalCountLabel.setFont(new Font("Arial", Font.BOLD, 14));
        animalCountLabel.setForeground(new Color(165, 180, 252)); // Bleu lavande (#A5B4FC)
        animalsGbc.gridx = 0;
        animalsGbc.gridy = 0;
        animalsGbc.anchor = GridBagConstraints.WEST;
        animalsPanel.add(animalCountLabel, animalsGbc);

        // Panneau pour les cartes d’animaux (avec défilement)
        JPanel animalCardsPanel = new JPanel(new GridBagLayout());
        animalCardsPanel.setBackground(new Color(248, 250, 252));
        GridBagConstraints cardGbc = new GridBagConstraints();
        cardGbc.insets = new Insets(10, 0, 10, 0);
        cardGbc.fill = GridBagConstraints.HORIZONTAL;
        cardGbc.weightx = 1.0;
        cardGbc.gridx = 0;

        // Ajout des cartes pour chaque animal
        for (int i = 0; i < animals.size(); i++) {
            model.Animal animal = animals.get(i);
            JPanel animalCard = createAnimalCard(animal);
            cardGbc.gridy = i;
            animalCardsPanel.add(animalCard, cardGbc);
        }

        // Ajout du panneau des cartes dans un JScrollPane
        JScrollPane animalsScrollPane = new JScrollPane(animalCardsPanel);
        animalsScrollPane.setBorder(BorderFactory.createEmptyBorder());
        animalsScrollPane.setBackground(new Color(248, 250, 252));
        animalsScrollPane.getVerticalScrollBar().setUnitIncrement(16); // Défilement fluide
        animalsGbc.gridx = 0;
        animalsGbc.gridy = 1;
        animalsGbc.weighty = 1.0;
        animalsGbc.fill = GridBagConstraints.BOTH;
        animalsPanel.add(animalsScrollPane, animalsGbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        contentPanel.add(animalsPanel, gbc);

        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Bouton fermer
        JButton closeButton = new JButton("Fermer");
        styleButton(closeButton);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(224, 242, 254));
        buttonPanel.add(closeButton);
        closeButton.addActionListener(e -> profileDialog.dispose());

        // Effet de survol pour le bouton
        closeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                closeButton.setBackground(new Color(249, 168, 212)); // Rose vif pastel (#F9A8D4)
            }

            @Override
            public void mouseExited(MouseEvent e) {
                closeButton.setBackground(new Color(251, 207, 232)); // Rose poudré (#FBCFE8)
            }
        });

        profileDialog.add(mainPanel, BorderLayout.CENTER);
        profileDialog.add(buttonPanel, BorderLayout.SOUTH);

        profileDialog.setVisible(true);
    }

    private JPanel createAnimalCard(model.Animal animal) {
        JPanel card = new JPanel(new BorderLayout(10, 10));
        card.setBackground(new Color(248, 250, 252)); // Blanc cassé (#F8FAFC)
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(167, 243, 208), 1), // Vert menthe pastel (#A7F3D0)
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        // Icône (placeholder textuel)
        JLabel iconLabel = new JLabel("🐾");
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        iconLabel.setForeground(new Color(165, 180, 252)); // Bleu lavande (#A5B4FC)
        card.add(iconLabel, BorderLayout.WEST);

        // Détails de l’animal
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBackground(new Color(248, 250, 252));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 5, 2, 5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        detailsPanel.add(createStyledLabel("Nom :", true), gbc);
        gbc.gridx = 1;
        detailsPanel.add(createStyledLabel(animal.getName(), false), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        detailsPanel.add(createStyledLabel("Espèce :", true), gbc);
        gbc.gridx = 1;
        detailsPanel.add(createStyledLabel(animal.getSpecies(), false), gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        detailsPanel.add(createStyledLabel("Race :", true), gbc);
        gbc.gridx = 1;
        detailsPanel.add(createStyledLabel(animal.getBreed() != null ? animal.getBreed() : "-", false), gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        detailsPanel.add(createStyledLabel("Âge :", true), gbc);
        gbc.gridx = 1;
        detailsPanel.add(createStyledLabel(animal.getAge() != null ? animal.getAge() : "-", false), gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        detailsPanel.add(createStyledLabel("Sexe :", true), gbc);
        gbc.gridx = 1;
        detailsPanel.add(createStyledLabel(animal.getSex() != null ? animal.getSex() : "-", false), gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        detailsPanel.add(createStyledLabel("Puce :", true), gbc);
        gbc.gridx = 1;
        detailsPanel.add(createStyledLabel(animal.getChipNumber() != null ? animal.getChipNumber() : "-", false), gbc);

        card.add(detailsPanel, BorderLayout.CENTER);

        return card;
    }

    private JLabel createStyledLabel(String text, boolean isBold) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", isBold ? Font.BOLD : Font.PLAIN, 12));
        label.setForeground(isBold ? new Color(100, 116, 139) : new Color(30, 41, 59)); // Gris bleuté (#64748B) ou noir adouci (#1E293B)
        return label;
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        try {
            List<Owner> owners = service.getAllOwners();
            for (Owner owner : owners) {
                tableModel.addRow(new Object[]{
                        owner.getId(),
                        owner.getFullName(),
                        owner.getPhone() != null ? owner.getPhone() : "-",
                        owner.getEmail() != null ? owner.getEmail() : "-"
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des propriétaires : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(251, 207, 232)); // Rose poudré (#FBCFE8)
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }
}