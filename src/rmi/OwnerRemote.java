package rmi;

import model.Owner;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface OwnerRemote extends Remote {
    void addOwner(Owner owner) throws RemoteException, Exception;

    List<Owner> getAllOwners() throws RemoteException, Exception;

    void updateOwner(Owner owner) throws RemoteException, Exception;

    void deleteOwner(int id) throws RemoteException, Exception;

    Owner getOwnerById(int id) throws RemoteException, Exception;

    List<Owner> searchOwnersByLastName(String lastName) throws RemoteException, Exception;
}
