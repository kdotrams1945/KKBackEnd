package com.kleverkapital.kkbackend.optionsPricing;


import java.util.List;
import java.util.Map;

public class ProfitResult {
    double price;
    double profit1;
    double profit2;
    double profit3;


    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getProfit1() {
        return profit1;
    }

    public void setProfit1(double profit1) {
        this.profit1 = profit1;
    }

    public double getProfit2() {
        return profit2;
    }

    public void setProfit2(double profit2) {
        this.profit2 = profit2;
    }

    public double getProfit3() {
        return profit3;
    }

    public void setProfit3(double profit3) {
        this.profit3 = profit3;
    }

    public void DoNonsens(List<Double> values) {
        setProfit1(values.get(0));
        setProfit2(values.get(1));
        setProfit3(values.get(2));
    }

//    public Map<Integer, Double> getProfits() {
//        return profits;
//    }
//
//    public void setProfits(Map<Integer, Double> profits) {
//        this.profits = profits;
//    }
}
