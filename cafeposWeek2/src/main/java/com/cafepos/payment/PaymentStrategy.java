package com.cafepos.payment;

import com.cafepos.domain.Order;

public interface PaymentStrategy {
    void pay(Order order);
}
