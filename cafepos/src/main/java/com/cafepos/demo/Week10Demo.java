package com.cafepos.demo;

import com.cafepos.infra.Wiring;
import com.cafepos.ui.ConsoleView;
import com.cafepos.ui.OrderController;

public final class Week10Demo {

    public static void main(String[] args) {

        Wiring.Components components = Wiring.createDefault();

        OrderController controller =
                new OrderController(components.orderRepository(), components.checkoutService());
        ConsoleView view = new ConsoleView();
        //  Create a new order
        long orderId = controller.createOrder();
        //  Add some drinks using ProductFactory recipes
        controller.addItem(orderId, "ESP+SHOT+OAT", 1); // 1 oat flat white w/ extra shot
        controller.addItem(orderId, "LAT+L", 2);        // 2 large lattes
        //  Checkout
        String receipt = controller.checkout(orderId, 10);
        //  Show receipt
        view.print(receipt);
    }
}
