package com.cafepos.demo;

import com.cafepos.factory.ProductFactory;
import com.cafepos.pricing.*;
import com.cafepos.payment.*;
import com.cafepos.smells.OrderManagerGod;


public final class Week6Demo {

    public static void main(String[] args) {
        // smelly version
        String oldReceipt = OrderManagerGod.process(
                "LAT+L",
                2,
                "CARD",
                "LOYAL5",
                false
        );


        var pricing = new PricingService(
                new LoyaltyPercentDiscount(5),
                new FixedRateTaxPolicy(10)
        );

        //clean version
        var printer = new ReceiptPrinter();

        var factory = new ProductFactory();

        PaymentStrategy payment = new CardPayment("1234567812345678");
        {

            var checkout = new CheckoutService(factory, pricing, printer, payment, 10);

            String newReceipt = checkout.checkout("LAT+L", 2);

            System.out.println("Old Receipt:\n" + oldReceipt);
            System.out.println("\nNew Receipt:\n" + newReceipt);
            System.out.println("\nMatch: " + oldReceipt.equals(newReceipt));
        }
    }
}
