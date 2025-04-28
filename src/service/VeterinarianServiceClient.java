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

    public Veterinarian findVeterinarianByEmail(String email) {
        try {
            Veterinarian vet = veterinarianService.findByEmail(email);
            if (vet == null) {
                System.out.println("Aucun vétérinaire trouvé pour email : " + email);
            } else {
                System.out.println("Vétérinaire trouvé : " + vet.getFullName());
            }
            return vet;
        } catch (RemoteException e) {
            System.err.println("Erreur de communication RMI : " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Erreur inattendue : " + e.getMessage());
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

    public boolean authenticateVeterinarian(String email, String password) {
        try {
            return veterinarianService.authenticate(email, password);
        } catch (RemoteException e) {
            System.err.println("RemoteException during authenticateVeterinarian: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Exception during authenticateVeterinarian: " + e.getMessage());
            return false;
        }
    }
}
