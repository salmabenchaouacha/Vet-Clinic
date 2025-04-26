package dao;

import model.Visit;
import util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface VisitDAO {
    public List<Visit> getAllVisits() throws Exception;

    public void addVisit(Visit visit) throws Exception;

    public void updateVisit(Visit visit) throws Exception;

    public void deleteVisit(int id) throws Exception;
}