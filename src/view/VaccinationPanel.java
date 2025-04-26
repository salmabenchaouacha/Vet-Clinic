package view;

import model.Animal;
import model.Owner;
import model.Vaccination;
import rmi.VeterinaryService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class VaccinationPanel extends JPanel {
    private VeterinaryService service;
    private JTable vaccinationTable;
    private DefaultTableModel tableModel;
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

    public VaccinationPanel(VeterinaryService service) {
        this.service = service;
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 220)); // Fond beige clair

        // Tableau des vaccins
        String[] columns = {"ID", "Animal", "Vaccin", "Date"};
        tableModel = new DefaultTableModel(columns, 0);
        vaccinationTable = new JTable(tableModel);
        vaccinationTable.setRowHeight(25);
        vaccinationTable.setFont(new Font("Arial", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(vaccinationTable);
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
        dialog.getContentPane().setBackground(new Color(240, 248, 255));

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formPanel.setBackground(new Color(240, 248, 255));

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
            List<Owner> owners = service.getAllOwners();
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
                    List<Animal> animals = service.getAllAnimals().stream()
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
                Animal selectedAnimal = service.getAllAnimals().stream()
                        .filter(a -> a.getId() == vaccination.getAnimalId())
                        .findFirst()
                        .orElse(null);
                if (selectedAnimal != null) {
                    Owner selectedOwner = service.getAllOwners().stream()
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
        styleButton(saveButton);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(240, 248, 255));
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
            List<Animal> animals = service.getAllAnimals();
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
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des vaccins : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void styleButton(JButton button) {
        button.setBackground(new Color(60, 179, 113));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    }
}