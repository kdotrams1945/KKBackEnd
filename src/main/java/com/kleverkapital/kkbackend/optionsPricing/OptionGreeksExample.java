package com.kleverkapital.kkbackend.optionsPricing;


import net.finmath.functions.AnalyticFormulas;

public class OptionGreeksExample {
    public static void main(String[] args) {
        // Inputs
        double spot = 210;
        double strike = 220;
        double maturity = 1.0;         // in years
        double riskFreeRate = 0.05;    // 5%
        double volatility = 0.2;       // 20%

        // Call option greeks using AnalyticFormulas
        double delta = AnalyticFormulas.blackScholesOptionDelta(spot,riskFreeRate,
                volatility, maturity, strike);
        double vega = AnalyticFormulas.blackScholesOptionVega(spot,riskFreeRate, volatility, maturity, strike);
        double gamma = AnalyticFormulas.blackScholesOptionGamma(spot, riskFreeRate, volatility, maturity, strike);
        double theta = AnalyticFormulas.blackScholesOptionTheta(spot,  riskFreeRate, volatility, maturity, strike);
        double rho = AnalyticFormulas.blackScholesOptionRho(spot,  riskFreeRate, volatility, maturity, strike);

        // Output
        System.out.println("Delta: " + delta);
        System.out.println("Vega:  " + vega);
        System.out.println("Gamma: " + gamma);
        System.out.println("Theta: " + theta);
        System.out.println("Rho:   " + rho);
    }
}
