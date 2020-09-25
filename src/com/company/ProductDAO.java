package com.company;

import java.util.List;

public interface ProductDAO {
    void setOVChipkaartDAO(OVChipkaartDAO ovChipkaartDAO);

    boolean save(Product product);
    boolean update(Product product);
    boolean delete(Product product);

    List<Product> findByOVChipkaart(OVChipkaart ovChipkaart);
    List<Product> findAll();
}
