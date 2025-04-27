package service;

import rmi.VeterinarianRemote;
import model.Veterinarian;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.List;

public class VeterinarianServiceClient {
    private VeterinarianRemote veterinarianService;

    public VeterinarianServiceClient() {
        try {
            veterinarianService = (VeterinarianRemote) Naming.lookup("rmi://localhost:1099/veterinarianService");
            System.out.println("Connected to VeterinarianService");
        } catch (Exception e) {
            System.err.println("Error connecting to VeterinarianRemote: " + e.getMessage());
        }
    }

    public boolean saveVeterinarian(Veterinarian veterinarian) {
        try {
            veterinarianService.save(veterinarian);
            System.out.println("Veterinarian saved successfully.");
            return true;
        } catch (RemoteException e) {
            System.err.println("RemoteException during saveVeterinarian: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Exception during saveVeterinarian: " + e.getMessage());
            return false;
        }
    }

    public boolean updateVeterinarian(Veterinarian veterinarian) {
        try {
            veterinarianService.update(veterinarian);
            System.out.println("Veterinarian updated successfully.");
            return true;
        } catch (RemoteException e) {
            System.err.println("RemoteException during updateVeterinarian: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Exception during updateVeterinarian: " + e.getMessage());
            return false;
        }
    }

    public Veterinarian findVeterinarianByUsername(String username) {
        try {
            return veterinarianService.findByUsername(username);
        } catch (RemoteException e) {
            System.err.println("RemoteException during findVeterinarianByUsername: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Exception during findVeterinarianByUsername: " + e.getMessage());
            return null;
        }
    }

    public List<Veterinarian> findAllVeterinarians() {
        try {
            return veterinarianService.findAll();
        } catch (RemoteException e) {
            System.err.println("RemoteException during findAllVeterinarians: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Exception during findAllVeterinarians: " + e.getMessage());
            return null;
        }
    }

    public boolean authenticateVeterinarian(String username, String password) {
        try {
            return veterinarianService.authenticate(username, password);
        } catch (RemoteException e) {
            System.err.println("RemoteException during authenticateVeterinarian: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Exception during authenticateVeterinarian: " + e.getMessage());
            return false;
        }
    }
}
