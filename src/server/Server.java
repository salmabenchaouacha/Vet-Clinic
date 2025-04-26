package server;

import rmi.VeterinaryService;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
    public static void main(String[] args) {
        try {
            VeterinaryService service = new VeterinaryServiceImpl();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("VeterinaryService", service);
            System.out.println("Serveur RMI démarré sur le port 1099...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}