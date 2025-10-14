package com.cafepos;

import com.cafepos.observer.*;
import com.cafepos.domain.SimpleProduct;
import com.cafepos.domain.LineItem;
import com.cafepos.common.Money;
import com.cafepos.domain.Order;
import com.cafepos.payment.CashPayment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class Week4ObserverTest {

    private Order order;
    private List<String> events;

    @BeforeEach
    void setUp() {
        order = new Order(1L);
        events = new ArrayList<>();

        // Register a test observer that just records event names
        order.register((o, eventType) -> events.add(eventType));
    }

    @Test
    void whenItemAdded_thenItemAddedEventIsSent() {
        var product = new SimpleProduct("P-001", "Latte", Money.of(3.50));
        var lineItem = new LineItem(product, 1);

        order.addItem(lineItem);

        assertTrue(events.contains("itemAdded"),
                "Expected 'itemAdded' event when adding an item");
    }

    @Test
    void whenPaid_thenPaidEventIsSent() {
        order.pay(new CashPayment());
        assertTrue(events.contains("paid"),
                "Expected 'paid' event when paying the order");
    }

    @Test
    void whenMarkedReady_thenReadyEventIsSent() {
        order.markReady();
        assertTrue(events.contains("ready"),
                "Expected 'ready' event when marking order ready");
    }

    @Test
    void multipleObserversReceiveSameEvent() {
        List<String> events2 = new ArrayList<>();
        order.register((o, evt) -> events2.add(evt));

        order.markReady();

        assertTrue(events.contains("ready"), "First observer should get ready");
        assertTrue(events2.contains("ready"), "Second observer should get ready");
    }

    @Test
    void unregisterStopsNotifications() {
        Order order2 = new Order(2L);
        List<String> events2 = new ArrayList<>();

        OrderObserver observer = (o, evt) -> events2.add(evt);
        order2.register(observer);
        order2.unregister(observer);

        order2.addItem(new LineItem(
                new SimpleProduct("P-002", "Espresso", Money.of(2.0)), 1));

        assertFalse(events2.contains("itemAdded"),
                "Unregistered observer should not receive events");
    }

}
