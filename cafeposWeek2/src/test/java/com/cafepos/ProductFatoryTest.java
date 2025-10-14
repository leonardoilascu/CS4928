package com.cafepos;

import com.cafepos.catalog.*;
import com.cafepos.common.Money;
import com.cafepos.decorator.*;
import com.cafepos.factory.ProductFactory;
import com.cafepos.domain.*;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ProductFatoryTest {

    @Test
    void decorator_single_addon() {  //adding one decorator changes name + price
        Product espresso = new SimpleProduct("P-ESP", "Espresso", Money.of(2.50));
        Product withShot = new ExtraShot(espresso);

        assertEquals("Espresso + Extra Shot", withShot.name());
        assertEquals(Money.of(3.30), ((Priced) withShot).price());
    }

    @Test
    void decorator_stacks_multiple_addons() { //multiple decorators stack and priced right
        Product espresso = new SimpleProduct("P-ESP", "Espresso", Money.of(2.50));
        Product decorated = new SizeLarge(new OatMilk(new ExtraShot(espresso)));

        assertEquals("Espresso + Extra Shot + Oat Milk (Large)", decorated.name());
        assertEquals(Money.of(4.50), ((Priced) decorated).price());
    }

    @Test
    void factory_creates_correct_decorated_product() { //factory builds the right drink from recipe code
        ProductFactory factory = new ProductFactory();
        Product p = factory.create("ESP+SHOT+OAT");

        assertTrue(p.name().contains("Espresso"));
        assertTrue(p.name().contains("Oat Milk"));
        assertEquals(Money.of(3.80), ((Priced) p).price());
    }

    @Test
    void order_uses_decorated_price_for_totals() { //order totals use decorator price not base price
        Product espresso = new SimpleProduct("P-ESP", "Espresso", Money.of(2.50));
        Product withShot = new ExtraShot(espresso);

        Order order = new Order(1);
        order.addItem(new LineItem(withShot, 2));

        assertEquals(Money.of(6.60), order.subtotal());
        assertEquals(Money.of(0.66), order.taxAtPercent(10));
        assertEquals(Money.of(7.26), order.totalWithTax(10));
    }

    @Test
    void factory_vs_manual_produce_same_result() { //building drink manually and through factory gives the same result
        ProductFactory f = new ProductFactory();

        Product viaFactory = f.create("ESP+SHOT+OAT+L");
        Product viaManual = new SizeLarge(new OatMilk(new ExtraShot(
                new SimpleProduct("P-ESP", "Espresso", Money.of(2.50)))
        ));

        assertEquals(viaManual.name(), viaFactory.name());
        assertEquals(((Priced) viaManual).price(), ((Priced) viaFactory).price());

        Order o1 = new Order(100);
        o1.addItem(new LineItem(viaFactory, 1));
        Order o2 = new Order(101);
        o2.addItem(new LineItem(viaManual, 1));

        assertEquals(o1.subtotal(), o2.subtotal());
        assertEquals(o1.totalWithTax(10), o2.totalWithTax(10));
    }
}
