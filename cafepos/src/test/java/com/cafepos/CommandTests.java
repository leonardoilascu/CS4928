package com.cafepos;

import com.cafepos.command.*;
import com.cafepos.domain.*;
import com.cafepos.payment.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandTests {

    private Order order;
    private OrderService service;
    private PosRemote remote;

    @BeforeEach
    void setUp() {
        order = new Order(OrderIds.next());
        service = new OrderService(order);
        remote = new PosRemote(5);
    }

    @Test
    void testCommandExecutesAndAddsItem() {
        // Arrange
        Command addEspresso = new AddItemCommand(service, "ESP", 1);

        // Act
        addEspresso.execute();

        // Assert
        assertEquals(1, order.items().size(), "Order should have 1 item after command execution");
    }

    @Test
    void testUndoReversesLastCommand() {
        // Arrange
        remote.setSlot(0, new AddItemCommand(service, "ESP", 1));
        remote.setSlot(1, new AddItemCommand(service, "LAT", 2));

        // Act
        remote.press(0);  // Add espresso
        remote.press(1);  // Add latte
        assertEquals(2, order.items().size(), "Should have 2 items");

        remote.undo();    // Remove last item (latte)

        // Assert
        assertEquals(1, order.items().size(), "Should have 1 item after undo");
    }

    @Test
    void testRemoteExecutesCorrectSlot() {
        // Arrange
        remote.setSlot(0, new AddItemCommand(service, "ESP+SHOT", 1));
        remote.setSlot(1, new AddItemCommand(service, "LAT+L", 2));

        // Act
        remote.press(0);

        // Assert
        assertEquals(1, order.items().size(), "Pressing slot 0 should add 1 item");
        assertTrue(order.items().get(0).product().name().contains("Espresso"),
                "Should be espresso product");
    }

    @Test
    void testMacroCommandExecutesMultipleCommands() {
        // Arrange
        Command cmd1 = new AddItemCommand(service, "ESP", 1);
        Command cmd2 = new AddItemCommand(service, "LAT", 1);
        Command cmd3 = new AddItemCommand(service, "CAP", 1);
        MacroCommand macro = new MacroCommand(cmd1, cmd2, cmd3);

        // Act
        macro.execute();

        // Assert
        assertEquals(3, order.items().size(), "Macro should execute all 3 commands");
    }

    @Test
    void testMacroCommandUndoReverseOrder() {
        // Arrange
        Command cmd1 = new AddItemCommand(service, "ESP", 1);
        Command cmd2 = new AddItemCommand(service, "LAT", 1);
        MacroCommand macro = new MacroCommand(cmd1, cmd2);

        // Act
        macro.execute();
        assertEquals(2, order.items().size(), "Should have 2 items after macro execute");

        macro.undo();

        // Assert
        assertEquals(0, order.items().size(), "Macro undo should remove both items in reverse order");
    }

    @Test
    void testPayOrderCommandExecutes() {
        // Arrange
        service.addItem("ESP", 1);
        PaymentStrategy payment = new CardPayment("1234567812345678");
        Command payCommand = new PayOrderCommand(service, payment, 10);

        // Act & Assert
        assertDoesNotThrow(() -> payCommand.execute(),
                "Payment command should execute without errors");
    }

    @Test
    void testMultipleUndoOperations() {
        // Arrange
        remote.setSlot(0, new AddItemCommand(service, "ESP", 1));
        remote.setSlot(1, new AddItemCommand(service, "LAT", 1));
        remote.setSlot(2, new AddItemCommand(service, "CAP", 1));

        // Act
        remote.press(0);
        remote.press(1);
        remote.press(2);
        assertEquals(3, order.items().size(), "Should have 3 items");

        remote.undo();
        assertEquals(2, order.items().size(), "First undo should leave 2 items");

        remote.undo();
        assertEquals(1, order.items().size(), "Second undo should leave 1 item");

        remote.undo();

        // Assert
        assertEquals(0, order.items().size(), "Third undo should leave 0 items");
    }

    @Test
    void testIntegrationCommandFlowWithPricing() {
        // Arrange
        remote.setSlot(0, new AddItemCommand(service, "ESP+SHOT", 1));  // 2.50 + 0.80 = 3.30
        remote.setSlot(1, new AddItemCommand(service, "LAT+L", 2));     // (3.20 + 0.70) * 2 = 7.80
        remote.setSlot(2, new PayOrderCommand(service, new CardPayment("1234"), 10));

        // Act
        remote.press(0);
        remote.press(1);

        // Assert
        // Subtotal should be 3.30 + 7.80 = 11.10
        var expectedSubtotal = com.cafepos.common.Money.of(11.10);
        assertEquals(expectedSubtotal, order.subtotal(),
                "Subtotal should match expected total from decorated products");

        // With 10% tax: 11.10 * 1.10 = 12.21
        var expectedTotal = com.cafepos.common.Money.of(12.21);
        assertEquals(expectedTotal, order.totalWithTax(10),
                "Total with tax should be correctly calculated");

        // Now pay
        assertDoesNotThrow(() -> remote.press(2), "Payment should complete successfully");
    }
}