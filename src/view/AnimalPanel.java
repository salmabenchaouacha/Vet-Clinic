package view;

import model.Animal;
import model.Owner;
import rmi.VeterinaryService;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;

public class AnimalPanel extends JPanel {
    private VeterinaryService service;
    private JTable animalTable;
    private DefaultTableModel tableModel;

    public AnimalPanel(VeterinaryService service) {
        this.service = service;
        setLayout(new BorderLayout());
        setBackground(new Color(224, 242, 254)); // Bleu pâle pastel (#E0F2FE)

        // Tableau des animaux avec une colonne pour la photo
        String[] columns = {"ID", "Photo", "Nom", "Espèce", "Race", "Âge", "Sexe", "Puce", "Propriétaire"};
        tableModel = new DefaultTableModel(columns, 0);
        animalTable = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (isRowSelected(row)) {
                    c.setBackground(new Color(255, 255, 255)); // Ligne sélectionnée : blanc
                    c.setForeground(Color.BLACK);
                } else {
                    c.setBackground(new Color(255, 255, 255)); // Toutes les lignes en blanc
                    c.setForeground(new Color(100, 116, 139)); // Gris bleuté pour le texte
                }
                return c;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == 1) { // Colonne "Photo"
                    return ImageIcon.class;
                }
                return super.getColumnClass(columnIndex);
            }
        };

        // Style du tableau
        animalTable.setRowHeight(50); // Augmenter la hauteur pour les images
        animalTable.setFont(new Font("Arial", Font.PLAIN, 12));
        animalTable.setShowGrid(true);
        animalTable.setGridColor(new Color(224, 242, 254)); // Grille en bleu pâle
        animalTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Centrer certaines colonnes
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        animalTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // ID
        animalTable.getColumnModel().getColumn(4).setCellRenderer(centerRenderer); // Âge
        animalTable.getColumnModel().getColumn(5).setCellRenderer(centerRenderer); // Sexe
        animalTable.getColumnModel().getColumn(6).setCellRenderer(centerRenderer); // Puce

        // Ajuster la largeur de la colonne "Photo"
        animalTable.getColumnModel().getColumn(1).setPreferredWidth(50);

        // Style de l'en-tête
        JTableHeader header = animalTable.getTableHeader();
        header.setBackground(new Color(242, 248, 226)); // Vert menthe pastel (#FAEDCB)
        header.setForeground(Color.BLACK);
        header.setFont(new Font("Arial", Font.BOLD, 12));
        header.setBorder(BorderFactory.createLineBorder(new Color(250, 237, 203), 1));

        // Effet de survol sur les lignes
        animalTable.addMouseMotionListener(new MouseAdapter() {
            private int lastRow = -1;

            @Override
            public void mouseMoved(MouseEvent e) {
                int row = animalTable.rowAtPoint(e.getPoint());
                if (row != lastRow) {
                    if (lastRow != -1 && !animalTable.isRowSelected(lastRow)) {
                        animalTable.repaint(animalTable.getCellRect(lastRow, 0, true));
                    }
                    if (row != -1 && !animalTable.isRowSelected(row)) {
                        lastRow = row;
                        animalTable.repaint(animalTable.getCellRect(row, 0, true));
                    } else {
                        lastRow = -1;
                    }
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (lastRow != -1 && !animalTable.isRowSelected(lastRow)) {
                    animalTable.repaint(animalTable.getCellRect(lastRow, 0, true));
                    lastRow = -1;
                }
            }
        });

        animalTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                int hoveredRow = -1;
                Point p = table.getMousePosition();
                if (p != null) {
                    hoveredRow = table.rowAtPoint(p);
                }
                if (isSelected) {
                    c.setBackground(new Color(255, 255, 255)); // Ligne sélectionnée : blanc
                    c.setForeground(Color.BLACK);
                } else if (row == hoveredRow) {
                    c.setBackground(new Color(224, 242, 254)); // Bleu pâle pour le survol (#E0F2FE)
                    c.setForeground(new Color(100, 116, 139));
                } else {
                    c.setBackground(new Color(255, 255, 255)); // Toutes les lignes en blanc
                    c.setForeground(new Color(100, 116, 139));
                }
                return c;
            }
        });

        // ScrollPane
        JScrollPane scrollPane = new JScrollPane(animalTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(243, 248, 255), 1)); // Bordure bleu pâle
        scrollPane.setBackground(new Color(243, 248, 255)); // Fond bleu pâle (#E0F2FE)
        scrollPane.getViewport().setBackground(new Color(243, 248, 255)); // Fond sous le tableau : bleu pâle (#E0F2FE)
        add(scrollPane, BorderLayout.CENTER);

        // Boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(243, 248, 255));
        JButton addButton = new JButton("Ajouter");
        JButton updateButton = new JButton("Modifier");
        JButton deleteButton = new JButton("Supprimer");

        // Appliquer des couleurs distinctes à chaque bouton
        styleButton(addButton, new Color(201, 228, 222), new Color(209, 250, 229)); // Vert menthe et survol
        styleButton(updateButton, new Color(198, 222, 241), new Color(199, 210, 254)); // Bleu lavande et survol
        styleButton(deleteButton, new Color(239, 207, 226), new Color(249, 168, 212)); // Rose poudré et survol

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // Charger les données
        refreshTable();

        // Action ajouter
        addButton.addActionListener(e -> showAnimalDialog(null));

        // Action modifier
        updateButton.addActionListener(e -> {
            int selectedRow = animalTable.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                String name = (String) tableModel.getValueAt(selectedRow, 2);
                String species = (String) tableModel.getValueAt(selectedRow, 3);
                String breed = (String) tableModel.getValueAt(selectedRow, 4);
                String age = (String) tableModel.getValueAt(selectedRow, 5);
                String sex = (String) tableModel.getValueAt(selectedRow, 6);
                String chipNumber = (String) tableModel.getValueAt(selectedRow, 7);
                int ownerId = (int) tableModel.getValueAt(selectedRow, 8);
                String photoPath = (String) tableModel.getValueAt(selectedRow, 1); // Récupérer le chemin de la photo
                Animal animal = new Animal(id, name, species, breed, age, sex, chipNumber, ownerId, photoPath);
                showAnimalDialog(animal);
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner un animal", "Erreur", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Action supprimer
        deleteButton.addActionListener(e -> {
            int selectedRow = animalTable.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(this, "Supprimer cet animal ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        service.deleteAnimal(id);
                        refreshTable();
                        JOptionPane.showMessageDialog(this, "Animal supprimé avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner un animal", "Erreur", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    private void showAnimalDialog(Animal animal) {
        JDialog dialog = new JDialog((Frame) null, animal == null ? "Ajouter Animal" : "Modifier Animal", true);
        dialog.setSize(400, 450); // Augmenter la taille pour le champ photo
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.getContentPane().setBackground(new Color(224, 242, 254)); // Bleu pâle pastel (#E0F2FE)

        JPanel formPanel = new JPanel(new GridLayout(9, 2, 10, 10)); // Ajouter une ligne pour la photo
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formPanel.setBackground(new Color(224, 242, 254)); // Bleu pâle pastel (#E0F2FE)

        JTextField nameField = new JTextField(animal != null ? animal.getName() : "");
        JTextField speciesField = new JTextField(animal != null ? animal.getSpecies() : "");
        JTextField breedField = new JTextField(animal != null ? animal.getBreed() : "");
        JTextField ageField = new JTextField(animal != null ? animal.getAge() : "");
        JComboBox<String> sexCombo = new JComboBox<>(new String[]{"Mâle", "Femelle"});
        if (animal != null) sexCombo.setSelectedItem(animal.getSex());
        JTextField chipField = new JTextField(animal != null ? animal.getChipNumber() : "");
        JTextField photoField = new JTextField(animal != null ? animal.getPhotoPath() : "");
        photoField.setEditable(false); // Champ non éditable directement

        JButton selectPhotoButton = new JButton("Choisir une photo");
        selectPhotoButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File("images/"));
            fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            fileChooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter("Images", "jpg", "png", "jpeg"));
            int result = fileChooser.showOpenDialog(dialog);
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                photoField.setText(selectedFile.getPath());
            }
        });

        JComboBox<String> ownerCombo = new JComboBox<>();
        try {
            List<Owner> owners = service.getAllOwners();
            for (Owner owner : owners) {
                ownerCombo.addItem(owner.getId() + " - " + owner.getFullName());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des propriétaires : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (animal != null) {
            ownerCombo.setSelectedItem(animal.getOwnerId() + " - ");
        }

        formPanel.add(new JLabel("Nom :"));
        formPanel.add(nameField);
        formPanel.add(new JLabel("Espèce :"));
        formPanel.add(speciesField);
        formPanel.add(new JLabel("Race :"));
        formPanel.add(breedField);
        formPanel.add(new JLabel("Âge :"));
        formPanel.add(ageField);
        formPanel.add(new JLabel("Sexe :"));
        formPanel.add(sexCombo);
        formPanel.add(new JLabel("Numéro de puce :"));
        formPanel.add(chipField);
        formPanel.add(new JLabel("Photo :"));
        formPanel.add(photoField);
        formPanel.add(new JLabel(""));
        formPanel.add(selectPhotoButton);
        formPanel.add(new JLabel("Propriétaire :"));
        formPanel.add(ownerCombo);

        JButton saveButton = new JButton("Enregistrer");
        styleButton(saveButton, new Color(251, 207, 232), new Color(249, 168, 212)); // Rose poudré et survol
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(224, 242, 254));
        buttonPanel.add(saveButton);
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        saveButton.addActionListener(e -> {
            if (nameField.getText().trim().isEmpty() || speciesField.getText().trim().isEmpty() || ownerCombo.getSelectedItem() == null) {
                JOptionPane.showMessageDialog(dialog, "Veuillez remplir les champs obligatoires (Nom, Espèce, Propriétaire)", "Erreur", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                String ownerItem = (String) ownerCombo.getSelectedItem();
                int ownerId = Integer.parseInt(ownerItem.split(" - ")[0]);
                Animal newAnimal = new Animal(
                        animal != null ? animal.getId() : 0,
                        nameField.getText().trim(),
                        speciesField.getText().trim(),
                        breedField.getText().trim(),
                        ageField.getText().trim(),
                        (String) sexCombo.getSelectedItem(),
                        chipField.getText().trim(),
                        ownerId,
                        photoField.getText().trim() // Enregistrer le chemin de la photo
                );
                if (animal == null) {
                    service.addAnimal(newAnimal);
                    JOptionPane.showMessageDialog(dialog, "Animal ajouté avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    service.updateAnimal(newAnimal);
                    JOptionPane.showMessageDialog(dialog, "Animal modifié avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
                }
                refreshTable();
                dialog.dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        dialog.setVisible(true);
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        try {
            List<Animal> animals = service.getAllAnimals();
            List<Owner> owners = service.getAllOwners();
            for (Animal animal : animals) {
                String ownerName = owners.stream()
                        .filter(o -> o.getId() == animal.getOwnerId())
                        .findFirst()
                        .map(Owner::getFullName)
                        .orElse("Inconnu");
                ImageIcon imageIcon = null;
                if (animal.getPhotoPath() != null && !animal.getPhotoPath().isEmpty()) {
                    try {
                        Image image = new ImageIcon(animal.getPhotoPath()).getImage();
                        image = image.getScaledInstance(40, 40, Image.SCALE_SMOOTH);
                        imageIcon = new ImageIcon(image);
                    } catch (Exception e) {
                        System.err.println("Erreur lors du chargement de l'image : " + e.getMessage());
                    }
                }
                tableModel.addRow(new Object[]{
                        animal.getId(),
                        imageIcon, // Ajouter l'image
                        animal.getName(),
                        animal.getSpecies(),
                        animal.getBreed(),
                        animal.getAge(),
                        animal.getSex(),
                        animal.getChipNumber(),
                        animal.getOwnerId() + " - " + ownerName
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des animaux : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void styleButton(JButton button, Color baseColor, Color hoverColor) {
        button.setBackground(baseColor);
        button.setForeground(Color.BLACK);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(baseColor);
            }
        });
    }
}