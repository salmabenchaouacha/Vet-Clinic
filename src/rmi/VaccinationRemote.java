package rmi;

import model.Vaccination;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface VaccinationRemote extends Remote {
    List<Vaccination> getAllVaccinations() throws RemoteException, Exception;

    void addVaccination(Vaccination vaccination) throws RemoteException, Exception;

    void updateVaccination(Vaccination vaccination) throws RemoteException, Exception;

    void deleteVaccination(int id) throws RemoteException, Exception;
}
