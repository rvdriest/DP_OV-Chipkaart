package com.company;

public class Adres {
    private int id;
    private String postcode;
    private String huisnummer;
    private String straat;
    private String woonplaats;
    private Reiziger reiziger;

    public Adres(int id, String postcode, String huisnummer, String straat, String woonplaats, Reiziger reiziger) {
        this.id = id;
        this.postcode = postcode;
        this.huisnummer = huisnummer;
        this.straat = straat;
        this.woonplaats = woonplaats;
        this.reiziger = reiziger;

        reiziger.setAdres(this);
    }

    public int getId() {
        return id;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getHuisnummer() {
        return huisnummer;
    }

    public void setHuisnummer(String huisnummer) {
        this.huisnummer = huisnummer;
    }

    public String getStraat() {
        return straat;
    }

    public String getWoonplaats() {
        return woonplaats;
    }

    public Reiziger getReiziger() {
        return reiziger;
    }

    @Override
    public String toString() {
        return String.format("Adres {#%d %s-%s, Reiziger {#%d %s. %s %s, geb. %s}}",
                id,
                postcode,
                huisnummer,
                reiziger.getId(),
                reiziger.getVoorletters(),
                reiziger.getTussenvoegsel() == null ? "" : reiziger.getTussenvoegsel(),
                reiziger.getAchternaam(),
                reiziger.getGeboorteDatum());
    }
}
