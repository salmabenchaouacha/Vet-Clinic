package rmi;

import dao.VaccinationDAO;
import dao.VaccinationDAOImpl;
import model.Vaccination;

import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class VaccinationRemoteImpl extends UnicastRemoteObject implements VaccinationRemote {

    private VaccinationDAO vaccinationDAO;

    public VaccinationRemoteImpl() throws Exception {
        super();
        vaccinationDAO = new VaccinationDAOImpl();
    }

    @Override
    public List<Vaccination> getAllVaccinations() throws Exception {
        return vaccinationDAO.getAllVaccinations();
    }

    @Override
    public void addVaccination(Vaccination vaccination) throws Exception {
        vaccinationDAO.addVaccination(vaccination);
    }

    @Override
    public void updateVaccination(Vaccination vaccination) throws Exception {
        vaccinationDAO.updateVaccination(vaccination);
    }

    @Override
    public void deleteVaccination(int id) throws Exception {
        vaccinationDAO.deleteVaccination(id);
    }
}
