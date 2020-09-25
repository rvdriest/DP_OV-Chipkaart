package com.company;

import java.sql.*;
import java.util.List;

public class Main {
    private static Connection connection = getConnection();
    public static void main(String[] args) {
            ReizigerDAO reizigerDAO = new ReizigerDAOPsql(connection);
            AdresDAO adresDAO = new AdresDAOPsql(connection);
            OVChipkaartDAO ovChipkaartDAO = new OVChipkaartDAOPsql(connection);
            ProductDAO productDAO = new ProductDAOPsql(connection);

            reizigerDAO.setAdresDAO(adresDAO);
            adresDAO.setReizigerDAO(reizigerDAO);
            ovChipkaartDAO.setReizigerDAO(reizigerDAO);
            ovChipkaartDAO.setProductDAO(productDAO);
            reizigerDAO.setOVChipkaartDAO(ovChipkaartDAO);
            productDAO.setOVChipkaartDAO(ovChipkaartDAO);

//            testReizigerDAO(reizigerDAO);
//            testAdresDAO(adresDAO);
            testOVChipkaartDAO(ovChipkaartDAO, reizigerDAO, productDAO);
//            testProductDAO(productDAO, ovChipkaartDAO);
            closeConnection();
    }


    private static Connection getConnection() {
        Connection newConnection = null;
        try {
            String connectionUrl = "jdbc:postgresql://localhost:5432/ovchip?user=postgres&password=123456";
            newConnection = DriverManager.getConnection(connectionUrl);
        }catch(SQLException sqlException) {
            System.err.println("[SQLException] Verbinden met database is mislukt: " + sqlException.getMessage());
        }
        return newConnection;
    }

    private static void closeConnection() {
        try {
            connection.close();
        }catch(SQLException sqlException) {
            System.err.println("[SQLException] Verbinding met database sluiten is mislukt: " + sqlException.getMessage());
        }
    }

    /**
     * P2. Reiziger DAO: persistentie van een klasse
     *
     * Deze methode test de CRUD-functionaliteit van de Reiziger DAO
     *
     */
    private static void testReizigerDAO(ReizigerDAO rdao) {
        System.out.println("\n---------- Test ReizigerDAO -------------");

        // Haal alle reizigers op uit de database
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("\n[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        // Maak een nieuwe reiziger aan en persisteer deze in de database
        String gbdatum = "1981-03-14";
        Reiziger sietske = new Reiziger(77, "S", null, "Boers", java.sql.Date.valueOf(gbdatum));
        System.out.print("\n[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        rdao.save(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers");
        rdao.findAll().forEach(reiziger -> System.out.println(reiziger));

//        // Haal reiziger op met id 2
        int reizigerId = 2;
        System.out.println(String.format("\n[Test] Gebruiker met id %d: ", reizigerId));
        System.out.println(rdao.findById(reizigerId) + "\n");

//        // Haal reizigers op met 2002-12-03 als geboortedatum
        String geboorteDatum = "2002-12-03";
        reizigers = rdao.findByGbDatum(geboorteDatum);
        System.out.println(String.format("\n[Test] Alle gebruikers met als geboortedatum %s: ", geboorteDatum));
        reizigers.forEach(reiziger -> System.out.println(reiziger));

//         Verwijder gebruiker met id 77
        System.out.print("\n[Test] Eerst " + rdao.findAll().size() + " reizigers, ");
        rdao.delete(rdao.findById(77));
        System.out.println("na verwijderen van gebruiker: " + rdao.findAll().size());

        //Update gebruiker 1. Verander voorletter van G naar R
        Reiziger reiziger = rdao.findById(1);
        System.out.print("\n[Test] Voorletter is eerst " + reiziger.getVoorletters());
        reiziger.setVoorletters("R");
        System.out.println(rdao.update(reiziger));;
        System.out.println(", na update is zijn de voorletters: " + rdao.findById(1).getVoorletters());
    }

    public static void testAdresDAO(AdresDAO adao) {
        System.out.println("\n---------- Test AdresDAO -------------");

        //findAll
        System.out.println("Test findAll()");
        adao.findAll().forEach(adres -> System.out.println(adres));

        //save
        System.out.println("\nTest save()");
        Adres nieuweAdres = new Adres(
                6,
                "3731XC",
                "67",
                "Aesolusweg",
                "De Bilt",
                new Reiziger(77, "S", null, "Boers", Date.valueOf("1981-03-14")));
        adao.save(nieuweAdres);
        System.out.println("Alle adressen na het toevoegen:");
        adao.findAll().forEach(adres -> System.out.println(adres));

        //findByReiziger
        System.out.println("\nTest findByReiziger()");
        System.out.println(adao.findByReiziger(new Reiziger(77, "S", null, "Boers", Date.valueOf("1981-03-14"))));

        //update
        System.out.println("\nTest update()");
        nieuweAdres.setHuisnummer("69");
        adao.update(nieuweAdres);
        System.out.println("Alle adressen na het updaten:");
        adao.findAll().forEach(adres -> System.out.println(adres));

        //delete
        System.out.println("\nTest delete()");
        adao.delete(nieuweAdres);
        System.out.println("Alle adressen na het verwijderen:");
        adao.findAll().forEach(adres -> System.out.println(adres));
    }

    public static void testOVChipkaartDAO(OVChipkaartDAO odao, ReizigerDAO rdao, ProductDAO pdao) {
        Reiziger reiziger = rdao.findById(2);
        OVChipkaart ovChipkaart = new OVChipkaart(
                8,
                Date.valueOf("2020-12-01"),
                1,
                21.00,
                reiziger
        );
        System.out.println("\n---------- Test OVChipkaartDAO -------------");

        //save
        System.out.println("\n[Test] save()");
        System.out.print("Eerst " + odao.findAll().size() + " reizigers, na ReizigerDAO.save() ");
        odao.save(ovChipkaart);
        System.out.println(odao.findAll().size() + " reizigers");

        //update
        System.out.println("\n[Test] update()");
        System.out.println("Voor update: ");
        odao.findByReiziger(reiziger).forEach(kaart -> System.out.println(kaart));
        ovChipkaart.setSaldo(10.00);
        odao.update(ovChipkaart);
        System.out.println("Na update: ");
        odao.findByReiziger(reiziger).forEach(kaart -> System.out.println(kaart));

        //findByReiziger
        System.out.println("\n[Test] findByReiziger()");
        odao.findByReiziger(reiziger).forEach(kaart -> System.out.println(kaart));

        //delete
        System.out.println("\n[Test] delete()");
        System.out.print("Eerst " + odao.findAll().size() + " kaarten, ");
        odao.delete(ovChipkaart);
        System.out.println("na verwijderen van OV Chipkaart: " + odao.findAll().size());

        //findAll
        System.out.println("\n[Test] FindAll()");
        odao.findAll().forEach(kaart -> System.out.println(kaart));

        System.out.println("\n[Test] FindByProdduct()");
        odao.findByProduct(pdao.findAll().get(0)).forEach(kaart -> System.out.println(kaart));
    }

    public static void testProductDAO(ProductDAO pdao, OVChipkaartDAO odao) {
        System.out.println("\n---------- Test ProductDAO -------------");

        System.out.println("\n[Test] findByOvchipkaart");
        pdao.findByOVChipkaart(odao.findAll().get(3)).forEach(product -> System.out.println(product));

        System.out.println("\n[Test] findAll");
        pdao.findAll().forEach(product -> System.out.println(product));

        System.out.println("\n[Test] Save");
        Product product = new Product(7, "Koningsdag", "Voordelig reizen op koningsdag", 20.00);
        product.voegOvChipkaartToe(odao.findAll().get(1));
        System.out.println("Voor de save: " + pdao.findAll().size());
        pdao.save(product);
        System.out.println("Na de save: " + pdao.findAll().size());

        System.out.println("\n[Test] Update");
        product.setPrijs(10.00);
        product.verwijderOvChipkaart(product.getOvChipkaarten().get(0));
//        product.voegOvChipkaartToe(product.);
        System.out.println("Voor de update: ");
        pdao.findAll().forEach(prod -> System.out.println(prod));
        pdao.update(product);
        System.out.println("Na de update: ");
        pdao.findAll().forEach(prod -> System.out.println(prod));

        System.out.println("\n[Test] Delete");
        System.out.println("Voor update: " + pdao.findAll().size());
        pdao.delete(product);
        System.out.println("Na update: " + pdao.findAll().size());
    }
}
