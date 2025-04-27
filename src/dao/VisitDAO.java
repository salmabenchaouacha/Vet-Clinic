package dao;

import model.Visit;

import java.util.List;

public interface VisitDAO {
    public List<Visit> getAllVisits() throws Exception;

    public void addVisit(Visit visit) throws Exception;

    public void updateVisit(Visit visit) throws Exception;

    public void deleteVisit(int id) throws Exception;

}