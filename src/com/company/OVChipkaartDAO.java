package com.company;

import java.util.List;

public interface OVChipkaartDAO {
    void setReizigerDAO(ReizigerDAO reizigerDAO);
    void setProductDAO(ProductDAO productDAO);

    boolean save(OVChipkaart ovChipkaart);
    boolean update(OVChipkaart ovChipkaart);
    boolean delete(OVChipkaart ovChipkaart);

    List<OVChipkaart> findByReiziger(Reiziger reiziger);
    List<OVChipkaart> findByProduct(Product product);
    List<OVChipkaart> findAll();
}
