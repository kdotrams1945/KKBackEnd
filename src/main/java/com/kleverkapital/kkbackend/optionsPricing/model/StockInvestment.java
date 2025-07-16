package com.kleverkapital.kkbackend.optionsPricing.model;

import com.kleverkapital.kkbackend.optionsPricing.InvestmentUtilities;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class StockInvestment  {
    int _quantity;
    double initialPrice;

    public StockInvestment(double price, int quantity) {

        if (price < 5)
            throw new IllegalArgumentException("Price cannot be negative");
        initialPrice = price;
        _quantity = quantity;
    }


    public double calculateProfit(double price) {
        if (price < 0) {
            throw new IllegalArgumentException("Price cannot be less than 0");
        }
        var pricePaid = initialPrice * _quantity;
        var currentValue = price * _quantity;
        return currentValue - pricePaid;
    }

    public List<Pair<Double, Double>> getProfitRanges() {

        double price = initialPrice;
        var increment = InvestmentUtilities.getIncrement(price);
        int ranges = 5;
        List<Pair<Double, Double>> profits = new ArrayList<>();
        for (int i = -1 * ranges; i < ranges; i++) {
            double currentPrice = price + (increment * i);
            profits.add(Pair.of(currentPrice,
                    calculateProfit(currentPrice)));
        }
        return profits;
    }

    public double getInitialPrice() {
        return initialPrice;
    }

    public void setInitialPrice(double initialPrice) {
        this.initialPrice = initialPrice;
    }
}
