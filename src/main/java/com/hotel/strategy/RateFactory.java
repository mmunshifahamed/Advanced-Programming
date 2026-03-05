package com.hotel.strategy;

public class RateFactory {

    public static RateStrategy getStrategy(String roomType) {
        return switch (roomType) {
            case "Single" -> new SingleRate();
            case "Double" -> new DoubleRate();
            case "Deluxe" -> new DeluxeRate();
            case "Suite"  -> new SuiteRate();
            default -> null;
        };
    }
}