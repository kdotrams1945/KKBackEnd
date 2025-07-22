package com.kleverkapital.kkbackend.optionsPricing.model;


import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("OI")
public class OptionInput {

    String type;
    int daysUntilExpiry;
    double strikePrice;
    int contracts;

    public OptionInput(String type, int daysUntilExpiry, double strikePrice, int contracts) {
        this.type = type;
        this.daysUntilExpiry = daysUntilExpiry;
        this.strikePrice = strikePrice;
        this.contracts = contracts;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDaysUntilExpiry() {
        return daysUntilExpiry;
    }

    public void setDaysUntilExpiry(int daysUntilExpiry) {
        this.daysUntilExpiry = daysUntilExpiry;
    }

    public double getStrikePrice() {
        return strikePrice;
    }

    public void setStrikePrice(double strikePrice) {
        this.strikePrice = strikePrice;
    }

    public int getContracts() {
        return contracts;
    }

    public void setContracts(int contracts) {
        this.contracts = contracts;
    }
}
