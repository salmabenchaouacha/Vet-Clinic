package rmi;

import model.Animal;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface AnimalRemote extends Remote {
    void addAnimal(Animal animal) throws RemoteException, Exception;

    List<Animal> getAllAnimalsByOwner() throws RemoteException, Exception;

    void updateAnimal(Animal animal) throws RemoteException, Exception;

    void deleteAnimal(int id) throws RemoteException, Exception;

    List<Animal> getAllAnimals() throws RemoteException, Exception;
}
