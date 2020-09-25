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

    public void setPrijs(double prijs) {
        this.prijs = prijs;
    }

    public void voegOvChipkaartToe(OVChipkaart ovChipkaart) {
        this.ovChipkaarten.add(ovChipkaart);
    }

    public void verwijderOvChipkaart(OVChipkaart ovChipkaart) {
        this.ovChipkaarten.remove(ovChipkaart);
    }

    @Override
    public String toString() {
        String infoString = String.format("Product {#%d, naam: %s, beschrijving: %s, prijs: %.2f}",
                this.nummer,
                this.naam,
                this.beschrijving,
                this.prijs);
        for(OVChipkaart ovChipkaart : this.ovChipkaarten) {
            infoString += String.format("\n  OVChipkaart {#%d Geldig tot: %s, Saldo: %.2f, Klasse: %d, Reiziger {#%d %s. %s %s, geb. %s}}\n",
                    ovChipkaart.getKaartnummer(),
                    ovChipkaart.getGeldigTot(),
                    ovChipkaart.getSaldo(),
                    ovChipkaart.getKlasse(),
                    ovChipkaart.getReiziger().getId(),
                    ovChipkaart.getReiziger().getVoorletters(),
                    ovChipkaart.getReiziger().getTussenvoegsel(),
                    ovChipkaart.getReiziger().getAchternaam(),
                    ovChipkaart.getReiziger().getGeboorteDatum());
        }
        return infoString;
    }
}
