package com.cafepos.demo;

import com.cafepos.events.EventBus;
import com.cafepos.events.OrderCreated;
import com.cafepos.events.OrderPaid;
import com.cafepos.infra.Wiring;
import com.cafepos.ui.OrderController;

public final class EventDemoWiring {

    public static void main(String[] args) {
        // Build components
        Wiring.Components components = Wiring.createDefault();

        // Controller from the MVC step
        OrderController controller =
                new OrderController(components.orderRepository(), components.checkoutService());

        // Create the event bus
        EventBus bus = new EventBus();

        // Subscribe a couple of handlers
        bus.on(OrderCreated.class, e ->
                System.out.println("[UI] order created: " + e.orderId()));

        bus.on(OrderPaid.class, e ->
                System.out.println("[UI] order paid: " + e.orderId()));

        // Create an order via the controller
        long orderId = controller.createOrder();

        // Emit events to simulate lifecycle
        bus.emit(new OrderCreated(orderId));

        bus.emit(new OrderPaid(orderId));
    }
}
