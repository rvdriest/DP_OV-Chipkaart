package com.company;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaartDAOPsql implements OVChipkaartDAO {
    private Connection connection;
    private ReizigerDAO reizigerDAO;
    private ProductDAO productDAO;

    public OVChipkaartDAOPsql(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void setReizigerDAO(ReizigerDAO reizigerDAO) {
        this.reizigerDAO = reizigerDAO;
    }

    @Override
    public void setProductDAO(ProductDAO productDAO) {
        this.productDAO = productDAO;
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
                for(Product product : ovChipkaart.getProducten()) {
                    PreparedStatement insertRelationPreparedStatement = connection.prepareStatement("INSERT INTO ov_chipkaart_product (kaart_nummer, product_nummer, last_update) VALUES(?, ?, now())");
                    insertRelationPreparedStatement.setInt(1, ovChipkaart.getKaartnummer());
                    insertRelationPreparedStatement.setInt(2, product.getNummer());
                    insertRelationPreparedStatement.execute();
                    insertRelationPreparedStatement.close();
                }
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



            //Verwijder relaties die niet meer bestaan
            Statement productenNummersStatement = connection.createStatement();
            ResultSet productenNummers = productenNummersStatement.executeQuery("SELECT product_nummer FROM ov_chipkaart_product WHERE kaart_nummer=" + ovChipkaart.getKaartnummer());

            List<Integer> productNummers = new ArrayList<>();
            while(productenNummers.next()) {
                productNummers.add(productenNummers.getInt("product_nummer"));
            }
            //Delete
            for(int productNummer : productNummers) {
                boolean isFound = false;
                for(Product product : ovChipkaart.getProducten()) {
                    if(product.getNummer() == productNummer) isFound = true;
                }

                //Als product_nummer (uit database) niet voorkomt in ovChipkaart.getProducten();
                if(!isFound) {
                    //Verwijder relatie
                    PreparedStatement deleteRelationStatement = connection.prepareStatement("DELETE FROM ov_chipkaart_product WHERE kaart_nummer=? AND product_nummer=?");
                    deleteRelationStatement.setInt(1, ovChipkaart.getKaartnummer());
                    deleteRelationStatement.setInt(2, productNummer);
                    deleteRelationStatement.executeUpdate();
                    deleteRelationStatement.close();
                }
            }
            //Insert
            for(Product product : ovChipkaart.getProducten()) {
                boolean isFound = false;
                for(int productNummer : productNummers) {
                    if(productNummer == product.getNummer()) {
                        isFound = true;
                        break;
                    }
                }

                if(!isFound) {
                    PreparedStatement updateRelationStatement = connection.prepareStatement("INSERT INTO ov_chipkaart_product (kaart_nummer, product_nummer) VALUES (?, ?)");
                    updateRelationStatement.setInt(1, ovChipkaart.getKaartnummer());
                    updateRelationStatement.setInt(2, product.getNummer());
                    updateRelationStatement.executeUpdate();
                    updateRelationStatement.close();
                }
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
            PreparedStatement deleteRelationStatement = connection.prepareStatement("DELETE FROM ov_chipkaart_product WHERE kaart_nummer=?");
            deleteRelationStatement.setInt(1, ovChipkaart.getKaartnummer());
            deleteRelationStatement.executeUpdate();
            deleteRelationStatement.close();

            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM ov_chipkaart WHERE kaart_nummer=?");
            preparedStatement.setInt(1, ovChipkaart.getKaartnummer());
            int result = preparedStatement.executeUpdate();
            if(result > 0) {
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
                OVChipkaart ovChipkaart = new OVChipkaart(kaartnummer, geldigTot, klasse, saldo, reiziger);
                List<Product> producten = productDAO.findByOVChipkaart(ovChipkaart);
                for(Product product : producten) {
                    ovChipkaart.voegProductToe(product);
                }
                ovChipkaarten.add(ovChipkaart);
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

                OVChipkaart ovChipkaart = new OVChipkaart(kaartnummer, geldigTot, klasse, saldo, reiziger);
                List<Product> producten = productDAO.findByOVChipkaart(ovChipkaart);
                for(Product product : producten) {
                    ovChipkaart.voegProductToe(product);
                }
                ovChipkaarten.add(ovChipkaart);
            }
            statement.close();
            resultSet.close();
        }catch(SQLException ex) {
            System.err.println("[SQLException] Ophalen van alle OVChipkaarten is niet gelukt: " + ex.getMessage());
        }
        return ovChipkaarten;
    }

    @Override
    public List<OVChipkaart> findByProduct(Product product) {
        List<OVChipkaart> ovChipkaarten = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT ov_chipkaart.kaart_nummer, ov_chipkaart.geldig_tot, ov_chipkaart.klasse, ov_chipkaart.saldo, ov_chipkaart.reiziger_id FROM ov_chipkaart_product JOIN ov_chipkaart ON ov_chipkaart_product.kaart_nummer = ov_chipkaart.kaart_nummer WHERE ov_chipkaart_product.product_nummer = ?");
            preparedStatement.setInt(1, product.getNummer());
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()) {
                int kaartNummer = resultSet.getInt("kaart_nummer");
                Date geldigTot = resultSet.getDate("geldig_tot");
                int klasse = resultSet.getInt("klasse");
                double saldo = resultSet.getDouble("saldo");
                Reiziger reiziger = reizigerDAO.findById(resultSet.getInt("reiziger_id"));

                OVChipkaart ovChipkaart = new OVChipkaart(kaartNummer, geldigTot, klasse, saldo, reiziger);
                ovChipkaarten.add(ovChipkaart);
            }
        }catch(SQLException ex) {
            System.err.println("Ophalen van OVChipkaarten per product is fout gegaan: " + ex.getMessage());
        }
        return ovChipkaarten;
    }
}
