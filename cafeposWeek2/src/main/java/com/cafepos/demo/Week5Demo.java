package com.cafepos.demo;

import com.cafepos.factory.ProductFactory;
import com.cafepos.catalog.*;
import com.cafepos.domain.*;

public final class Week5Demo {
    public static void main(String[] args) {
        ProductFactory factory = new ProductFactory();

        Product p1 = factory.create("ESP+SHOT+OAT");
        Product p2 = factory.create("LAT+L");

        Order order = new Order(OrderIds.next());
        order.addItem(new LineItem(p1, 1));
        order.addItem(new LineItem(p2, 2));

        System.out.println("Order #" + order.id());
        for (LineItem li : order.items()) {
            System.out.println(" - " + li.product().name() + " x" + li.quantity() + " = " + li.lineTotal());
        }
        System.out.println("Subtotal: " + order.subtotal());
        System.out.println("Tax (10%): " + order.taxAtPercent(10));
        System.out.println("Total: " + order.totalWithTax(10));
    }
}
