package com.company;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ProductDAOPsql implements ProductDAO{
    private Connection connection;

    public ProductDAOPsql(Connection connection) {
        this.connection = connection;
    }

    @Override
    public boolean save(Product product) {
        boolean isSuccess = false;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO product (product_nummer, naam, beschrijving, prijs) VALUES(?, ?, ?, ?)");
            preparedStatement.setInt(1, product.getNummer());
            preparedStatement.setString(2, product.getNaam());
            preparedStatement.setString(3, product.getBeschrijving());
            preparedStatement.setDouble(4, product.getPrijs());
            int resultSet = preparedStatement.executeUpdate();
            if(resultSet > 0) {
                isSuccess = true;
            }
            preparedStatement.close();
        }catch(SQLException ex) {
            System.err.println("[SQLException] Opslaan van product niet gelukt: " + ex.getMessage());
        }
        return isSuccess;
    }

    @Override
    public boolean update(Product product) {
        boolean isSuccess = false;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE product SET naam=?, beschrijving=?, prijs=? WHERE product_nummer=?");

            preparedStatement.setString(1, product.getNaam());
            preparedStatement.setString(2, product.getBeschrijving());
            preparedStatement.setDouble(3, product.getPrijs());
            preparedStatement.setInt(4, product.getNummer());
            int resultSet = preparedStatement.executeUpdate();
            if(resultSet > 0) {
                isSuccess = true;
            }
            preparedStatement.close();
        }catch(SQLException ex) {
            System.err.println("[SQLException] Updaten van product niet gelukt: " + ex.getMessage());
        }
        return isSuccess;
    }

    @Override
    public boolean delete(Product product) {
        boolean isSuccess = false;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM product WHERE product_nummer=?");
            preparedStatement.setInt(1, product.getNummer());
            int resultSet = preparedStatement.executeUpdate();
            if(resultSet > 0) {
                isSuccess = true;
            }
            preparedStatement.close();
        }catch(SQLException ex) {
            System.err.println("[SQLException] Verwijderen van product niet gelukt: " + ex.getMessage());
        }
        return isSuccess;
    }
}
