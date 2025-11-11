package com.cafepos;

import com.cafepos.printing.*;
import vendor.legacy.LegacyThermalPrinter;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AdapterTests {

    // Fake legacy printer for testing - captures what was sent to it
    static class FakeLegacyPrinter extends LegacyThermalPrinter {
        int lastPayloadLength = -1;
        byte[] lastPayload = null;

        @Override
        public void legacyPrint(byte[] payload) {
            this.lastPayloadLength = payload.length;
            this.lastPayload = payload;
        }
    }

    @Test
    void adapterConvertsStringToBytes() {
        // Arrange
        FakeLegacyPrinter fake = new FakeLegacyPrinter();
        Printer adapter = new LegacyPrinterAdapter(fake);

        // Act
        adapter.print("ABC");

        // Assert
        assertTrue(fake.lastPayloadLength >= 3,
                "Payload length should be at least 3 bytes for 'ABC'");
        assertEquals(3, fake.lastPayloadLength,
                "Simple ASCII string 'ABC' should be exactly 3 bytes");
    }

    @Test
    void adapterHandlesEmptyString() {
        // Arrange
        FakeLegacyPrinter fake = new FakeLegacyPrinter();
        Printer adapter = new LegacyPrinterAdapter(fake);

        // Act
        adapter.print("");

        // Assert
        assertEquals(0, fake.lastPayloadLength,
                "Empty string should result in 0 bytes");
    }

    @Test
    void adapterHandlesMultilineReceipt() {
        // Arrange
        FakeLegacyPrinter fake = new FakeLegacyPrinter();
        Printer adapter = new LegacyPrinterAdapter(fake);
        String receipt = "Order (LAT+L) x2\nSubtotal: 7.80\nTax (10%): 0.78\nTotal: 8.58";

        // Act
        adapter.print(receipt);

        // Assert
        assertTrue(fake.lastPayloadLength > 50,
                "Multi-line receipt should have substantial byte length");
        assertNotNull(fake.lastPayload,
                "Payload should not be null");
    }

    @Test
    void adapterPreservesReceiptContent() {
        // Arrange
        FakeLegacyPrinter fake = new FakeLegacyPrinter();
        Printer adapter = new LegacyPrinterAdapter(fake);
        String originalReceipt = "Total: 12.76 EUR";

        // Act
        adapter.print(originalReceipt);

        // Assert
        String receivedText = new String(fake.lastPayload, java.nio.charset.StandardCharsets.UTF_8);
        assertEquals(originalReceipt, receivedText,
                "Adapter should preserve exact receipt text through conversion");
    }

    @Test
    void adapterWorksWithRealLegacyPrinter() {
        // Arrange
        LegacyThermalPrinter legacyPrinter = new LegacyThermalPrinter();
        Printer adapter = new LegacyPrinterAdapter(legacyPrinter);

        // Act & Assert
        assertDoesNotThrow(() -> adapter.print("Test Receipt"),
                "Adapter should work with real legacy printer without errors");
    }

    @Test
    void adapterHandlesSpecialCharacters() {
        // Arrange
        FakeLegacyPrinter fake = new FakeLegacyPrinter();
        Printer adapter = new LegacyPrinterAdapter(fake);
        String specialText = "Receipt: €10.50 — 100%";

        // Act
        adapter.print(specialText);

        // Assert
        assertTrue(fake.lastPayloadLength > 0,
                "Special characters should be converted to bytes");
        String decoded = new String(fake.lastPayload, java.nio.charset.StandardCharsets.UTF_8);
        assertEquals(specialText, decoded,
                "Special characters should survive the conversion");
    }

    @Test
    void multiplePrintsWorkCorrectly() {
        // Arrange
        FakeLegacyPrinter fake = new FakeLegacyPrinter();
        Printer adapter = new LegacyPrinterAdapter(fake);

        // Act
        adapter.print("First");
        int firstLength = fake.lastPayloadLength;

        adapter.print("Second Receipt");
        int secondLength = fake.lastPayloadLength;

        // Assert
        assertTrue(secondLength > firstLength,
                "Second receipt should have more bytes than first");
        assertEquals(14, secondLength,
                "'Second Receipt' should be 14 bytes");
    }
}