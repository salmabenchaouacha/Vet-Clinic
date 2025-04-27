package rmi;

import model.Visit;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface VisitRemote extends Remote {
    List<Visit> getAllVisits() throws RemoteException, Exception;

    void addVisit(Visit visit) throws RemoteException, Exception;

    void updateVisit(Visit visit) throws RemoteException, Exception;

    void deleteVisit(int id) throws RemoteException, Exception;
}
