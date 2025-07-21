package com.kleverkapital.kkbackend.optionsPricing.options;


import lombok.Data;

import java.util.Objects;

@Data
public class OptionValueKey {
    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    private double value;

    public double getStockPrice() {
        return stockPrice;
    }

    public void setStockPrice(double stockPrice) {
        this.stockPrice = stockPrice;
    }

    double stockPrice;
    double volatility;
    int daysToExpiry;


    public OptionValueKey(double stockPrice, double sigma, int daysToExpiry,
                          double value) {
        this.stockPrice = stockPrice;
        this.volatility = sigma;
        this.daysToExpiry = daysToExpiry;
        this.value = value;
    }



    public double getVolatility() {
        return volatility;
    }

    public void setVolatility(double volatility) {
        this.volatility = volatility;
    }

    public int getDaysToExpiry() {
        return daysToExpiry;
    }

    public void setDaysToExpiry(int daysToExpiry) {
        this.daysToExpiry = daysToExpiry;
    }



//    @Override
//    public String toString() {
//
//        return "["+strikePrice + "," + volatility + "," + daysToExpiry+"]"; //  Format keys however you want
 //   }
}
