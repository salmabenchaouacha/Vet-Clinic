package server;

import rmi.AnimalRemoteImpl;
import rmi.ChatMessageRemoteImpl;
import rmi.OwnerRemoteImpl;
import rmi.VaccinationRemoteImpl;
import rmi.VeterinarianRemoteImpl;
import rmi.VisitRemoteImpl;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.createRegistry(1099);
            System.out.println("Serveur RMI démarré sur le port 1099...");

            registry.rebind("animalService", new AnimalRemoteImpl());

            registry.rebind("chatService", new ChatMessageRemoteImpl());

            registry.rebind("ownerService", new OwnerRemoteImpl());

            registry.rebind("vaccinationService", new VaccinationRemoteImpl());

            registry.rebind("veterinarianService", new VeterinarianRemoteImpl());

            registry.rebind("visitService", new VisitRemoteImpl());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
