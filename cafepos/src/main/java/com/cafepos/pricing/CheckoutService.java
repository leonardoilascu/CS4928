package com.cafepos.pricing;

import com.cafepos.catalog.Priced; // make sure this import is present
import com.cafepos.common.Money;
import com.cafepos.domain.LineItem;
import com.cafepos.domain.Order;
import com.cafepos.domain.OrderIds;
import com.cafepos.domain.Product;
import com.cafepos.factory.ProductFactory;
import com.cafepos.payment.PaymentStrategy;
import com.cafepos.pricing.PricingService;
import com.cafepos.pricing.ReceiptPrinter;

public final class CheckoutService {
    private final ProductFactory factory;
    private final PricingService pricing;
    private final ReceiptPrinter printer;
    private final PaymentStrategy payment;
    private final int taxPercent;

    public CheckoutService(ProductFactory factory,
                           PricingService pricing,
                           ReceiptPrinter printer,
                           PaymentStrategy payment,
                           int taxPercent) {
        this.factory = factory;
        this.pricing = pricing;
        this.printer = printer;
        this.payment = payment;
        this.taxPercent = taxPercent;
    }

    public String checkout(String recipe, int qty) {
        Product product = factory.create(recipe);
        if (qty <= 0) qty = 1;


        Money unitPrice;
        if (product instanceof Priced pricedProduct) {
            unitPrice = pricedProduct.price();
        } else {
            unitPrice = product.basePrice();
        }

        Money subtotal = unitPrice.multiply(java.math.BigDecimal.valueOf(qty));
        var pricingResult = pricing.price(subtotal);

        Order order = new Order(OrderIds.next());
        order.addItem(new LineItem(product, qty));

        System.out.println("[Card] Customer paid " + pricingResult.total() + " EUR with card ****1234");


        return printer.format(recipe, qty, pricingResult, taxPercent);
    }
}
