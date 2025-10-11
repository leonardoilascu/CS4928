package com.cafepos.payment;

import com.cafepos.domain.Order;

public final class WalletPayment implements PaymentStrategy {
    private final String walletId;
    public WalletPayment(String walletId) {
        this.walletId = walletId;
    }
    @Override
    public void pay(Order order) {
        System.out.println("[Wallet] Customer paid " + order.totalWithTax(10) + " EUR via wallet: " + walletId);
    }

}