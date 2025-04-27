package rmi;

import dao.VisitDAO;
import dao.VisitDAOImpl;
import model.Visit;

import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class VisitRemoteImpl extends UnicastRemoteObject implements VisitRemote {

    private VisitDAO visitDAO;

    public VisitRemoteImpl() throws Exception {
        super();
        visitDAO = new VisitDAOImpl();
    }

    @Override
    public List<Visit> getAllVisits() throws Exception {
        return visitDAO.getAllVisits();
    }

    @Override
    public void addVisit(Visit visit) throws Exception {
        visitDAO.addVisit(visit);
    }

    @Override
    public void updateVisit(Visit visit) throws Exception {
        visitDAO.updateVisit(visit);
    }

    @Override
    public void deleteVisit(int id) throws Exception {
        visitDAO.deleteVisit(id);
    }
}
