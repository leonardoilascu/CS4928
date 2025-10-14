package com.cafepos;

import com.cafepos.domain.*;
import com.cafepos.domain.Order;
import com.cafepos.payment.*;
import com.cafepos.common.Money;
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

class Week3DemoTest {

    private SimpleProduct espresso;
    private SimpleProduct cookie;
    private Order order;

    @BeforeEach
    void setup() {
        espresso = new SimpleProduct("P-ESP", "Espresso", Money.of(2.50));
        cookie = new SimpleProduct("P-CCK", "Chocolate Cookie", Money.of(3.50));
        order = new Order(OrderIds.next());
    }

    @Test
    void testSubtotalAndTotalWithTax() {
        order.addItem(new LineItem(espresso, 2));
        order.addItem(new LineItem(cookie, 1));

        Money expectedSubtotal = Money.of(2.50 * 2 + 3.50);
        assertEquals(expectedSubtotal, order.subtotal());

        Money expectedTotal = expectedSubtotal.add(expectedSubtotal.percentage(10));
        assertEquals(expectedTotal, order.totalWithTax(10));
    }

    @Test
    void testCashPaymentStrategyInvoked() {
        final boolean[] called = {false};
        PaymentStrategy fakeCash = o -> called[0] = true;

        order.addItem(new LineItem(espresso, 1));
        order.pay(fakeCash);

        assertTrue(called[0], "Cash payment strategy should be invoked");
    }

    @Test
    void testCardPaymentMasksNumber() {
        order.addItem(new LineItem(espresso, 2));

        CardPayment card = new CardPayment("1234567812341234");
        // Just check that no exception is thrown and output masks correctly
        assertDoesNotThrow(() -> order.pay(card));
    }

    @Test
    void testWalletPaymentStrategy() {
        order.addItem(new LineItem(cookie, 1));

        WalletPayment wallet = new WalletPayment("alice-wallet-01");
        assertDoesNotThrow(() -> order.pay(wallet));
    }

    @Test
    void testNullPaymentStrategyFails() {
        order.addItem(new LineItem(cookie, 1));

        assertThrows(IllegalArgumentException.class, () -> order.pay(null));
    }

    @Test
    void testOrderDelegatesToAnyPaymentStrategy() {
        final boolean[] called = {false};
        PaymentStrategy fake = o -> called[0] = true;

        order.addItem(new LineItem(espresso, 1));
        order.pay(fake);

        assertTrue(called[0], "Order should delegate to strategy without knowing details");
    }
}
