package com.company;

import java.util.List;

public interface ReizigerDAO {
    void setAdresDAO(AdresDAO adresDAO);
    void setOVChipkaartDAO(OVChipkaartDAO ovChipkaartDAO);

    boolean save(Reiziger reiziger);
    boolean update(Reiziger reiziger);
    boolean delete(Reiziger reiziger);

    Reiziger findById(int id);
    List<Reiziger> findByGbDatum(String datum);
    List<Reiziger> findAll();
}
