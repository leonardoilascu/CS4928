package com.cafepos.pricing;

import com.cafepos.common.Money;

public final class FixedRateTaxPolicy implements TaxPolicy {
    private final int percent;
    public FixedRateTaxPolicy(int percent) { if (percent < 0) throw new IllegalArgumentException(); this.percent = percent; }
        @Override public Money taxOn(Money amount) {
        var t = amount.asBigDecimal().multiply(java.math.BigDecimal.valueOf(percent)).divide(java.math.BigDecimal.valueOf(100));
        return Money.of(t);
    }
}
