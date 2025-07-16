package com.kleverkapital.kkbackend.optionsPricing;

public class InvestmentUtilities {
    public static double getIncrement(double initialPrice) {
        var increment = 2.5;
        if (initialPrice > 1000) {
            increment = 25;
        } else if (initialPrice > 100) {
            increment = 5;
        } else if (initialPrice <= 15) {
            increment = 1;
        }
        return increment;
    }
}
