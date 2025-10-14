package com.cafepos.catalog;

import com.cafepos.domain.Product;

import java.util.Optional;
public interface Catalog {
    void add(Product p);
    Optional<Product> findById(String id);
}