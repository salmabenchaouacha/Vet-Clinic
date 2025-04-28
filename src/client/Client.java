package client;

import service.VeterinarianServiceClient;
import view.SignInFrame;

public class Client {
    public static void main(String[] args) {
        try {
            VeterinarianServiceClient serviceClient = new VeterinarianServiceClient(); // On crée un service
            new SignInFrame(serviceClient).setVisible(true); // On passe le service au SignInFrame
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
