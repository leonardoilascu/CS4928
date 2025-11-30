package com.cafepos.smells;

import com.cafepos.common.Money;
import com.cafepos.factory.ProductFactory;
import com.cafepos.domain.Product;

import java.math.BigDecimal;

public class OrderManagerGod {
    public static int TAX_PERCENT = 10; // Global/Static State
    public static String LAST_DISCOUNT_CODE = null; // Global/Static State

    public static String process(String recipe, int qty, String paymentType, String discountCode, boolean printReceipt) {
        // God Class Method
        ProductFactory factory = new ProductFactory();
        Product product = factory.create(recipe);

        Money unitPrice;
        try {
            var priced = product instanceof com.cafepos.catalog.Priced p && p != null
                    ? p.price() : product.basePrice();
            unitPrice = priced;
        } catch (Exception e) {
            unitPrice = product.basePrice();
        }

        if (qty <= 0) qty = 1;
        Money subtotal = unitPrice.multiply(BigDecimal.valueOf(qty));// Primitive Obsession: using int for qty

        Money discount = Money.zero();
        if (discountCode != null) { // Duplicated Logic
            if (discountCode.equalsIgnoreCase("LOYAL5")) {
                discount = Money.of(subtotal.asBigDecimal()
                        .multiply(java.math.BigDecimal.valueOf(5)).divide(java.math.BigDecimal.valueOf(100)));
            } else if (discountCode.equalsIgnoreCase("COUPON1")) {
                discount = Money.of(1.00);
            } else if (discountCode.equalsIgnoreCase("NONE")) {
                discount = Money.zero();
            } else {
                discount = Money.zero();
            }
            LAST_DISCOUNT_CODE = discountCode;
        }

        Money discounted = Money.of(subtotal.asBigDecimal().subtract(discount.asBigDecimal()));
        // casting types over and over again
        if (discounted.asBigDecimal().signum() < 0) {
            discounted = Money.zero();
        }
        //  Calculating tax inline
        var tax = Money.of(discounted.asBigDecimal().multiply(java.math.BigDecimal.valueOf(TAX_PERCENT)).divide(java.math.BigDecimal.valueOf(100)));

        var total = discounted.add(tax);

        if (paymentType != null) {
                if (paymentType.equalsIgnoreCase("CASH")) {
                    System.out.println("[Cash] Customer paid " + total + "EUR");
                } else if (paymentType.equalsIgnoreCase("CARD")) {
                    System.out.println("[Card] Customer paid " + total + "EUR with card ****1234"); // Shotgun Surgery: hardcoded card number
                } else if (paymentType.equalsIgnoreCase("WALLET")) {
                    System.out.println("[Wallet] Customer paid " + total + "EUR via waller user-wallet-789"); // Shotgun Surgery: hardcoded wallet id
                } else {
                    System.out.println("[Unknown Payment] " + total); // Shotgun Surgery: Shouldn't process payment
                }
            }

            StringBuilder receipt = new StringBuilder();
            receipt.append("Order (").append(recipe).append(")x").append(qty).append("\n"); // Shotgun Surgery: hardcoded receipt formatting
            receipt.append("Subtotal: ").append(subtotal).append("\n");
            if (discount.asBigDecimal().signum() > 0) {
                receipt.append("Discount: -").append(discount).append("\n");
            }
            receipt.append("Tax (").append(TAX_PERCENT).append("%):").append(tax).append("\n");
            receipt.append("Total: ").append(total);
            String out = receipt.toString();

            if (printReceipt) {
                System.out.println(out);
            }
            return out;
        }
    }

