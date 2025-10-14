package com.cafepos.observer;
import com.cafepos.domain.*;

public final class KitchenDisplay implements OrderObserver {
    @Override
    public void updated(Order order, String eventType) {
        if ("itemAdded".equals(eventType)) {
            System.out.println("[Kitchen] Order #" + order.getId() + ": item added");
        } else if ("paid".equals(eventType)) {
            System.out.println("[Kitchen] Order #" + order.getId() + ": payment received");
        }
    }
}
