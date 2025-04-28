package dao;

import model.Veterinarian;

import java.rmi.RemoteException;
import java.util.List;

public interface VeterinarianDAO {
    void save(Veterinarian veterinarian) throws Exception;
    void update(Veterinarian veterinarian) throws Exception;
    Veterinarian findByEmail(String email) throws Exception;
    List<Veterinarian> findAll() throws Exception;
    boolean authenticate(String email, String password) throws Exception;
}
