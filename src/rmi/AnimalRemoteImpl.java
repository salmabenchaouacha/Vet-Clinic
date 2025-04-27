package rmi;

import dao.AnimalDAO;
import dao.AnimalDAOImpl;
import model.Animal;

import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class AnimalRemoteImpl extends UnicastRemoteObject implements AnimalRemote {

    private AnimalDAO animalDAO;

    public AnimalRemoteImpl() throws Exception {
        super();
        animalDAO = new AnimalDAOImpl();
    }

    @Override
    public void addAnimal(Animal animal) throws Exception {
        animalDAO.addAnimal(animal);
    }

    @Override
    public List<Animal> getAllAnimalsByOwner() throws Exception {
        return animalDAO.getAllAnimalsByOwner();
    }

    @Override
    public void updateAnimal(Animal animal) throws Exception {
        animalDAO.updateAnimal(animal);
    }

    @Override
    public void deleteAnimal(int id) throws Exception {
        animalDAO.deleteAnimal(id);
    }

    @Override
    public List<Animal> getAllAnimals() throws Exception {
        return animalDAO.getAllAnimals();
    }
}
