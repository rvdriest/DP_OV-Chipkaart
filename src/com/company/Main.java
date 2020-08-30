package com.company;

import java.sql.*;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        String connectionUrl = "jdbc:postgresql://localhost:5432/ovchip?user=postgres&password=W8wo0rd@01";
        try {
            Connection connection = DriverManager.getConnection(connectionUrl);
            ReizigerDAOPsql reizigerDAO = new ReizigerDAOPsql(connection);

            testReizigerDAO(reizigerDAO);
        }catch(SQLException sqlException) {
            System.err.println("[SQLException] Reizigers konden niet worden opgehaald: " + sqlException.getMessage());
        }


    }

    /**
     * P2. Reiziger DAO: persistentie van een klasse
     *
     * Deze methode test de CRUD-functionaliteit van de Reiziger DAO
     *
     * @throws SQLException
     */
    private static void testReizigerDAO(ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- Test ReizigerDAO -------------");

        // Haal alle reizigers op uit de database
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        // Maak een nieuwe reiziger aan en persisteer deze in de database
        String gbdatum = "1981-03-14";
        Reiziger sietske = new Reiziger(77, "S", "", "Boers", java.sql.Date.valueOf(gbdatum));
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        rdao.save(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");

        // Haal reiziger op met id 2
        int reizigerId = 2;
        System.out.println("[Test] Gebruiker met id 2: ");
        System.out.println(rdao.findById(2));
        System.out.println();

        // Haal reizigers op met 2002-12-03 als geboortedatum
        String geboorteDatum = "2002-12-03";
        reizigers = rdao.findByGbDatum(geboorteDatum);
        System.out.println("[Test] Alle gebruikers met als geboortedatum 2002-12-03:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        // Verwijder gebruiker met id 77
        System.out.print("[Test] Eerst " + rdao.findAll().size() + " reizigers, ");
        rdao.delete(rdao.findById(77));
        System.out.println("na verwijderen van gebruiker: " + rdao.findAll().size());
        System.out.println();

        //Update gebruiker 1. Verander voorletter van G naar R
        Reiziger reiziger = rdao.findById(1);
        System.out.print("[Test] Voorletter is eerst " + reiziger.getVoorletters());
        reiziger.setVoorletters("R");
        rdao.update(reiziger);
        System.out.println(", na update is zijn de voorletters: " + rdao.findById(1).getVoorletters());
        System.out.println();

    }
}