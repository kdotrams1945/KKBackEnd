package com.kleverkapital.kkbackend.optionsPricing.model;

public class OptionAnalysisResult {
    OptionProfitResult[] results;
    OptionGreeks[] greeks;

    public OptionProfitResult[] getResults() {
        return results;
    }

    public void setResults(OptionProfitResult[] results) {
        this.results = results;
    }

    public OptionGreeks[] getGreeks() {
        return greeks;
    }

    public void setGreeks(OptionGreeks[] greeks) {
        this.greeks = greeks;
    }
}
