package model;

import java.io.Serializable;

public class Owner implements Serializable {
    private int id;
    private String fullName;

    private String phone;
    private String email;

    public Owner(int id, String fullName, String phone, String email) {
        this.id = id;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
    }


    // Getters
    public int getId() {
        return id;
    }







    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    // Setters




    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Owner{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
    public String getFirstName() {
        if (fullName == null || fullName.isEmpty()) return "";
        String[] parts = fullName.split(" ", 2);
        return parts[0];
    }

    public String getLastName() {
        if (fullName == null || fullName.isEmpty()) return "";
        String[] parts = fullName.split(" ", 2);
        return parts.length > 1 ? parts[1] : "";
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}