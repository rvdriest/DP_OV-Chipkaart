package com.company;

import java.util.List;

public interface AdresDAO {
    void setReizigerDAO(ReizigerDAO reizigerDAO);

    boolean save(Adres adres);
    boolean update(Adres adres);
    boolean delete(Adres adres);

    Adres findByReiziger(Reiziger reiziger);
    List<Adres> findAll();
}
