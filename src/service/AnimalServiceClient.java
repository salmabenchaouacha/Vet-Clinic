package service;

import rmi.AnimalRemote;
import model.Animal;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.List;

public class AnimalServiceClient {
    private AnimalRemote animalService;

    public AnimalServiceClient() {
        try {
            animalService = (AnimalRemote) Naming.lookup("rmi://localhost:1099/animalService");
            System.out.println("Connected to AnimalService");
        } catch (Exception e) {
            System.err.println("Error connecting to AnimalRemote: " + e.getMessage());
        }
    }

    public boolean addAnimal(Animal animal) {

        try {
            animalService.addAnimal(animal);
            System.out.println("Animal added successfully.");
            return true;
        } catch (RemoteException e) {
            System.err.println("RemoteException during addAnimal: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Exception during addAnimal: " + e.getMessage());
            return false;
        }
    }

    public List<Animal> getAllAnimalsByOwner() {
        try {
            return animalService.getAllAnimalsByOwner();
        } catch (RemoteException e) {
            System.err.println("RemoteException during getAllAnimalsByOwner: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Exception during getAllAnimalsByOwner: " + e.getMessage());
            return null;
        }
    }

    public boolean updateAnimal(Animal animal) {
        try {
            animalService.updateAnimal(animal);
            System.out.println("Animal updated successfully.");
            return true;
        } catch (RemoteException e) {
            System.err.println("RemoteException during updateAnimal: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Exception during updateAnimal: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteAnimal(int animalId) {
        try {
            animalService.deleteAnimal(animalId);
            System.out.println("Animal deleted successfully.");
            return true;
        } catch (RemoteException e) {
            System.err.println("RemoteException during deleteAnimal: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Exception during deleteAnimal: " + e.getMessage());
            return false;
        }
    }

    public List<Animal> getAllAnimals() {
        try {
            return animalService.getAllAnimals();
        } catch (RemoteException e) {
            System.err.println("RemoteException during getAllAnimals: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Exception during getAllAnimals: " + e.getMessage());
            return null;
        }
    }
}
