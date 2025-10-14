package com.cafepos.decorator;

import com.cafepos.catalog.Priced;
import com.cafepos.domain.Product;
import com.cafepos.common.Money;

public final class SizeLarge extends ProductDecorator implements Priced {
    private static final Money SURCHARGE = Money.of(0.70);

    public SizeLarge(Product base) {
        super(base);
    }

    @Override
    public String name() { 
        return base.name() + " (Large)";
    }

    public Money price() {
        Money basePrice = (base instanceof Priced p) ? p.price() : base.basePrice();
        return basePrice.add(SURCHARGE);
    }
}
