package com.cafepos.catalog;

import com.cafepos.domain.Product;

import java.util.*;
public final class InMemoryCatalog implements Catalog {
    private final Map<String, Product> byId = new HashMap<>();
    @Override public void add(Product p) {
        if (p == null) throw new
                IllegalArgumentException("product required");
    byId.put(p.id(), p);
    }
    @Override public Optional<Product> findById(String id) {
        return Optional.ofNullable(byId.get(id));
    }
}