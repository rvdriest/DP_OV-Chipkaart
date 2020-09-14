package com.company;

import java.util.ArrayList;
import java.util.List;

public class Product {
    private int nummer;
    private String naam;
    private String beschrijving;
    private double prijs;
    private List<OVChipkaart> ovChipkaarten;

    public Product(int nummer, String naam, String beschrijving, double prijs) {
        this.nummer = nummer;
        this.naam = naam;
        this.beschrijving = beschrijving;
        this.prijs = prijs;
        ovChipkaarten = new ArrayList<>();
    }

    public int getNummer() {
        return nummer;
    }

    public String getNaam() {
        return naam;
    }

    public String getBeschrijving() {
        return beschrijving;
    }

    public double getPrijs() {
        return prijs;
    }

    public List<OVChipkaart> getOvChipkaarten() {
        return ovChipkaarten;
    }

    public void voegOvChipkaartToe(OVChipkaart ovChipkaart) {
        this.ovChipkaarten.add(ovChipkaart);
    }

    public void verwijderOvChipkaart(OVChipkaart ovChipkaart) {
        this.ovChipkaarten.remove(ovChipkaart);
    }
}
