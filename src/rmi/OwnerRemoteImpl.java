package rmi;

import dao.OwnerDAO;
import dao.OwnerDAOImpl;
import model.Owner;

import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class OwnerRemoteImpl extends UnicastRemoteObject implements OwnerRemote {

    private OwnerDAO ownerDAO;

    public OwnerRemoteImpl() throws Exception {
        super();
        ownerDAO = new OwnerDAOImpl();
    }

    @Override
    public void addOwner(Owner owner) throws Exception {
        ownerDAO.addOwner(owner);
    }

    @Override
    public List<Owner> getAllOwners() throws Exception {
        return ownerDAO.getAllOwners();
    }

    @Override
    public void updateOwner(Owner owner) throws Exception {
        ownerDAO.updateOwner(owner);
    }

    @Override
    public void deleteOwner(int id) throws Exception {
        ownerDAO.deleteOwner(id);
    }

    @Override
    public Owner getOwnerById(int id) throws Exception {
        return ownerDAO.getOwnerById(id);
    }

    @Override
    public List<Owner> searchOwnersByLastName(String lastName) throws Exception {
        return ownerDAO.searchOwnersByLastName(lastName);
    }
}
