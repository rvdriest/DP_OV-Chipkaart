package com.company;

import java.sql.Date;

public class OVChipkaart {
    private int kaartnummer;
    private Date geldigTot;
    private int klasse;
    private double saldo;
    private Reiziger reiziger;

    public OVChipkaart(int kaartnummer, Date geldigTot, int klasse, double saldo, Reiziger reiziger) {
        this.kaartnummer = kaartnummer;
        this.geldigTot = geldigTot;
        this.klasse = klasse;
        this.saldo = saldo;
        this.reiziger = reiziger;
        reiziger.addOVChipkaart(this);
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

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

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
