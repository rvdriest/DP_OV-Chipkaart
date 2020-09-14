package com.company;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReizigerDAOPsql implements ReizigerDAO {
    private Connection connection;
    private AdresDAO adresDAO;
    private OVChipkaartDAO ovChipkaartDAO;

    public ReizigerDAOPsql(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void setAdresDAO(AdresDAO adresDAO) {
        this.adresDAO = adresDAO;
    }

    @Override
    public void setOVChipkaartDAO(OVChipkaartDAO ovChipkaartDAO) {
        this.ovChipkaartDAO = ovChipkaartDAO;
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
                if(reiziger.getAdres() != null) {
                    if(adresDAO.findByReiziger(reiziger) == null) {
                        adresDAO.save(reiziger.getAdres());
                    }else {
                        adresDAO.update(reiziger.getAdres());
                    }
                }
                if(reiziger.getOvChipkaarten().size() > 0) {
                    reiziger.getOvChipkaarten().forEach(ovChipkaart -> ovChipkaartDAO.save(ovChipkaart));
                }
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

            boolean isAdresUpdated = false;
            if(reiziger.getAdres() != null) {
                if(adresDAO.findByReiziger(reiziger) == null) {
                    isAdresUpdated = adresDAO.save(reiziger.getAdres());
                }else {
                    isAdresUpdated = adresDAO.update(reiziger.getAdres());
                }
            }

            boolean isOVChipkaartUpdated = false;
            for(OVChipkaart ovChipkaart : reiziger.getOvChipkaarten()) {
                if(ovChipkaartDAO.update(ovChipkaart)) isOVChipkaartUpdated = true;
            }
            if(result > 0 || isAdresUpdated || isOVChipkaartUpdated) {
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
            ovChipkaartDAO.findByReiziger(reiziger).forEach(ovChipkaart -> ovChipkaartDAO.delete(ovChipkaart));
            Adres adres = adresDAO.findByReiziger(reiziger);
            if(adres != null) {
                adresDAO.delete(adres);
            }
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
                reiziger.setOvChipkaarten(ovChipkaartDAO.findByReiziger(reiziger));
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
                reiziger.setOvChipkaarten(ovChipkaartDAO.findByReiziger(reiziger));
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
                reiziger.setOvChipkaarten(ovChipkaartDAO.findByReiziger(reiziger));
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
