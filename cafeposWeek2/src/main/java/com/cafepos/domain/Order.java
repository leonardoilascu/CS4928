package com.cafepos.domain;

import com.cafepos.common.Money;
import java.util.*;
import com.cafepos.observer.*;

public final class Order implements OrderPublisher {
    private final long id;
    private final List<LineItem> items = new ArrayList<>();

    private final List<OrderObserver> observers = new ArrayList<>();

    @Override
    public void register(OrderObserver o) {
        if (o == null) return;
        if (!observers.contains(o)) {
            observers.add(o);
        }
    }

    @Override
    public void unregister(OrderObserver o) {
        observers.remove(o);
    }

    @Override
    public void notifyObservers(Order order, String eventType) {
        for (OrderObserver o : observers) {
            o.updated(order, eventType);
        }
    }

    public Order(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public long id() { return id; }
    public List<LineItem> items() { return Collections.unmodifiableList(items); }

    public void addItem(LineItem li) {
        if (li == null) throw new IllegalArgumentException("line item required");
        items.add(li);
        notifyObservers(this, "itemAdded");
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


    public void pay(com.cafepos.payment.PaymentStrategy strategy) {
    if (strategy == null) throw new IllegalArgumentException("strategy required");
    strategy.pay(this);
    notifyObservers(this, "paid");
}
    public void markReady() {
        notifyObservers(this, "ready");
    }







}

