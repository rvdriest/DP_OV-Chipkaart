package com.company;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOPsql implements ProductDAO{
    private Connection connection;
    private OVChipkaartDAO ovChipkaartDAO;

    public ProductDAOPsql(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void setOVChipkaartDAO(OVChipkaartDAO ovChipkaartDAO) {
        this.ovChipkaartDAO = ovChipkaartDAO;
    }

    @Override
    public boolean save(Product product) {
        boolean isSuccess = false;
        try {
            PreparedStatement insertProductPreparedStatement = connection.prepareStatement("INSERT INTO product (product_nummer, naam, beschrijving, prijs) VALUES(?, ?, ?, ?)");
            insertProductPreparedStatement.setInt(1, product.getNummer());
            insertProductPreparedStatement.setString(2, product.getNaam());
            insertProductPreparedStatement.setString(3, product.getBeschrijving());
            insertProductPreparedStatement.setDouble(4, product.getPrijs());
            int resultSet = insertProductPreparedStatement.executeUpdate();
            if(resultSet > 0) {
                for(OVChipkaart ovChipkaart : product.getOvChipkaarten()) {
                    PreparedStatement insertRelationPreparedStatement = connection.prepareStatement("INSERT INTO ov_chipkaart_product (kaart_nummer, product_nummer, last_update) VALUES(?, ?, now())");
                    insertRelationPreparedStatement.setInt(1, ovChipkaart.getKaartnummer());
                    insertRelationPreparedStatement.setInt(2, product.getNummer());
                    insertRelationPreparedStatement.execute();
                    insertRelationPreparedStatement.close();
                }
                isSuccess = true;
            }
            insertProductPreparedStatement.close();
        }catch(SQLException ex) {
            System.err.println("[SQLException] Opslaan van product niet gelukt: " + ex.getMessage());
        }
        return isSuccess;
    }

    @Override
    public boolean update(Product product) {
        boolean isSuccess = false;
        try {
            //UPDATE product
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE product SET naam=?, beschrijving=?, prijs=? WHERE product_nummer=?");
            preparedStatement.setString(1, product.getNaam());
            preparedStatement.setString(2, product.getBeschrijving());
            preparedStatement.setDouble(3, product.getPrijs());
            preparedStatement.setInt(4, product.getNummer());
            int resultSet = preparedStatement.executeUpdate();
            preparedStatement.close();
            if(resultSet > 0) {
                isSuccess = true;
            }
            //Verwijder relaties die niet meer bestaan
            Statement ovChipkaartenNummersStatement = connection.createStatement();
            ResultSet ovChipkaartenNummers = ovChipkaartenNummersStatement.executeQuery("SELECT kaart_nummer FROM ov_chipkaart_product WHERE product_nummer=" + product.getNummer());

            List<Integer> kaartnummers = new ArrayList<>();
            while(ovChipkaartenNummers.next()) {
                kaartnummers.add(ovChipkaartenNummers.getInt("kaart_nummer"));
            }

            for(int kaartNummer : kaartnummers) {
                boolean isFound = false;
                for(OVChipkaart ovChipkaart : product.getOvChipkaarten()) {
                    if(ovChipkaart.getKaartnummer() == kaartNummer) {
                        isFound = true;
                        break;
                    }
                }
                //Als ovchipkaart_nummer (uit database) niet voorkomt in product.getOvChipkaarten();
                if(!isFound) {
                    //Verwijder relatie
                    PreparedStatement deleteRelationStatement = connection.prepareStatement("DELETE FROM ov_chipkaart_product WHERE kaart_nummer=? AND product_nummer=?");
                    deleteRelationStatement.setInt(1, kaartNummer);
                    deleteRelationStatement.setInt(2, product.getNummer());
                    deleteRelationStatement.executeUpdate();
                    deleteRelationStatement.close();
                }
            }

            //INSERT relaties als niet bestaat
            for(OVChipkaart ovChipkaart : product.getOvChipkaarten()) {
                boolean isFound = false;
                for(int kaartNummer : kaartnummers) {
                    if(ovChipkaart.getKaartnummer() == kaartNummer) {
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

        }catch(SQLException ex) {
            System.err.println("[SQLException] Updaten van product niet gelukt: " + ex.getMessage());
        }
        return isSuccess;
    }

    @Override
    public boolean delete(Product product) {
        boolean isSuccess = false;
        try {
            // Delete relation (OVChipkaart - Product)
            PreparedStatement deleteRelationStatement = connection.prepareStatement("DELETE FROM ov_chipkaart_product WHERE product_nummer = ?");
            deleteRelationStatement.setInt(1, product.getNummer());
            deleteRelationStatement.executeUpdate();
            deleteRelationStatement.close();

            // Delete product
            PreparedStatement deleteProductStatement = connection.prepareStatement("DELETE FROM product WHERE product_nummer=?");
            deleteProductStatement.setInt(1, product.getNummer());
            int resultSet = deleteProductStatement.executeUpdate();
            if(resultSet > 0) {
                isSuccess = true;
            }
            deleteProductStatement.close();
        }catch(SQLException ex) {
            System.err.println("[SQLException] Verwijderen van product niet gelukt: " + ex.getMessage());
        }
        return isSuccess;
    }

    @Override
    public List<Product> findByOVChipkaart(OVChipkaart ovChipkaart) {
        List<Product> producten = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT product.product_nummer, product.naam, product.beschrijving, product.prijs FROM ov_chipkaart_product JOIN product ON ov_chipkaart_product.product_nummer = product.product_nummer WHERE ov_chipkaart_product.kaart_nummer = ?");
            preparedStatement.setInt(1, ovChipkaart.getKaartnummer());
            ResultSet products = preparedStatement.executeQuery(); //Haal alle producten op
            while (products.next()) {
                int productNummer = products.getInt("product_nummer");
                String naam = products.getString("naam");
                String beschrijving = products.getString("beschrijving");
                double prijs = products.getDouble("prijs");
                Product product = new Product(productNummer, naam, beschrijving, prijs);
                producten.add(product);
            }
        }catch(SQLException ex) {
            System.err.println("Er is iets fout gegaan bij het ophalen van alle producten: " + ex.getMessage());
        }
        return producten;
    }

    @Override
    public List<Product> findAll() {
        List<Product> producten = new ArrayList<>();
        try {
            Statement statement = connection.createStatement();
            ResultSet products = statement.executeQuery("SELECT * FROM product"); //Haal alle producten op
            while (products.next()) {
                int productNummer = products.getInt("product_nummer");
                String naam = products.getString("naam");
                String beschrijving = products.getString("beschrijving");
                double prijs = products.getDouble("prijs");
                Product product = new Product(productNummer, naam, beschrijving, prijs);

                List<OVChipkaart> ovChipkaarten = ovChipkaartDAO.findByProduct(product);
                for(OVChipkaart ovChipkaart : ovChipkaarten) {
                    product.voegOvChipkaartToe(ovChipkaart);
                }
                producten.add(product);
            }
            statement.close();
            products.close();
        }catch(SQLException ex) {
            System.err.println("Er is iets fout gegaan bij het ophalen van alle producten: " + ex.getMessage());
        }
        return producten;
    }
}
