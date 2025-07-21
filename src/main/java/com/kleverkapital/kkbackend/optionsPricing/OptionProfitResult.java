package com.kleverkapital.kkbackend.optionsPricing;


import com.kleverkapital.kkbackend.optionsPricing.options.OptionPriceCalculatorController;

import java.util.ArrayList;
import java.util.List;

public class OptionProfitResult {
    private OptionPriceCalculatorController.OptionGreeks greeks;

    public OptionProfitResult(List<String> labels) {
        this.labels = labels;
    }


    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    private List<String> labels;
    // stock price to profit
    private List<ProfitResult> results;



    public List<ProfitResult> getResults() {
        return results;
    }

    public void setResults(List<ProfitResult> results) {
        this.results = results;
    }

    public void mergeResults(OptionProfitResult result1, OptionProfitResult result2) {
        if (result1 == null && result2 != null) {
            setResults(result2.getResults());
        }
        if (result1 != null && result2 == null) {
            setResults(result1.getResults());
        }
        var results = result1.getResults();
        var results2 = result2.getResults();
        ArrayList<ProfitResult> newResults = new ArrayList<ProfitResult>();
        for (int i = 0; i < results.size(); i++) {

            var r1 = results.get(i);
            var r2 = results2.get(i);
            var x = new ProfitResult();
            x.setPrice(r1.getPrice());
            x.setProfit1(r1.getProfit1() + r2.getProfit1());
            x.setProfit2(r1.getProfit2() + r2.getProfit2());
            x.setProfit3(r1.getProfit3() + r2.getProfit3());
            newResults.add(x);
        }
        setResults(newResults);
    }

    public void setGreeks(OptionPriceCalculatorController.OptionGreeks greeks) {
        this.greeks = greeks;
    }

    public OptionPriceCalculatorController.OptionGreeks getGreeks() {
        return greeks;
    }
}
