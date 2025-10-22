package com.cafepos.pricing;
import com.cafepos.common.Money;

public interface TaxPolicy {

    Money taxOf(Money amount);

    Money taxOn(Money discounted);
}
