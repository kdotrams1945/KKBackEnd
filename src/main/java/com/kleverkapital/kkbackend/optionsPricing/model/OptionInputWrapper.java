package com.kleverkapital.kkbackend.optionsPricing.model;


import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("Wrapper")
public class OptionInputWrapper {
    String item1Type;
    int item1DaysUntillExpiry;
    double item1StrikePrice;
    int item1Contracts;
    String item2Type;
    int item2daysUntilExpiry;
    double item2strikePrice;
    int item2contracts;
    int quantity;

    public OptionInput OptionInput1() {
        return new OptionInput(item1Type, item1DaysUntillExpiry, item1StrikePrice, item1Contracts);
    }


    public OptionInput OptionInput2() {
        return new OptionInput(item2Type, item2daysUntilExpiry, item2strikePrice, item2contracts);
    }

    public String getItem1Type() {
        return item1Type;
    }

    public void setItem1Type(String item1Type) {
        this.item1Type = item1Type;
    }

    public int getItem1DaysUntillExpiry() {
        return item1DaysUntillExpiry;
    }

    public void setItem1DaysUntillExpiry(int item1DaysUntillExpiry) {
        this.item1DaysUntillExpiry = item1DaysUntillExpiry;
    }

    public double getItem1StrikePrice() {
        return item1StrikePrice;
    }

    public void setItem1StrikePrice(double item1StrikePrice) {
        this.item1StrikePrice = item1StrikePrice;
    }

    public int getItem1Contracts() {
        return item1Contracts;
    }

    public void setItem1Contracts(int item1Contracts) {
        this.item1Contracts = item1Contracts;
    }

    public String getItem2Type() {
        return item2Type;
    }

    public void setItem2Type(String item2Type) {
        this.item2Type = item2Type;
    }

    public int getItem2daysUntilExpiry() {
        return item2daysUntilExpiry;
    }

    public void setItem2daysUntilExpiry(int item2daysUntilExpiry) {
        this.item2daysUntilExpiry = item2daysUntilExpiry;
    }

    public double getItem2strikePrice() {
        return item2strikePrice;
    }

    public void setItem2strikePrice(double item2strikePrice) {
        this.item2strikePrice = item2strikePrice;
    }

    public int getItem2contracts() {
        return item2contracts;
    }

    public void setItem2contracts(int item2contracts) {
        this.item2contracts = item2contracts;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}