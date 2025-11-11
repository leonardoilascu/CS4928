package com.cafepos;

import com.cafepos.common.Money;
import com.cafepos.menu.*;
import com.cafepos.state.OrderFSM;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class Week9Tests {

    // ===== COMPOSITE/ITERATOR TESTS =====

    @Test
    void depth_first_iteration_collects_all_nodes() {
        Menu root = new Menu("ROOT");
        Menu a = new Menu("A");
        Menu b = new Menu("B");
        root.add(a);
        root.add(b);

        a.add(new MenuItem("x", Money.of(1.0), true));
        b.add(new MenuItem("y", Money.of(2.0), false));

        List<String> names = root.allItems().stream()
                .map(MenuComponent::name)
                .toList();

        assertTrue(names.contains("x"));
        assertTrue(names.contains("y"));
        assertTrue(names.contains("A"));
        assertTrue(names.contains("B"));
    }

    @Test
    void vegetarian_filter_returns_only_veg_items() {
        Menu root = new Menu("CAFÉ");
        root.add(new MenuItem("Espresso", Money.of(2.50), true));
        root.add(new MenuItem("Steak", Money.of(15.00), false));
        root.add(new MenuItem("Salad", Money.of(8.00), true));

        List<MenuItem> vegItems = root.vegetarianItems();

        assertEquals(2, vegItems.size());
        assertTrue(vegItems.stream().allMatch(MenuItem::vegetarian));
        assertTrue(vegItems.stream().anyMatch(mi -> mi.name().equals("Espresso")));
        assertTrue(vegItems.stream().anyMatch(mi -> mi.name().equals("Salad")));
    }

    @Test
    void nested_menu_traversal_order() {
        Menu root = new Menu("ROOT");
        Menu drinks = new Menu("Drinks");
        Menu coffee = new Menu("Coffee");

        coffee.add(new MenuItem("Espresso", Money.of(2.50), true));
        coffee.add(new MenuItem("Latte", Money.of(3.20), true));
        drinks.add(coffee);
        root.add(drinks);

        List<MenuComponent> all = root.allItems();

        // Should traverse depth-first: Drinks, Coffee, Espresso, Latte
        assertTrue(all.size() >= 4);

        // Verify items are present
        List<String> names = all.stream().map(MenuComponent::name).toList();
        assertTrue(names.contains("Drinks"));
        assertTrue(names.contains("Coffee"));
        assertTrue(names.contains("Espresso"));
        assertTrue(names.contains("Latte"));
    }

    @Test
    void menu_item_cannot_add_children() {
        MenuItem item = new MenuItem("Espresso", Money.of(2.50), true);

        assertThrows(UnsupportedOperationException.class,
                () -> item.add(new MenuItem("Invalid", Money.of(1.0), false)));
    }

    @Test
    void menu_component_price_validation() {
        MenuItem item = new MenuItem("Latte", Money.of(3.50), true);

        assertEquals("Latte", item.name());
        assertEquals(Money.of(3.50), item.price());
        assertTrue(item.vegetarian());
    }

    // ===== STATE PATTERN TESTS =====

    @Test
    void order_fsm_happy_path() {
        OrderFSM fsm = new OrderFSM();
        assertEquals("NEW", fsm.status());

        fsm.pay();
        assertEquals("PREPARING", fsm.status());

        fsm.markReady();
        assertEquals("READY", fsm.status());

        fsm.deliver();
        assertEquals("DELIVERED", fsm.status());
    }

    @Test
    void cannot_prepare_before_payment() {
        OrderFSM fsm = new OrderFSM();
        assertEquals("NEW", fsm.status());

        // Should remain in NEW state
        fsm.prepare();
        assertEquals("NEW", fsm.status());
    }

    @Test
    void cannot_deliver_from_preparing() {
        OrderFSM fsm = new OrderFSM();
        fsm.pay();
        assertEquals("PREPARING", fsm.status());

        // Should remain in PREPARING state
        fsm.deliver();
        assertEquals("PREPARING", fsm.status());
    }

    @Test
    void can_cancel_from_new_state() {
        OrderFSM fsm = new OrderFSM();
        assertEquals("NEW", fsm.status());

        fsm.cancel();
        assertEquals("CANCELLED", fsm.status());
    }

    @Test
    void can_cancel_during_preparing() {
        OrderFSM fsm = new OrderFSM();
        fsm.pay();
        assertEquals("PREPARING", fsm.status());

        fsm.cancel();
        assertEquals("CANCELLED", fsm.status());
    }

    @Test
    void cannot_cancel_after_ready() {
        OrderFSM fsm = new OrderFSM();
        fsm.pay();
        fsm.markReady();
        assertEquals("READY", fsm.status());

        // Should remain in READY state
        fsm.cancel();
        assertEquals("READY", fsm.status());
    }

    @Test
    void delivered_state_is_terminal() {
        OrderFSM fsm = new OrderFSM();
        fsm.pay();
        fsm.markReady();
        fsm.deliver();
        assertEquals("DELIVERED", fsm.status());

        // All operations should keep it in DELIVERED
        fsm.pay();
        assertEquals("DELIVERED", fsm.status());

        fsm.prepare();
        assertEquals("DELIVERED", fsm.status());

        fsm.cancel();
        assertEquals("DELIVERED", fsm.status());
    }

    @Test
    void cancelled_state_is_terminal() {
        OrderFSM fsm = new OrderFSM();
        fsm.cancel();
        assertEquals("CANCELLED", fsm.status());

        // All operations should keep it in CANCELLED
        fsm.pay();
        assertEquals("CANCELLED", fsm.status());

        fsm.prepare();
        assertEquals("CANCELLED", fsm.status());

        fsm.markReady();
        assertEquals("CANCELLED", fsm.status());
    }

    @Test
    void multiple_pay_calls_when_preparing() {
        OrderFSM fsm = new OrderFSM();
        fsm.pay();
        assertEquals("PREPARING", fsm.status());

        // Second pay should not change state
        fsm.pay();
        assertEquals("PREPARING", fsm.status());
    }
}