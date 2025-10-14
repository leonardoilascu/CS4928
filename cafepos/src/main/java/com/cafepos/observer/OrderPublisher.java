package com.cafepos.observer;

import com.cafepos.domain.Order;

public interface OrderPublisher {
    void register(OrderObserver o);
    void unregister(OrderObserver o);
    void notifyObservers(Order order, String eventType);
}
