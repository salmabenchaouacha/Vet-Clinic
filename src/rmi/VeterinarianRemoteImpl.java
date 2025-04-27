package rmi;

import dao.VeterinarianDAO;
import dao.VeterinarianDAOImpl;
import model.Veterinarian;

import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class VeterinarianRemoteImpl extends UnicastRemoteObject implements VeterinarianRemote {

    private VeterinarianDAO veterinarianDAO;

    public VeterinarianRemoteImpl() throws Exception {
        super();
        veterinarianDAO = new VeterinarianDAOImpl();
    }

    @Override
    public void save(Veterinarian veterinarian) throws Exception {
        veterinarianDAO.save(veterinarian);
    }

    @Override
    public void update(Veterinarian veterinarian) throws Exception {
        veterinarianDAO.update(veterinarian);
    }

    @Override
    public Veterinarian findByUsername(String username) throws Exception {
        return veterinarianDAO.findByUsername(username);
    }

    @Override
    public List<Veterinarian> findAll() throws Exception {
        return veterinarianDAO.findAll();
    }

    @Override
    public boolean authenticate(String username, String password) throws Exception {
        return veterinarianDAO.authenticate(username, password);
    }
}
