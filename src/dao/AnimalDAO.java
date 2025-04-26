package dao;

import model.Animal;

import java.rmi.RemoteException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public interface AnimalDAO {
    public void addAnimal(Animal animal) throws Exception ;

    public List<Animal> getAllAnimalsByOwner() throws Exception;

    public void updateAnimal(Animal animal) throws Exception;

    public void deleteAnimal(int id) throws Exception;

    public List<Animal> getAllAnimals() throws Exception;
}