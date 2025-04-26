package client;

import rmi.VeterinaryService;
import view.SignInFrame;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            VeterinaryService service = (VeterinaryService) registry.lookup("VeterinaryService");
            new SignInFrame(service).setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}