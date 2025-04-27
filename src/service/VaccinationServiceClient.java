package service;

import rmi.VaccinationRemote;
import model.Vaccination;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.List;

public class VaccinationServiceClient {
    private VaccinationRemote vaccinationService;

    public VaccinationServiceClient() {
        try {
            vaccinationService = (VaccinationRemote) Naming.lookup("rmi://localhost:1099/vaccinationService");
            System.out.println("Connected to VaccinationService");
        } catch (Exception e) {
            System.err.println("Error connecting to VaccinationRemote: " + e.getMessage());
        }
    }

    public boolean addVaccination(Vaccination vaccination) {
        try {
            vaccinationService.addVaccination(vaccination);
            System.out.println("Vaccination added successfully.");
            return true;
        } catch (RemoteException e) {
            System.err.println("RemoteException during addVaccination: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Exception during addVaccination: " + e.getMessage());
            return false;
        }
    }

    public List<Vaccination> getAllVaccinations() {
        try {
            return vaccinationService.getAllVaccinations();
        } catch (RemoteException e) {
            System.err.println("RemoteException during getAllVaccinations: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Exception during getAllVaccinations: " + e.getMessage());
            return null;
        }
    }

    public boolean updateVaccination(Vaccination vaccination) {
        try {
            vaccinationService.updateVaccination(vaccination);
            System.out.println("Vaccination updated successfully.");
            return true;
        } catch (RemoteException e) {
            System.err.println("RemoteException during updateVaccination: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Exception during updateVaccination: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteVaccination(int id) {
        try {
            vaccinationService.deleteVaccination(id);
            System.out.println("Vaccination deleted successfully.");
            return true;
        } catch (RemoteException e) {
            System.err.println("RemoteException during deleteVaccination: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Exception during deleteVaccination: " + e.getMessage());
            return false;
        }
    }
}
