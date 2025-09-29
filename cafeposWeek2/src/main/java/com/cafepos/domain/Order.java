package com.cafepos.domain;

import com.cafepos.common.Money;
import java.util.*;
import com.cafepos.payment.PaymentStrategy;

public final class Order {
    private final long id;
    private final List<LineItem> items = new ArrayList<>();

    public Order(long id) {
        this.id = id;
    }

    public long id() { return id; }
    public List<LineItem> items() { return Collections.unmodifiableList(items); }

    public void addItem(LineItem li) {
        if (li == null) throw new IllegalArgumentException("line item required");
        items.add(li);
    }

    public Money subtotal() {
        return items.stream()
                .map(LineItem::lineTotal)
                .reduce(Money.zero(), Money::add);
    }

    public Money taxAtPercent(int percent) {
        if (percent < 0) throw new IllegalArgumentException("tax percent cannot be negative");
        return subtotal().percentage(percent);
    }

    public Money totalWithTax(int percent) {
        return subtotal().add(taxAtPercent(percent));
    }
    public void pay(PaymentStrategy strategy) {
        if (strategy == null) throw new IllegalArgumentException("strategy required");
        strategy.pay(this);
    }
}