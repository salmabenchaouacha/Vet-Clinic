package model;

import java.io.Serializable;

public class Vaccination implements Serializable {
    private static final long serialVersionUID = 1L; // Bonne pratique pour la sérialisation

    private int id;
    private int animalId;
    private String vaccineName;
    private String date;

    public Vaccination(int id, int animalId, String vaccineName, String date) {
        this.id = id;
        this.animalId = animalId;
        this.vaccineName = vaccineName;
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAnimalId() {
        return animalId;
    }

    public void setAnimalId(int animalId) {
        this.animalId = animalId;
    }

    public String getVaccineName() {
        return vaccineName;
    }

    public void setVaccineName(String vaccineName) {
        this.vaccineName = vaccineName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}