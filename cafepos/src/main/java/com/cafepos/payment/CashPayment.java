package com.cafepos.payment;

import com.cafepos.domain.Order;

public class CashPayment implements PaymentStrategy {
    @Override
    public void pay(Order order) {
        System.out.println("[Cash] Customer paid " + order.totalWithTax(10) + " EUR");
    }

}