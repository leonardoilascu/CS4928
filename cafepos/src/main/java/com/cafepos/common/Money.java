package com.cafepos.common;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public final class Money implements Comparable<Money> {
    private final BigDecimal amount;

    // Factory methods
    public static Money of(double value) {
        return new Money(BigDecimal.valueOf(value));
    }

    public static Money zero() {
        return new Money(BigDecimal.ZERO);
    }

    // Private constructor with invariant check
    private Money(BigDecimal a) {
        if (a == null) throw new IllegalArgumentException("amount required");
        if (a.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("amount cannot be negative");
        }
        this.amount = a.setScale(2, RoundingMode.HALF_UP);
    }

    // Arithmetic
    public Money add(Money other) {
        return new Money(this.amount.add(other.amount));
    }

    public Money multiply(java.math.BigDecimal factor) {
        if (factor == null) throw new IllegalArgumentException("factor required");
        if (factor.compareTo(java.math.BigDecimal.ZERO) < 0)
            throw new IllegalArgumentException("factor cannot be negative");
        return new Money(this.amount.multiply(factor));
    }

    public Money percentage(int percent) {
        if (percent < 0) throw new IllegalArgumentException("percent cannot be negative");
        java.math.BigDecimal rate = java.math.BigDecimal
                .valueOf(percent)
                .divide(java.math.BigDecimal.valueOf(100), 4, java.math.RoundingMode.HALF_UP);
        return multiply(rate);
    }


    // Comparable
    @Override
    public int compareTo(Money o) {
        return this.amount.compareTo(o.amount);
    }

    // Equals & hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Money)) return false;
        Money money = (Money) o;
        return amount.equals(money.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }

    // toString for debugging
    @Override
    public String toString() {
        return amount.toString();
    }

}
