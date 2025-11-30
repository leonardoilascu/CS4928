package com.cafepos.pricing;

import com.cafepos.common.Money;

public final class FixedCouponDiscount implements DiscountPolicy {
    private final Money amount;
    public FixedCouponDiscount(Money amount) { this.amount = amount; }
        @Override public Money discountOf(Money subtotal) {
        if (amount.asBigDecimal().compareTo(subtotal.asBigDecimal()) > 0) return subtotal;
        return amount;
    }
}