package com.cafepos.ui;

import com.cafepos.app.CheckoutService;
import com.cafepos.domain.LineItem;
import com.cafepos.domain.Order;
import com.cafepos.domain.OrderIds;
import com.cafepos.domain.OrderRepository;
import com.cafepos.domain.Product;
import com.cafepos.factory.ProductFactory;

public final class OrderController {

    private final OrderRepository orderRepository;
    private final CheckoutService checkoutService;
    private final ProductFactory productFactory = new ProductFactory();

    public OrderController(OrderRepository orderRepository,
                           CheckoutService checkoutService) {
        this.orderRepository = orderRepository;
        this.checkoutService = checkoutService;
    }

    public long createOrder() {
        long id = OrderIds.next();
        Order order = new Order(id);
        orderRepository.save(order);
        return id;
    }

    public void addItem(long orderId, String recipe, int quantity) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found: " + orderId));

        Product product = productFactory.create(recipe);
        LineItem item = new LineItem(product, quantity);
        order.addItem(item);

        // save updated order
        orderRepository.save(order);
    }

    public String checkout(long orderId, int taxPercent) {
        return checkoutService.checkout(orderId, taxPercent);
    }
}
