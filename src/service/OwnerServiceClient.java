package service;

import rmi.OwnerRemote;
import model.Owner;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.List;

public class OwnerServiceClient {
    private OwnerRemote ownerService;

    public OwnerServiceClient() {
        try {
            ownerService = (OwnerRemote) Naming.lookup("rmi://localhost:1099/ownerService");
            System.out.println("Connected to OwnerService");
        } catch (Exception e) {
            System.err.println("Error connecting to OwnerRemote: " + e.getMessage());
        }
    }

    public boolean addOwner(Owner owner) {
        try {
            ownerService.addOwner(owner);
            System.out.println("Owner added successfully.");
            return true;
        } catch (RemoteException e) {
            System.err.println("RemoteException during addOwner: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Exception during addOwner: " + e.getMessage());
            return false;
        }
    }

    public List<Owner> getAllOwners() {
        try {
            return ownerService.getAllOwners();
        } catch (RemoteException e) {
            System.err.println("RemoteException during getAllOwners: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Exception during getAllOwners: " + e.getMessage());
            return null;
        }
    }

    public boolean updateOwner(Owner owner) {
        try {
            ownerService.updateOwner(owner);
            System.out.println("Owner updated successfully.");
            return true;
        } catch (RemoteException e) {
            System.err.println("RemoteException during updateOwner: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Exception during updateOwner: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteOwner(int id) {
        try {
            ownerService.deleteOwner(id);
            System.out.println("Owner deleted successfully.");
            return true;
        } catch (RemoteException e) {
            System.err.println("RemoteException during deleteOwner: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Exception during deleteOwner: " + e.getMessage());
            return false;
        }
    }

    public Owner getOwnerById(int id) {
        try {
            return ownerService.getOwnerById(id);
        } catch (RemoteException e) {
            System.err.println("RemoteException during getOwnerById: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Exception during getOwnerById: " + e.getMessage());
            return null;
        }
    }

    public List<Owner> searchOwnersByLastName(String lastName) {
        try {
            return ownerService.searchOwnersByLastName(lastName);
        } catch (RemoteException e) {
            System.err.println("RemoteException during searchOwnersByLastName: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Exception during searchOwnersByLastName: " + e.getMessage());
            return null;
        }
    }
}
