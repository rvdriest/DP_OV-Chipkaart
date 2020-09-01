package com.company;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdresDAOPsql implements AdresDAO {
    private Connection connection;

    public AdresDAOPsql(Connection connection) {
        this.connection = connection;
    }


    @Override
    public boolean save(Adres adres) {
        boolean isSuccess = false;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO adres (adres_id, postcode, huisnummer, straat, woonplaats, reiziger_id) VALUES(?, ?, ?, ?, ?, ?)");
            preparedStatement.setInt(1, adres.getId());
            preparedStatement.setString(2, adres.getPostcode());
            preparedStatement.setString(3, adres.getHuisnummer());
            preparedStatement.setString(4, adres.getStraat());
            preparedStatement.setString(5, adres.getWoonplaats());
            preparedStatement.setInt(6, adres.getReiziger().getId());
            int resultSet = preparedStatement.executeUpdate();
            isSuccess = true;
        }catch(SQLException ex) {
            System.err.println("[SQLException] Opslaan van adres niet geluklt: " + ex.getMessage());
        }
        return isSuccess;
    }

    @Override
    public boolean update(Adres adres) {
        boolean isSuccess = false;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE adres SET postcode=?, huisnummer=?, straat=?, woonplaats=? WHERE adres_id=?");
            preparedStatement.setString(1, adres.getPostcode());
            preparedStatement.setString(2, adres.getHuisnummer());
            preparedStatement.setString(3, adres.getStraat());
            preparedStatement.setString(4, adres.getWoonplaats());
            preparedStatement.setInt(5, adres.getId());
            int result = preparedStatement.executeUpdate();
            if(result > 0) {
                isSuccess = true;
            }
        }catch(SQLException ex) {
            System.err.println("[SQLException] Opslaan van adres niet geluklt: " + ex.getMessage());
        }
        return isSuccess;
    }

    @Override
    public boolean delete(Adres adres) {
        boolean isSuccess = false;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM adres WHERE adres_id=?");
            preparedStatement.setInt(1, adres.getId());
            int result = preparedStatement.executeUpdate();
            if(result > 0) {
                isSuccess = true;
            }
        }catch(SQLException ex) {
            System.err.println("[SQLException] Opslaan van adres niet geluklt: " + ex.getMessage());
        }
        return isSuccess;
    }

    @Override
    public Adres findByReiziger(Reiziger reiziger) {
        Adres adres = null;
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM adres WHERE reiziger_id=" + reiziger.getId());
            while(resultSet.next()) {
                adres = new Adres();
                adres.setId(resultSet.getInt("adres_id"));
                adres.setWoonplaats(resultSet.getString("woonplaats"));
                adres.setStraat(resultSet.getString("straat"));
                adres.setPostcode(resultSet.getString("postcode"));
                adres.setHuisnummer(resultSet.getString("huisnummer"));
                adres.setReiziger(reiziger);
            }
        }catch(SQLException ex) {
            System.err.println("[SQLException] Geen adres gevonden bij deze reiziger");
        }
        return adres;
    }

    @Override
    public List<Adres> findAll() {
        List<Adres> adressen = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM adres");
            while(resultSet.next()) {
                Adres adres = new Adres();

                adres.setId(resultSet.getInt("adres_id"));

                adres.setHuisnummer(resultSet.getString("huisnummer"));
                adres.setPostcode(resultSet.getString("postcode"));
                adres.setStraat(resultSet.getString("straat"));
                adres.setWoonplaats(resultSet.getString("woonplaats"));
                //TODO reiziger toevoegen

                adressen.add(adres);
            }
        }catch(SQLException ex) {
            System.err.println("[SQLException] Ophalen van alle adressen is niet gelukt: " + ex.getMessage());
        }
        return adressen;
    }
}
