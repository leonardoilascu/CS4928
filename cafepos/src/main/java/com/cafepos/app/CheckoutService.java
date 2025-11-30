package com.cafepos.app;

import com.cafepos.domain.Order;
import com.cafepos.domain.OrderRepository;
import com.cafepos.pricing.PricingService;

public final class CheckoutService {
    private final OrderRepository orders;
    private final PricingService pricing;

    public CheckoutService(OrderRepository orders, PricingService pricing) {
        this.orders = orders;
        this.pricing = pricing;
    }


    public String checkout(long orderId, int taxPercent) {
        Order order = orders.findById(orderId).orElseThrow();
        var pr = pricing.price(order.subtotal());
        return new ReceiptFormatter().format(orderId, order.items(), pr, taxPercent);
    }
}
