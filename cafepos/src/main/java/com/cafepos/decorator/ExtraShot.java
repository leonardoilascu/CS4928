package com.cafepos.decorator;

import com.cafepos.catalog.*;
import com.cafepos.common.Money;
import com.cafepos.domain.Product;

public final class ExtraShot extends ProductDecorator implements Priced {
    private static final Money SURCHARGE = Money.of(0.80);

    public ExtraShot(Product base) { super(base);
    }

    @Override
    public String name() { return base.name() + " + Extra Shot";
    }

    @Override
    public Money price() {
        Money basePrice = (base instanceof Priced p) ? p.price() : base.basePrice();
        return basePrice.add(SURCHARGE);
    }
}
