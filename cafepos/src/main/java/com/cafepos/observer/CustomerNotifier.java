package com.cafepos.observer;

import com.cafepos.domain.Order;

public final class CustomerNotifier implements OrderObserver {
    @Override
    public void updated(Order order, String eventType) {
        System.out.println("[Customer] Dear customer, your Order #"
                + order.getId() + " has been updated: " + eventType + ".");
    }
}
