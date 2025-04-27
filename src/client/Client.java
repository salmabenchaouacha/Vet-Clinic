package client;

import view.SignInFrame;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Client {
    public static void main(String[] args) {
        try {
            new SignInFrame().setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}