package com.kleverkapital.kkbackend.optionsPricing.options;


import java.util.*;

public class OptProfitRes {
    HashSet<Double> stockPrices;
    HashSet<Integer> daysToExpiries;
    HashSet<Double> volatilities;

    public OptProfitRes() {
        stockPrices = new HashSet<>();
        daysToExpiries =  new HashSet<>();
        volatilities = new HashSet<>();
        optionValues = new ArrayList<>();
    }

    List<OptionValueKey> optionValues;
//    public double getOptionValue(double stockPrice, double sigma, int daysToExpiry){
//
//        return optionValues.get(new OptionValueKey(stockPrice, sigma, daysToExpiry));
//    }

    public void setOptionValue(double stockPrice,  double sigma,int daysToExpiry, double value){
        stockPrices.add(stockPrice);
        daysToExpiries.add(daysToExpiry);
        volatilities.add(sigma);
       optionValues.add(new OptionValueKey(stockPrice, sigma, daysToExpiry,value));
    }



    public Set<Double> getStockPrices() {
        return stockPrices;
    }

    public void setStockPrices(Set<Double> stockPrices) {
        this.stockPrices = new HashSet<>(stockPrices);
    }

    public Set<Integer> getDaysToExpiries() {
        return daysToExpiries;
    }

    public void setDaysToExpiries(Set<Integer> daysToExpiries) {
        this.daysToExpiries = new HashSet<>(daysToExpiries);
    }

    public Set<Double> getVolatilities() {
        return volatilities;
    }

    public void setVolatilities(Set<Double> volatilities) {
        this.volatilities = new HashSet<>(volatilities);
    }

    public List<OptionValueKey> getOptionValues() {
        return optionValues;
    }

    public void setOptionValues(List<OptionValueKey> optionValues) {
        this.optionValues = optionValues;
    }
}
