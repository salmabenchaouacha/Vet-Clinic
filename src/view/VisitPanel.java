package view;

import model.Animal;
import model.Owner;
import model.Visit;
import rmi.VeterinaryService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

public class VisitPanel extends JPanel {
    private VeterinaryService service;
    private JTable visitTable;
    private DefaultTableModel tableModel;

    public VisitPanel(VeterinaryService service) {
        this.service = service;
        setLayout(new BorderLayout());
        setBackground(new Color(245, 245, 220)); // Fond beige clair

        // Tableau des visites
        String[] columns = {"ID", "Animal", "Date", "Raison", "Notes"};
        tableModel = new DefaultTableModel(columns, 0);
        visitTable = new JTable(tableModel);
        visitTable.setRowHeight(25);
        visitTable.setFont(new Font("Arial", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(visitTable);
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
        addButton.addActionListener(e -> showVisitDialog(null));

        // Action modifier
        updateButton.addActionListener(e -> {
            int selectedRow = visitTable.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                int animalId = Integer.parseInt(tableModel.getValueAt(selectedRow, 1).toString().split(" - ")[0]);
                String date = (String) tableModel.getValueAt(selectedRow, 2);
                String reason = (String) tableModel.getValueAt(selectedRow, 3);
                String notes = (String) tableModel.getValueAt(selectedRow, 4);
                Visit visit = new Visit(id, animalId, date, reason, notes);
                showVisitDialog(visit);
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une visite", "Erreur", JOptionPane.WARNING_MESSAGE);
            }
        });

        // Action supprimer
        deleteButton.addActionListener(e -> {
            int selectedRow = visitTable.getSelectedRow();
            if (selectedRow >= 0) {
                int id = (int) tableModel.getValueAt(selectedRow, 0);
                int confirm = JOptionPane.showConfirmDialog(this, "Supprimer cette visite ?", "Confirmation", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        service.deleteVisit(id);
                        refreshTable();
                        JOptionPane.showMessageDialog(this, "Visite supprimée avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner une visite", "Erreur", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    private void showVisitDialog(Visit visit) {
        JDialog dialog = new JDialog((Frame) null, visit == null ? "Ajouter Visite" : "Modifier Visite", true);
        dialog.setSize(400, 350);
        dialog.setLocationRelativeTo(this);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.getContentPane().setBackground(new Color(240, 248, 255));

        JPanel formPanel = new JPanel(new GridLayout(6, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        formPanel.setBackground(new Color(240, 248, 255));

        // Liste des propriétaires
        JComboBox<String> ownerCombo = new JComboBox<>();
        JComboBox<String> animalCombo = new JComboBox<>();
        JTextField dateField = new JTextField(visit != null ? visit.getDate() : "");
        JTextField reasonField = new JTextField(visit != null ? visit.getReason() : "");
        JTextField notesField = new JTextField(visit != null ? visit.getNotes() : "");

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
        if (visit != null) {
            try {
                Animal selectedAnimal = service.getAllAnimals().stream()
                        .filter(a -> a.getId() == visit.getAnimalId())
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
        formPanel.add(new JLabel("Date (YYYY-MM-DD) :"));
        formPanel.add(dateField);
        formPanel.add(new JLabel("Raison :"));
        formPanel.add(reasonField);
        formPanel.add(new JLabel("Notes :"));
        formPanel.add(notesField);

        JButton saveButton = new JButton("Enregistrer");
        styleButton(saveButton);
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(240, 248, 255));
        buttonPanel.add(saveButton);
        dialog.add(formPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);

        saveButton.addActionListener(e -> {
            if (animalCombo.getSelectedItem() == null || dateField.getText().trim().isEmpty() || reasonField.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(dialog, "Veuillez remplir tous les champs obligatoires", "Erreur", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                String animalItem = (String) animalCombo.getSelectedItem();
                int animalId = Integer.parseInt(animalItem.split(" - ")[0]);
                Visit newVisit = new Visit(
                        visit != null ? visit.getId() : 0,
                        animalId,
                        dateField.getText().trim(),
                        reasonField.getText().trim(),
                        notesField.getText().trim()
                );
                if (visit == null) {
                    service.addVisit(newVisit);
                    JOptionPane.showMessageDialog(dialog, "Visite ajoutée avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    service.updateVisit(newVisit);
                    JOptionPane.showMessageDialog(dialog, "Visite modifiée avec succès", "Succès", JOptionPane.INFORMATION_MESSAGE);
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
            List<Visit> visits = service.getAllVisits();
            List<Animal> animals = service.getAllAnimals();
            for (Visit visit : visits) {
                String animalName = animals.stream()
                        .filter(a -> a.getId() == visit.getAnimalId())
                        .findFirst()
                        .map(a -> a.getId() + " - " + a.getName())
                        .orElse("Inconnu");
                tableModel.addRow(new Object[]{
                        visit.getId(),
                        animalName,
                        visit.getDate(),
                        visit.getReason(),
                        visit.getNotes()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des visites : " + e.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
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