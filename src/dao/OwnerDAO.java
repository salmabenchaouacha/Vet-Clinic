package dao;

import model.Owner;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public interface OwnerDAO {
    public void addOwner(Owner owner) throws Exception;

    public List<Owner> getAllOwners() throws Exception;

    public void updateOwner(Owner owner) throws Exception;

    public void deleteOwner(int id) throws Exception;

    public Owner getOwnerById(int id) throws Exception;
}