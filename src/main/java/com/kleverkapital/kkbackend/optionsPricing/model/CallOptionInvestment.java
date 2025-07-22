package com.kleverkapital.kkbackend.optionsPricing.model;


public class CallOptionInvestment implements OptionInvestment {
    // todo: why final in java.
    // Effective Java;
    // Refactoring; Design Patterns; Test Driven Development.
    //
    //Final = can only be assigned once
    //These values shouldn't be assigned multiple times after once assignment
    //You only pay premium once, for example
    private final double optionPrice;
    private final int optionsMultiplier;
    private final int numberOfContracts;

    @Override
    public double getStrikePrice() {
        return strikePrice;
    }

    @Override
    public int getNumberOfContracts() {
        return numberOfContracts;
    }

    @Override
    public int getOptionsMultiplier() {
        return optionsMultiplier;
    }

    @Override
    public double getOptionPrice() {
        return optionPrice;
    }

    private final double strikePrice;


    // todo: lets calculate premiumPaid : option price calculator.

    public CallOptionInvestment(double strikePrice,
                                double premiumPaid,
                                int numberOfContracts) {

        this.strikePrice = strikePrice;
        this.optionPrice = premiumPaid;
        this.optionsMultiplier = 100;
        this.numberOfContracts = numberOfContracts;
    }


    @Override
    public double calculateProfit(double marketPrice) {
        double profitPerShare = marketPrice - strikePrice - optionPrice;
        if (marketPrice < strikePrice) {
            profitPerShare = -optionPrice;
        }

        return profitPerShare * optionsMultiplier * numberOfContracts;
    }



}