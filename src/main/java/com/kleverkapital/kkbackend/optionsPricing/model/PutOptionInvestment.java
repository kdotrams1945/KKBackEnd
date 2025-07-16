package com.kleverkapital.kkbackend.optionsPricing.model;


public class PutOptionInvestment implements OptionInvestment {
    private double strikePrice;
    private double premiumPaid;
    private int numberOfContracts;

    public PutOptionInvestment(double strikePrice, double premiumPaid, int numberOfContracts) {
        this.strikePrice = strikePrice;
        this.premiumPaid = premiumPaid;
        this.numberOfContracts = numberOfContracts;
    }


    @Override
    public double getStrikePrice() {
        return strikePrice;
    }

    @Override
    public double calculateProfit(double marketPrice) {
        double profitPerShare = strikePrice - marketPrice - premiumPaid;
        if (marketPrice > strikePrice) {
            profitPerShare = - premiumPaid;
        }
//
        return profitPerShare * 100 * numberOfContracts;
//        return (getStrikePrice() - marketPrice - premiumPaid) * getOptionsMultiplier() * getNumberOfContracts();
    }
    @Override
    public int getNumberOfContracts() {
        return numberOfContracts;
    }

    @Override
    public int getOptionsMultiplier() {
        return 100;
    }

    @Override
    public double getOptionPrice() {
        return premiumPaid;
    }
}