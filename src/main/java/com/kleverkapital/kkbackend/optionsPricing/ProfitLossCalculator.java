package com.kleverkapital.kkbackend.optionsPricing;


import com.kleverkapital.kkbackend.optionsPricing.model.CallOptionInvestment;
import com.kleverkapital.kkbackend.optionsPricing.model.OptionInvestment;
import com.kleverkapital.kkbackend.optionsPricing.model.PutOptionInvestment;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

import static com.kleverkapital.kkbackend.optionsPricing.BlackScholesMethods.callPrice;
import static com.kleverkapital.kkbackend.optionsPricing.BlackScholesMethods.putPrice;


public class ProfitLossCalculator {

    public double calculate(OptionInvestment investment,
                            double currentStockPrice,
                            double timeInterval,
                            double riskfreeRate,
                            double sigma
    ) {

        if (investment instanceof CallOptionInvestment) {
            double v = callPrice(currentStockPrice,
                    investment.getStrikePrice(),
                    timeInterval / 365, riskfreeRate, sigma);
            double profit = (v - investment.getOptionPrice()) * investment.getOptionsMultiplier() * investment.getNumberOfContracts();
            return profit;
        } else if (investment instanceof PutOptionInvestment) {
            double putPrice = putPrice(currentStockPrice,
                    investment.getStrikePrice(),
                    timeInterval / 365, riskfreeRate, sigma);
            double profit = (putPrice - investment.getOptionPrice()) * investment.getOptionsMultiplier() * investment.getNumberOfContracts();
            return profit;
        }
        throw new IllegalArgumentException("Invalid investment type");
    }

    public List<Pair<Double, Double>> getProfitRanges(OptionInvestment investment
            , double currentStockPrice) {

        double price = currentStockPrice;
        var increment = InvestmentUtilities.getIncrement(price);
        int ranges = 10;
        List<Pair<Double, Double>> profits = new ArrayList<>();
        for (int i = -1 * ranges; i < ranges; i++) {
            double currentPrice = price + (increment * i);
            profits.add(Pair.of(currentPrice,
                    investment.calculateProfit(currentPrice)));
        }
        return profits;
    }

    public OptionProfitResult calculateOptionProfitResultsWithMultiSigma(OptionInvestment option,
                                                                         double riskfreeRate,
                                                                         List<Double> sigmas,
                                                                         int dayToExpiry,
                                                                         double stockPrice
    ) {
        var price = stockPrice;
        var increment = InvestmentUtilities.getIncrement(price);
        var names = sigmas.stream().map(x -> new String("sigma " + x*100 + "%")).toList();
        OptionProfitResult result = new OptionProfitResult(names);
        List<ProfitResult> profitPairs = new ArrayList<>();
        result.setResults(profitPairs);
        var ranges = 40;
        for (int i = -1 * ranges; i < ranges; i++) {
            double currentPrice = price + (increment * i);
            var y = new ProfitResult();
            y.setPrice(currentPrice);
            profitPairs.add(y);

            setPriceForDifferentExpiryDayRanges2(option, riskfreeRate, sigmas, dayToExpiry, currentPrice, y);

        }
        return result;
    }
    public OptionProfitResult calculateOptionProfitResults(OptionInvestment option,
                                                           double riskfreeRate,
                                                           double sigma,
                                                           List<Integer> daysSet,
                                                           double stockPrice) {
        var price = stockPrice;
        var increment = InvestmentUtilities.getIncrement(price);
        var names = daysSet.stream().map(x -> new String("days to expire: " + x)).toList();
        OptionProfitResult result = new OptionProfitResult(names);
        List<ProfitResult> profitPairs = new ArrayList<>();
        result.setResults(profitPairs);
        var ranges = 15;
        for (int i = -1 * ranges; i < ranges; i++) {
            double currentPrice = price + (increment * i);
            var y = new ProfitResult();
            y.setPrice(currentPrice);
            profitPairs.add(y);

            setPriceForDifferentExpiryDayRanges(option, riskfreeRate, sigma, daysSet, currentPrice, y);

        }
        return result;
    }

    private void setPriceForDifferentExpiryDayRanges2(OptionInvestment option,
                                                      double riskfreeRate,
                                                      List<Double> sigmas,
                                                      int daysToExpire, double currentPrice, ProfitResult resultsForAPrice) {
        List<Double> values = new ArrayList<>();
        for (double sigma : sigmas) {
            double value;

            value = calculate(option, currentPrice,
                    daysToExpire,
                    riskfreeRate, sigma);


            if (Double.isNaN(value) || Double.isInfinite(value)) {
                System.out.println(currentPrice + " " + value);
                value = 0;
                System.out.println(currentPrice + " " + value);
            }

            values.add(value);

        }
        resultsForAPrice.DoNonsens(values);
    }
    private void setPriceForDifferentExpiryDayRanges(OptionInvestment option, double riskfreeRate, double sigma,
                                                     List<Integer> daysSet, double currentPrice, ProfitResult resultsForAPrice) {
        List<Double> values = new ArrayList<>();
        for (int daytoExpire : daysSet) {
            double value;
            if (daytoExpire == 0)
            {
                value = option.calculateProfit(currentPrice);
            }
            else {
                value = calculate(option, currentPrice,
                        daytoExpire,
                        riskfreeRate, sigma);
            }

            if (Double.isNaN(value) || Double.isInfinite(value)) {
                System.out.println(currentPrice + " " + value);
                value = 0;
                System.out.println(currentPrice + " " + value);
            }

            values.add(value);

        }
        resultsForAPrice.DoNonsens(values);
    }
}