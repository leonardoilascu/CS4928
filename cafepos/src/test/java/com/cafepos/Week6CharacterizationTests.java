package com.cafepos;

import com.cafepos.smells.OrderManagerGod;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class Week6CharacterizationTests {

    @Test
    void noDiscountCash() {
        String r = OrderManagerGod.process("ESP+SHOT+OAT", 1, "CASH", "NONE", false);
        assertTrue(r.contains("Subtotal: 3.80"));
        assertTrue(r.contains("Tax (10%):0.38"));
        assertTrue(r.contains("Total: 4.18"));
    }

    @Test
    void loyalty5Card() {
        String r = OrderManagerGod.process("LAT+L", 2, "CARD", "LOYAL5", false);
        assertTrue(r.contains("Subtotal: 7.80"));
        assertTrue(r.contains("Discount: -0.39"));
        assertTrue(r.contains("Tax (10%):0.74"));
        assertTrue(r.contains("Total: 8.15"));
    }

    @Test
    void coupon1Wallet() {
        String r = OrderManagerGod.process("ESP+SHOT", 0, "WALLET", "COUPON1", false);
        assertTrue(r.contains("Subtotal: 3.30"));
        assertTrue(r.contains("Discount: -1.00"));
        assertTrue(r.contains("Tax (10%):0.23"));
        assertTrue(r.contains("Total: 2.53"));
    }
}
