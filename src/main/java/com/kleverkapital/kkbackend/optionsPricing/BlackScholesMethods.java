package com.kleverkapital.kkbackend.optionsPricing;

import net.finmath.functions.AnalyticFormulas;
import org.apache.commons.math3.special.Erf;

import static java.lang.Math.*;

public class BlackScholesMethods {


//    public static double blackScholesCallPrice(double S,
//                                               double K,
//                                               double T,
//                                               double r,
//                                               double sigma) {
//        if (T == 0)
//            T = 0.01;
//        double d1 = (log(S / K) + (r + 0.5 * sigma * sigma) * T) / (sigma * sqrt(T));
//        double d2 = d1 - sigma * sqrt(T);
//
//        return S * cumulativeNormal(d1) - K * exp(-r * T) * cumulativeNormal(d2);
//    }



    public static double callPrice(double stockPrice, double strikePrice, double timeToExpiry,
                                   double riskFreeRate, double volatility) {

        return AnalyticFormulas.blackScholesOptionValue(
                stockPrice,
                riskFreeRate,
                volatility,
                timeToExpiry,
                strikePrice, true
        );
    }

    public static double putPrice(double stockPrice, double strikePrice, double timeToExpiry,
                                  double riskFreeRate, double volatility) {

        return AnalyticFormulas.blackScholesOptionValue(
                stockPrice,
                riskFreeRate,
                volatility,
                timeToExpiry,
                strikePrice, false
        );
    }


//    public static double blackScholesPutPrice(double S, double K, double T, double r, double sigma) {
//        if (T == 0)
//            T = 1/1000;
//        double d1 = (log(S / K) + (r + 0.5 * sigma * sigma) * T) / (sigma * sqrt(T));
//        double d2 = d1 - sigma * sqrt(T);
//        return K * exp(-r * T) * cumulativeNormal(-d2) - S * cumulativeNormal(-d1);
//    }

    // Approximation of the cumulative standard normal distribution
    public static double cumulativeNormal(double x) {
        // return Gaussian.cdf(x);
        return 0.5 * (1.0 + Erf.erf(x / sqrt(2.0)));
    }

//    public static void main(String[] args) {
//        double stockPrice = 120.0;
//        double strikePrice = 110.0;
//        double timeToExpiration = 0.25; // 3 months = 0.25 years
//        double riskFreeRate = 0.05;     // 5%
//        double volatility = 0.3;        // 30%
//        double premiumPaid = 8.0;       // Premium per share paid
//        int contractSize = 100;
//
//        double callPrice = blackScholesCallPrice(stockPrice, strikePrice, timeToExpiration, riskFreeRate, volatility);
//        double profit = (callPrice - premiumPaid) * contractSize;
//
//        System.out.printf("Call option price: $%.2f%n", callPrice);
//        System.out.printf("Profit before expiration: $%.2f%n", profit);
//    }
}