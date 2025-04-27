package dao;

import model.Vaccination;

import java.util.List;

public interface VaccinationDAO {
    public List<Vaccination> getAllVaccinations() throws Exception;

    public void addVaccination(Vaccination vaccination) throws Exception;

    public void updateVaccination(Vaccination vaccination) throws Exception;

    public void deleteVaccination(int id) throws Exception;
}