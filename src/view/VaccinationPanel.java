package view;

import model.Animal;
import model.Owner;
import model.Vaccination;
import service.AnimalServiceClient;
import service.OwnerServiceClient;
import service.VaccinationServiceClient;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class VaccinationPanel extends JPanel {
    private VaccinationServiceClient service;
    private AnimalServiceClient animalService;
    private JTable vaccinationTable;
    private DefaultTableModel tableModel;
    private OwnerServiceClient ownerService;
    private JScrollPane scrollPane;
    // Liste prédéfinie des types de vaccins
    private static final String[] VACCINE_TYPES = {
            "Rage",
            "Leptospirose",
            "Tétanos",
            "Parvovirose",
            "Hépatite",
            "Maladie de Carré",
            "Typhus"
    };

    public VaccinationPanel() {
        this.service = new VaccinationServiceClient();
        this.ownerService = new OwnerServiceClient();
        this.animalService = new AnimalServiceClient();
        setLayout(new BorderLayout());
        setBackground(new Color(255, 255, 255)); // Changed to white to match OwnerPanel

        // Tableau des vaccins
        String[] columns = {"ID", "Animal", "Vaccin", "Date"};
        tableModel = new DefaultTableModel(columns, 0);
        vaccinationTable = new JTable(tableModel) {
            @Override
            public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                if (isRowSelected(row)) {
                    c.setBackground(new Color(198, 222, 241)); // Ligne sélectionnée : bleu lavande
                    c.setForeground(Color.BLACK);
                } else {
                    c.setBackground(new Color(255, 255, 255)); // Toutes les lignes en blanc
                    c.setForeground(new Color(100, 116, 139)); // Gris bleuté pour le texte
                }
                return c;
            }
        };

        // Style du tableau (to match OwnerPanel)
        vaccinationTable.setRowHeight(50); // Augmenter la hauteur
        vaccinationTable.setFont(new Font("Arial", Font.PLAIN, 12));
        vaccinationTable.setShowGrid(true);
        vaccinationTable.setGridColor(new Color(224, 242, 254)); // Grille en bleu pâle
        vaccinationTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Centrer certaines colonnes
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        vaccinationTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); // ID

        // Permettre au tableau de s'étirer sur toute la largeur de la fenêtre
        vaccinationTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS); // Les colonnes s'ajustent pour remplir l'espace

        // Style de l'en-tête
        JTableHeader header = vaccinationTable.getTableHeader();
        header.setBackground(new Color(242, 248, 226)); // Vert menthe pastel (#FAEDCB)
        header.setForeground(Color.BLACK);
        header.setFont(new Font("Arial", Font.BOLD, 12));
        header.setBorder(BorderFactory.createLineBorder(new Color(250, 237, 203), 1));

        // Effet de survol sur les lignes
        vaccinationTable.addMouseMotionListener(new MouseAdapter() {
            private int lastRow = -1;

            @Override
            public void mouseMoved(MouseEvent e) {
                int row = vaccinationTable.rowAtPoint(e.getPoint());
                if (row != lastRow) {
                    if (lastRow != -1 && !vaccinationTable.isRowSelected(lastRow)) {
                        vaccinationTable.repaint(vaccinationTable.getCellRect(lastRow, 0, true));
                    }
                    if (row != -1 && !vaccinationTable.isRowSelected(row)) {
                        lastRow = row;
                        vaccinationTable.repaint(vaccinationTable.getCellRect(row, 0, true));
                    } else {
                        lastRow = -1;
                    }
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (lastRow != -1 && !vaccinationTable.isRowSelected(lastRow)) {
                    vaccinationTable.repaint(vaccinationTable.getCellRect(lastRow, 0, true));
                    lastRow = -1;
                }
            }
        });

        vaccinationTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                int hoveredRow = -1;
                Point p = table.getMousePosition();
                if (p != null) {
                    hoveredRow = table.rowAtPoint(p);
                }
                if (isSelected) {
                    c.setBackground(new Color(198, 222, 241)); // Ligne sélectionnée : bleu lavande
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

        // Ajouter la JTable dans un JScrollPane pour avoir un scroll
        scrollPane = new JScrollPane(vaccinationTable);
        scrollPane.setBackground(new Color(255, 255, 255)); // Changed to white to match OwnerPanel
        scrollPane.getViewport().setBackground(new Color(255, 255, 255)); // Changed to white to match OwnerPanel
        add(scrollPane, BorderLayout.CENTER);

        // Boutons (centered to match OwnerPanel)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(243, 248, 255)); // Match OwnerPanel subpanel background
        JButton addButton = new JButton("Ajouter");
        JButton updateButton = new JButton("Modifier");
        JButton deleteButton = new JButton("Supprimer");

        // Appliquer des couleurs distinctes à chaque bouton (to match OwnerPanel)
        styleButton(addButton, new Color(185, 201, 128), new Color(209, 250, 229)); // Vert menthe et survol
        styleButton(updateButton, new Color(184, 180, 227), new Color(199, 210, 254)); // Bleu lavande et survol
        styleButton(deleteButton, new Color(241, 199, 199), new Color(249, 168, 212)); // Rose poudré et survol

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);

        // Load the image (to match OwnerPanel)
        ImageIcon catImageIcon;
        try {
            catImageIcon = new ImageIcon("emoji/vaccin-removebg-preview (1).png");
            // Resize the image to a smaller width
            Image scaledImage = catImageIcon.getImage().getScaledInstance(300, -1, Image.SCALE_SMOOTH); // Adjust width to 300px, height proportional
            catImageIcon = new ImageIcon(scaledImage);
        } catch (Exception e) {
            System.err.println("Erreur lors du chargement de l'image : " + e.getMessage());
            catImageIcon = new ImageIcon(); // Fallback to an empty icon
        }
        JLabel catImageLabel = new JLabel(catImageIcon);

        // Create a panel to hold the image and center it
        JPanel imagePanel = new JPanel();
        imagePanel.setBackground(new Color(243, 248, 255)); // Match OwnerPanel subpanel background
        imagePanel.setLayout(new FlowLayout(FlowLayout.CENTER)); // Center the image
        imagePanel.add(catImageLabel);

        // Create a main south panel to hold both the image panel and the button panel
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setBackground(new Color(243, 248, 255)); // Match OwnerPanel subpanel background
        southPanel.add(imagePanel, BorderLayout.NORTH); // Image at the top
        southPanel.add(buttonPanel, BorderLayout.CENTER); // Buttons below the image

        // Add the south panel to the main layout
        add(southPanel, BorderLayout.SOUTH);

        // Charger les données
        refreshTable();

        // Action ajouter
        addButton.addActionListener(e -> showVaccinationDialog(null));

        // Action modifier
        updateButton.addActionListener(e -> {
            int selectedRow = vaccinationTable.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                int animalId = Integer.parseInt(tableModel.getValueAt(selectedRow, 1).toString().split(" - ")[0]);
                String vaccineName = (String) tableModel.getValueAt(selectedRow, 2);
                String date = (String) tableModel.getValueAt(selectedRow, 3);
                Vaccination vaccination = new Vaccination(id, animalId, vaccineName, date);
                showVaccinationDialog(vaccination);
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner un vaccin", "Erreur", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Action supprimer
        deleteButton.addActionListener(e -> {
            int selectedRow = vaccinationTable.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(this, "Supprimer ce vaccin ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        service.deleteVaccination(id);
                        refreshTable();
                        JOptionPane.showMessageDialog(this, "Vaccin supprimé avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner un vaccin", "Erreur", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    private void showVaccinationDialog(Vaccination vaccination) {
        JDialog dialog = new JDialog((Frame) null, vaccination == null ? "Ajouter Vaccin" : "Modifier Vaccin", true);
        dialog.setSize(400, 300);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.getContentPane().setBackground(new Color(224, 242, 254)); // Bleu pâle pastel (#E0F2FE)

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formPanel.setBackground(new Color(224, 242, 254));

        // Liste des propriétaires
        JComboBox<String> ownerCombo = new JComboBox<>();
        JComboBox<String> animalCombo = new JComboBox<>();
        // Liste déroulante pour les types de vaccins
        JComboBox<String> vaccineCombo = new JComboBox<>(VACCINE_TYPES);
        // JSpinner pour la date
        SpinnerDateModel dateModel = new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH);
        JSpinner dateSpinner = new JSpinner(dateModel);
        dateSpinner.setEditor(new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd"));

        // Pré-sélectionner le vaccin et la date si modification
        if (vaccination != null) {
            vaccineCombo.setSelectedItem(vaccination.getVaccineName());
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = sdf.parse(vaccination.getDate());
                dateModel.setValue(date);
            } catch (Exception e) {
                dateModel.setValue(new Date()); // Date par défaut si erreur
            }
        }

        try {
            List<Owner> owners = ownerService.getAllOwners();
            for (Owner owner : owners) {
                ownerCombo.addItem(owner.getId() + " - " + owner.getFullName());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des propriétaires : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Listener pour mettre à jour la liste des animaux quand un propriétaire est sélectionné
        ownerCombo.addActionListener(e -> {
            animalCombo.removeAllItems();
            String selectedOwner = (String) ownerCombo.getSelectedItem();
            if (selectedOwner != null) {
                try {
                    int ownerId = Integer.parseInt(selectedOwner.split(" - ")[0]);
                    List<Animal> animals = animalService.getAllAnimals().stream()
                            .filter(a -> a.getOwnerId() == ownerId)
                            .collect(Collectors.toList());
                    for (Animal animal : animals) {
                        animalCombo.addItem(animal.getId() + " - " + animal.getName());
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erreur lors du chargement des animaux : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Si modification, pré-sélectionner le propriétaire et l'animal
        if (vaccination != null) {
            try {
                Animal selectedAnimal = animalService.getAllAnimals().stream()
                        .filter(a -> a.getId() == vaccination.getAnimalId())
                        .findFirst()
                        .orElse(null);
                if (selectedAnimal != null) {
                    Owner selectedOwner = ownerService.getAllOwners().stream()
                            .filter(o -> o.getId() == selectedAnimal.getOwnerId())
                            .findFirst()
                            .orElse(null);
                    if (selectedOwner != null) {
                        ownerCombo.setSelectedItem(selectedOwner.getId() + " - " + selectedOwner.getFullName());
                        animalCombo.addItem(selectedAnimal.getId() + " - " + selectedAnimal.getName());
                        animalCombo.setSelectedItem(selectedAnimal.getId() + " - " + selectedAnimal.getName());
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur lors du chargement des données : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }

        formPanel.add(new JLabel("Propriétaire :"));
        formPanel.add(ownerCombo);
        formPanel.add(new JLabel("Animal :"));
        formPanel.add(animalCombo);
        formPanel.add(new JLabel("Type de vaccin :"));
        formPanel.add(vaccineCombo);
        formPanel.add(new JLabel("Date :"));
        formPanel.add(dateSpinner);

        JButton saveButton = new JButton("Enregistrer");
        styleButton(saveButton, new Color(251, 207, 232), new Color(249, 168, 212)); // Rose poudré et survol
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(224, 242, 254));
        buttonPanel.add(saveButton);
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        saveButton.addActionListener(e -> {
            if (animalCombo.getSelectedItem() == null || vaccineCombo.getSelectedItem() == null || dateSpinner.getValue() == null) {
                JOptionPane.showMessageDialog(dialog, "Veuillez remplir tous les champs obligatoires", "Erreur", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                String animalItem = (String) animalCombo.getSelectedItem();
                int animalId = Integer.parseInt(animalItem.split(" - ")[0]);
                String vaccineName = (String) vaccineCombo.getSelectedItem();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String date = sdf.format((Date) dateSpinner.getValue());

                Vaccination newVaccination = new Vaccination(
                        vaccination != null ? vaccination.getId() : 0,
                        animalId,
                        vaccineName,
                        date
                );
                if (vaccination == null) {
                    service.addVaccination(newVaccination);
                    JOptionPane.showMessageDialog(dialog, "Vaccin ajouté avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    service.updateVaccination(newVaccination);
                    JOptionPane.showMessageDialog(dialog, "Vaccin modifié avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
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
            List<Vaccination> vaccinations = service.getAllVaccinations();
            List<Animal> animals = animalService.getAllAnimals();
            for (Vaccination vaccination : vaccinations) {
                String animalName = animals.stream()
                        .filter(a -> a.getId() == vaccination.getAnimalId())
                        .findFirst()
                        .map(a -> a.getId() + " - " + a.getName())
                        .orElse("Inconnu");
                tableModel.addRow(new Object[]{
                        vaccination.getId(),
                        animalName,
                        vaccination.getVaccineName(),
                        vaccination.getDate()
                });
            }

            // Ajuster dynamiquement la hauteur du tableau en fonction du nombre de lignes
            int rowCount = tableModel.getRowCount();
            int rowHeight = vaccinationTable.getRowHeight();
            int headerHeight = vaccinationTable.getTableHeader().getPreferredSize().height;
            int totalHeight = (rowCount * rowHeight) + headerHeight;

            // Définir la taille préférée du tableau
            vaccinationTable.setPreferredScrollableViewportSize(new Dimension(
                    vaccinationTable.getPreferredScrollableViewportSize().width, // Largeur inchangée
                    totalHeight // Hauteur ajustée
            ));

            // Revalider le JScrollPane pour refléter la nouvelle taille
            scrollPane.revalidate();
            scrollPane.repaint();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des vaccins : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
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