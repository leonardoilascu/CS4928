package com.cafepos.printing;

import vendor.legacy.LegacyThermalPrinter;
import java.nio.charset.StandardCharsets;

public final class LegacyPrinterAdapter implements Printer {
    private final LegacyThermalPrinter adaptee;

    public LegacyPrinterAdapter(LegacyThermalPrinter adaptee) {
        this.adaptee = adaptee;
    }

    @Override
    public void print(String receiptText) {
        // convert String to bytes
        byte[] escpos = receiptText.getBytes(StandardCharsets.UTF_8);
        adaptee.legacyPrint(escpos);
    }
}