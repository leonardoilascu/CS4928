package com.cafepos.demo;

import com.cafepos.catalog.Catalog;
import com.cafepos.catalog.InMemoryCatalog;
import com.cafepos.domain.SimpleProduct;
import com.cafepos.domain.LineItem;
import com.cafepos.common.Money;
import com.cafepos.domain.Order;
import com.cafepos.domain.OrderIds;
import com.cafepos.observer.CustomerNotifier;
import com.cafepos.observer.DeliveryDesk;
import com.cafepos.observer.KitchenDisplay;
import com.cafepos.payment.CashPayment;

public final class Week4Demo {
    public static void main(String[] args) {
        Catalog catalog = new InMemoryCatalog();
        catalog.add(new SimpleProduct("P-ESP", "Espresso", Money.of(2.50)));

        Order order = new Order(OrderIds.next());
        order.register(new KitchenDisplay());
        order.register(new DeliveryDesk());
        order.register(new CustomerNotifier());

        order.addItem(new LineItem(catalog.findById("P-ESP").orElseThrow(), 1));
        order.pay(new CashPayment());
        order.markReady();
    }
}
