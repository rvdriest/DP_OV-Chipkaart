package com.company;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaartDAOPsql implements OVChipkaartDAO {
    private Connection connection;
    private ReizigerDAO reizigerDAO;

    public OVChipkaartDAOPsql(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void setReizigerDAO(ReizigerDAO reizigerDAO) {
        this.reizigerDAO = reizigerDAO;
    }

    @Override
    public boolean save(OVChipkaart ovChipkaart) {
        boolean isSuccess = false;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO ov_chipkaart (kaart_nummer, geldig_tot, klasse, saldo, reiziger_id) VALUES(?, ?, ?, ?, ?)");
            preparedStatement.setInt(1, ovChipkaart.getKaartnummer());
            preparedStatement.setDate(2, ovChipkaart.getGeldigTot());
            preparedStatement.setInt(3, ovChipkaart.getKlasse());
            preparedStatement.setDouble(4, ovChipkaart.getSaldo());
            preparedStatement.setInt(5, ovChipkaart.getReiziger().getId());
            int resultSet = preparedStatement.executeUpdate();
            if(resultSet > 0) {
                isSuccess = true;
            }
            preparedStatement.close();
        }catch(SQLException ex) {
            System.err.println("[SQLException] Opslaan van OVChipkaart niet gelukt: " + ex.getMessage());
        }
        return isSuccess;
    }

    @Override
    public boolean update(OVChipkaart ovChipkaart) {
        boolean isSuccess = false;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE ov_chipkaart SET reiziger_id=?, geldig_tot=?, klasse=?, saldo=? WHERE kaart_nummer=?");
            preparedStatement.setInt(1, ovChipkaart.getReiziger().getId());
            preparedStatement.setDate(2, ovChipkaart.getGeldigTot());
            preparedStatement.setInt(3, ovChipkaart.getKlasse());
            preparedStatement.setDouble(4, ovChipkaart.getSaldo());
            preparedStatement.setInt(5, ovChipkaart.getKaartnummer());

            int resultSet = preparedStatement.executeUpdate();
            if(resultSet > 0) {
                isSuccess = true;
            }
            preparedStatement.close();
        }catch(SQLException ex) {
            System.err.println("[SQLException] Opslaan van OVChipkaart niet gelukt: " + ex.getMessage());
        }
        return isSuccess;
    }

    @Override
    public boolean delete(OVChipkaart ovChipkaart) {
        boolean isSuccess = false;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM ov_chipkaart WHERE kaart_nummer=?");
            preparedStatement.setInt(1, ovChipkaart.getKaartnummer());
            int result = preparedStatement.executeUpdate();
            if(result > 0) {
                ovChipkaart.getReiziger().removeOvChipkaart(ovChipkaart);
                isSuccess = true;
            }
            preparedStatement.close();
        }catch(SQLException ex) {
            System.err.println("[SQLException] Opslaan van OVChipkaart niet gelukt: " + ex.getMessage());
        }
        return isSuccess;
    }

    @Override
    public List<OVChipkaart> findByReiziger(Reiziger reiziger) {
        List<OVChipkaart> ovChipkaarten = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM ov_chipkaart WHERE reiziger_id=?;");
            preparedStatement.setInt(1, reiziger.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                int kaartnummer = resultSet.getInt("kaart_nummer");
                Date geldigTot = resultSet.getDate("geldig_tot");
                int klasse = resultSet.getInt("klasse");
                double saldo = resultSet.getDouble("saldo");

                ovChipkaarten.add(new OVChipkaart(kaartnummer, geldigTot, klasse, saldo, reiziger));
            }
            preparedStatement.close();
            resultSet.close();
        }catch(SQLException ex) {
            System.err.println("[SQLException] Ophalen van alle OVChipkaart is niet gelukt: " + ex.getMessage());
        }
        return ovChipkaarten;
    }

    @Override
    public List<OVChipkaart> findAll() {
        List<OVChipkaart> ovChipkaarten = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM ov_chipkaart");
            while(resultSet.next()) {
                int kaartnummer = resultSet.getInt("kaart_nummer");
                Date geldigTot = resultSet.getDate("geldig_tot");
                int klasse = resultSet.getInt("klasse");
                double saldo = resultSet.getDouble("saldo");
                Reiziger reiziger = reizigerDAO.findById(resultSet.getInt("reiziger_id"));
                ovChipkaarten.add(new OVChipkaart(kaartnummer, geldigTot, klasse, saldo, reiziger));
            }
            statement.close();
            resultSet.close();
        }catch(SQLException ex) {
            System.err.println("[SQLException] Ophalen van alle OVChipkaarten is niet gelukt: " + ex.getMessage());
        }
        return ovChipkaarten;
    }
}
