package rmi;

import model.Animal;
import model.Owner;
import model.Vaccination;
import model.Visit;
import model.Veterinarian;
import model.ChatMessage;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface VeterinaryService extends Remote {
    List<Animal> getAllAnimals() throws RemoteException;
    void addAnimal(Animal animal) throws RemoteException;
    void updateAnimal(Animal animal) throws RemoteException;
    void deleteAnimal(int id) throws RemoteException;

    List<Owner> getAllOwners() throws RemoteException;
    void addOwner(Owner owner) throws RemoteException;
    void updateOwner(Owner owner) throws RemoteException;
    void deleteOwner(int id) throws RemoteException;

    List<Vaccination> getAllVaccinations() throws RemoteException;
    void addVaccination(Vaccination vaccination) throws RemoteException;
    void updateVaccination(Vaccination vaccination) throws RemoteException;
    void deleteVaccination(int id) throws RemoteException;

    List<Visit> getAllVisits() throws RemoteException;
    void addVisit(Visit visit) throws RemoteException;
    void updateVisit(Visit visit) throws RemoteException;
    void deleteVisit(int id) throws RemoteException;

    // Méthodes pour la gestion des vétérinaires
    boolean signIn(String username, String password) throws RemoteException;
    void signUp(Veterinarian veterinarian) throws RemoteException;
    Veterinarian getVeterinarianByUsername(String username) throws RemoteException;
    void updateVeterinarian(Veterinarian veterinarian) throws RemoteException;
    List<Veterinarian> getAllVeterinarians() throws RemoteException;

    // Nouvelles méthodes pour le chat
    void sendChatMessage(String senderUsername, String receiverUsername, String content) throws RemoteException;
    List<ChatMessage> getChatMessages(String user1, String user2) throws RemoteException;
    List<ChatMessage> getUnreadMessages(String username) throws RemoteException;
}