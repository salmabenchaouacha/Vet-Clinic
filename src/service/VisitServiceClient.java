package service;

import rmi.VisitRemote;
import model.Visit;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.List;

public class VisitServiceClient {
    private VisitRemote visitService;

    public VisitServiceClient() {
        try {
            visitService = (VisitRemote) Naming.lookup("rmi://localhost:1099/visitService");
            System.out.println("Connected to VisitService");
        } catch (Exception e) {
            System.err.println("Error connecting to VisitRemote: " + e.getMessage());
        }
    }

    public boolean addVisit(Visit visit) {
        try {
            visitService.addVisit(visit);
            System.out.println("Visit added successfully.");
            return true;
        } catch (RemoteException e) {
            System.err.println("RemoteException during addVisit: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Exception during addVisit: " + e.getMessage());
            return false;
        }
    }

    public boolean updateVisit(Visit visit) {
        try {
            visitService.updateVisit(visit);
            System.out.println("Visit updated successfully.");
            return true;
        } catch (RemoteException e) {
            System.err.println("RemoteException during updateVisit: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Exception during updateVisit: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteVisit(int id) {
        try {
            visitService.deleteVisit(id);
            System.out.println("Visit deleted successfully.");
            return true;
        } catch (RemoteException e) {
            System.err.println("RemoteException during deleteVisit: " + e.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Exception during deleteVisit: " + e.getMessage());
            return false;
        }
    }

    public List<Visit> getAllVisits() {
        try {
            return visitService.getAllVisits();
        } catch (RemoteException e) {
            System.err.println("RemoteException during getAllVisits: " + e.getMessage());
            return null;
        } catch (Exception e) {
            System.err.println("Exception during getAllVisits: " + e.getMessage());
            return null;
        }
    }
}
