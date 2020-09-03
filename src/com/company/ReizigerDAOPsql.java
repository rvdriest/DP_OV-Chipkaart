package com.company;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO {
    private Connection connection;
    private AdresDAO adresDAO;

    public ReizigerDAOPsql(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean save(Reiziger reiziger) {
        boolean isSuccess = false;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO reiziger (reiziger_id, voorletters, tussenvoegsel, achternaam, geboortedatum) VALUES(?, ?, ?, ?, ?)");
            preparedStatement.setInt(1, reiziger.getId());
            preparedStatement.setString(2, reiziger.getVoorletters());
            preparedStatement.setString(3, reiziger.getTussenvoegsel());
            preparedStatement.setString(4, reiziger.getAchternaam());
            preparedStatement.setDate(5, reiziger.getGeboorteDatum());
            int resultSet = preparedStatement.executeUpdate();
            if(resultSet > 0) {
                isSuccess = true;
            }
            preparedStatement.close();
        }catch(SQLException ex) {
            System.err.println("[SQLException] Opslaan van reiziger niet geluklt: " + ex.getMessage());
        }
        return isSuccess;
    }

    @Override
    public boolean update(Reiziger reiziger) {
        boolean isSuccess = false;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE reiziger SET voorletters=?, tussenvoegsel=?, achternaam=?, geboortedatum=? WHERE reiziger_id=?");
            preparedStatement.setString(1, reiziger.getVoorletters());
            preparedStatement.setString(2, reiziger.getTussenvoegsel());
            preparedStatement.setString(3, reiziger.getAchternaam());
            preparedStatement.setDate(4, reiziger.getGeboorteDatum());
            preparedStatement.setInt(5, reiziger.getId());
            int result = preparedStatement.executeUpdate();
            if(result > 0) {
                isSuccess = true;
            }
            preparedStatement.close();
        }catch(SQLException ex) {
            System.err.println("[SQLException] Opslaan van reiziger niet geluklt: " + ex.getMessage());
        }
        return isSuccess;
    }

    @Override
    public boolean delete(Reiziger reiziger) {
        boolean isSuccess = false;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM reiziger WHERE reiziger_id=?");
            preparedStatement.setInt(1, reiziger.getId());
            int result = preparedStatement.executeUpdate();
            if(result > 0) {
                isSuccess = true;
            }
            preparedStatement.close();
        }catch(SQLException ex) {
            System.err.println("[SQLException] Opslaan van reiziger niet geluklt: " + ex.getMessage());
        }
        return isSuccess;
    }

    @Override
    public Reiziger findById(int id) {
        adresDAO = new AdresDAOPsql(connection);
        Reiziger reiziger = null;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM reiziger WHERE reiziger_id=?;");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                int reizigerId = resultSet.getInt("reiziger_id");

                String voorletters = resultSet.getString("voorletters");
                String tussenvoegsel = resultSet.getString("tussenvoegsel");
                String achternaam = resultSet.getString("achternaam");

                Date geboorteDatum = resultSet.getDate("geboortedatum");

                reiziger = new Reiziger(reizigerId, voorletters, tussenvoegsel, achternaam, geboorteDatum);
                Adres adres = adresDAO.findByReiziger(reiziger);
                reiziger.setAdres(adres);
            }
            preparedStatement.close();
            resultSet.close();
        }catch(SQLException ex) {
            System.err.println("[SQLException] Ophalen van alle reiziger is niet gelukt: " + ex.getMessage());
        }
        return reiziger;
    }

    @Override
    public List<Reiziger> findByGbDatum(String datum) {
        adresDAO = new AdresDAOPsql(connection);
        List<Reiziger> reizigers = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM reiziger WHERE geboortedatum=?;");
            preparedStatement.setDate(1, Date.valueOf(datum));
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                int reizigerId = resultSet.getInt("reiziger_id");

                String voorletters = resultSet.getString("voorletters");
                String tussenvoegsel = resultSet.getString("tussenvoegsel");
                String achternaam = resultSet.getString("achternaam");

                Date geboorteDatum = resultSet.getDate("geboortedatum");

                Reiziger reiziger = new Reiziger(reizigerId, voorletters, tussenvoegsel, achternaam, geboorteDatum);
                Adres adres = adresDAO.findByReiziger(reiziger);
                reiziger.setAdres(adres);

                reizigers.add(reiziger);
            }
            preparedStatement.close();
            resultSet.close();
        }catch(SQLException ex) {
            System.err.println("[SQLException] Ophalen van alle reizigers is niet gelukt: " + ex.getMessage());
        }
        return reizigers;
    }

    @Override
    public List<Reiziger> findAll() {
        adresDAO = new AdresDAOPsql(connection);
        List<Reiziger> reizigers = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM reiziger");
            while(resultSet.next()) {
                int reizigerId = resultSet.getInt("reiziger_id");

                String voorletters = resultSet.getString("voorletters");
                String tussenvoegsel = resultSet.getString("tussenvoegsel");
                String achternaam = resultSet.getString("achternaam");

                Date geboorteDatum = resultSet.getDate("geboortedatum");

                Reiziger reiziger = new Reiziger(reizigerId, voorletters, tussenvoegsel, achternaam, geboorteDatum);
                Adres adres = adresDAO.findByReiziger(reiziger);
                reiziger.setAdres(adres);

                reizigers.add(reiziger);
            }
            statement.close();
            resultSet.close();
        }catch(SQLException ex) {
            System.err.println("[SQLException] Ophalen van alle reizigers is niet gelukt: " + ex.getMessage());
        }
        return reizigers;
    }
}
