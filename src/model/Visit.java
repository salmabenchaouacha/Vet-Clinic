package model;

import java.io.Serializable;

public class Visit implements Serializable {
    private int id;
    private int animalId;
    private String date;
    private String reason;
    private String notes;

    public Visit(int id, int animalId, String date, String reason, String notes) {
        this.id = id;
        this.animalId = animalId;
        this.date = date;
        this.reason = reason;
        this.notes = notes;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}