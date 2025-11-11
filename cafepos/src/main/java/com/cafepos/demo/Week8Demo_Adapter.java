package com.cafepos.demo;

import com.cafepos.printing.*;
import vendor.legacy.LegacyThermalPrinter;

public final class Week8Demo_Adapter {
    public static void main(String[] args) {
        //sample receipt
        String receipt = "Order (LAT+L) x2\nSubtotal: 7.80\nTax (10%): 0.78\nTotal: 8.58";

        // Create the legacy printer
        LegacyThermalPrinter legacyPrinter = new LegacyThermalPrinter();
        // Wrap it in adapter
        Printer printer = new LegacyPrinterAdapter(legacyPrinter);

        printer.print(receipt);

        System.out.println("[Demo] Sent receipt via adapter.");
    }
}