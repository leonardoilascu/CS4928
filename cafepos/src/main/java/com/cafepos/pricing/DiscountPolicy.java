package com.cafepos.pricing;

import com.cafepos.common.Money;

public interface DiscountPolicy {
    Money discountOf (Money subtotal);
}
