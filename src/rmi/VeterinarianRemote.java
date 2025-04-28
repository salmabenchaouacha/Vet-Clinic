package rmi;

import model.Veterinarian;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface VeterinarianRemote extends Remote {
    void save(Veterinarian veterinarian) throws RemoteException, Exception;

    void update(Veterinarian veterinarian) throws RemoteException, Exception;

    Veterinarian findByEmail(String username) throws RemoteException, Exception;

    List<Veterinarian> findAll() throws RemoteException, Exception;

    boolean authenticate(String username, String password) throws RemoteException, Exception;


}
