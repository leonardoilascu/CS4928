package com.cafepos.payment;

import com.cafepos.domain.Order;

/**
 * Pay the order using a card number. All digits are masked except the last four.
 */
public final class CardPayment implements PaymentStrategy {
    private final String cardNumber;

    public CardPayment(String cardNumber) {
        if (cardNumber == null || cardNumber.isEmpty()) {
            throw new IllegalArgumentException("cardNumber required");
        }
        this.cardNumber = cardNumber;
    }

    private String masked() {
        int n = cardNumber.length();
        if (n <= 4) return cardNumber;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n - 4; i++) sb.append('*');
        sb.append(cardNumber.substring(n - 4));
        return sb.toString();
    }

    @Override
    public void pay(Order order) {
        System.out.println("[Card] Customer paid " + order.totalWithTax(10) + " EUR with card " + masked());
    }
}
