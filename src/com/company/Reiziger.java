package com.company;

import java.sql.Date;

public class Reiziger {
    private int id;
    private String voorletters;
    private String tussenvoegsel;
    private String achternaam;
    private Date geboorteDatum;

    private Adres adres;

    public Reiziger(int id, String voorletters, String tussenvoegsel, String achternaam, Date geboorteDatum) {
        this.id = id;
        this.voorletters = voorletters;
        this.tussenvoegsel = tussenvoegsel;
        this.achternaam = achternaam;
        this.geboorteDatum = geboorteDatum;
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

    //om update() mee te testen
    public void setVoorletters(String voorletters) {
        this.voorletters = voorletters;
    }

    public void setAdres(Adres adres) {
        this.adres = adres;
    }

    @Override
    public String toString() {
        String infoString = "";
        if(adres != null) {
            infoString = String.format("Reiziger {#%d %s. %s %s, geb. %s, Adres {#%d %s-%s}}   ",
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
            infoString = String.format("Reiziger {#%d %s. %s %s, geb. %s, Adres null}   ",
                    id,
                    voorletters,
                    tussenvoegsel == null ? "" : tussenvoegsel,
                    achternaam,
                    geboorteDatum.toString()
            );
        }
        return infoString;
    }
}
