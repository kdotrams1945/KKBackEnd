package com.kleverkapital.kkbackend.optionsPricing.model;

public interface OptionInvestment {
    double getStrikePrice();

    int getNumberOfContracts();

    int getOptionsMultiplier();

    double getOptionPrice();

    double calculateProfit(double marketPrice);

    //List<Pair<Double, Double>> getProfitRanges();
}
