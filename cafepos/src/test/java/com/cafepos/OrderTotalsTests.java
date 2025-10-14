package com.cafepos;

import com.cafepos.common.Money;
import com.cafepos.domain.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class OrderTotalsTests {

    @Test
    void order_totals() {
        var p1 = new SimpleProduct("A", "A", Money.of(2.50));
        var p2 = new SimpleProduct("B", "B", Money.of(3.50));
        var o = new Order(1);
        o.addItem(new LineItem(p1, 2));
        o.addItem(new LineItem(p2, 1));
        assertEquals(Money.of(8.50), o.subtotal());
        assertEquals(Money.of(0.85), o.taxAtPercent(10));
        assertEquals(Money.of(9.35), o.totalWithTax(10));
    }
}