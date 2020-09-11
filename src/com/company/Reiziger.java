package com.company;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Reiziger {
    private int id;
    private String voorletters;
    private String tussenvoegsel;
    private String achternaam;
    private Date geboorteDatum;

    private List<OVChipkaart> ovChipkaarten;

    private Adres adres;

    public Reiziger(int id, String voorletters, String tussenvoegsel, String achternaam, Date geboorteDatum) {
        this.id = id;
        this.voorletters = voorletters;
        this.tussenvoegsel = tussenvoegsel;
        this.achternaam = achternaam;
        this.geboorteDatum = geboorteDatum;

        ovChipkaarten = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getVoorletters() {
        return voorletters;
    }

    public String getTussenvoegsel() {
        return tussenvoegsel;
    }

    public String getAchternaam() {
        return achternaam;
    }

    public Date getGeboorteDatum() {
        return geboorteDatum;
    }

    public void setVoorletters(String voorletters) {
        this.voorletters = voorletters;
    }

    public void setAdres(Adres adres) {
        this.adres = adres;
    }

    public Adres getAdres() {
        return adres;
    }

    public void addOVChipkaart(OVChipkaart ovChipkaart) {
        ovChipkaarten.add(ovChipkaart);
    }

    public void setOvChipkaarten(List<OVChipkaart> ovChipkaarten) { this.ovChipkaarten = ovChipkaarten; };

    public List<OVChipkaart> getOvChipkaarten() {
        return ovChipkaarten;
    }

    @Override
    public String toString() {
        String infoString = "";
        if(adres != null) {
            infoString = String.format("Reiziger {#%d %s. %s %s, geb. %s, Adres {#%d %s-%s}",
                    id,
                    voorletters,
                    tussenvoegsel == null ? "" : tussenvoegsel,
                    achternaam,
                    geboorteDatum.toString(),
                    adres.getId(),
                    adres.getPostcode(),
                    adres.getHuisnummer()
            );
        }else {
            infoString = String.format("Reiziger {#%d %s. %s %s, geb. %s, Adres null",
                    id,
                    voorletters,
                    tussenvoegsel == null ? "" : tussenvoegsel,
                    achternaam,
                    geboorteDatum.toString()
            );
        }

        for(OVChipkaart ovChipkaart : ovChipkaarten) {
            infoString += String.format(", {#%d Geldig tot: %s, Saldo: %.2f, Klasse: %d}",
                    ovChipkaart.getKaartnummer(),
                    ovChipkaart.getGeldigTot(),
                    ovChipkaart.getSaldo(),
                    ovChipkaart.getKlasse());
        }

        return infoString + "}";
    }
}
