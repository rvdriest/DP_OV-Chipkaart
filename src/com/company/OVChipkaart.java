package com.company;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaart {
    private int kaartnummer;
    private Date geldigTot;
    private int klasse;
    private double saldo;
    private Reiziger reiziger;
//    private List<Product> producten;

    public OVChipkaart(int kaartnummer, Date geldigTot, int klasse, double saldo, Reiziger reiziger) {
        this.kaartnummer = kaartnummer;
        this.geldigTot = geldigTot;
        this.klasse = klasse;
        this.saldo = saldo;
        this.reiziger = reiziger;
        reiziger.addOVChipkaart(this);
//        producten = new ArrayList<>();
    }

    public int getKaartnummer() {
        return kaartnummer;
    }

    public Date getGeldigTot() {
        return geldigTot;
    }

    public int getKlasse() {
        return klasse;
    }

    public double getSaldo() {
        return saldo;
    }

    public Reiziger getReiziger() {
        return reiziger;
    }

//    public List<Product> getProducten() {
//        return producten;
//    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

//    public void voegProductToe(Product product) {
//        this.producten.add(product);
//    }
//
//    public void verwijderProduct(Product product) {
//        this.producten.remove(product);
//    }

    @Override
    public String toString() {
        return String.format("OVChipkaart {#%d Geldig tot: %s, Saldo: %.2f, Klasse: %d, Reiziger {#%d %s. %s %s, geb. %s}}",
                kaartnummer,
                geldigTot,
                saldo,
                klasse,
                reiziger.getId(),
                reiziger.getVoorletters(),
                reiziger.getTussenvoegsel() == null ? "" : reiziger.getTussenvoegsel(),
                reiziger.getAchternaam(),
                reiziger.getGeboorteDatum()).replace("  ", " ");
    }
}
