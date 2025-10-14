package com.cafepos.domain;

import com.cafepos.common.Money;

import java.math.BigDecimal;

public final class LineItem {
    private final Product product;
    private final int quantity;

    public LineItem(Product product, int quantity) {
        if (product == null) throw new IllegalArgumentException("product required");
        if (quantity <= 0) throw new IllegalArgumentException("quantity must be > 0");
        this.product = product;
        this.quantity = quantity;
    }

    public Product product() {
        return product;
    }

    public int quantity() {
        return quantity;
    }

    public Money lineTotal() {
        Money unit = (product instanceof com.cafepos.catalog.Priced p)
                ? p.price()
                : product.basePrice();
        return unit.multiply(BigDecimal.valueOf(quantity));
    }
}