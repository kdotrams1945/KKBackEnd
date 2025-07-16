package com.kleverkapital.kkbackend.optionsPricing;


import net.finmath.time.TenorFromArray;
import net.finmath.time.TimeDiscretization;

import net.finmath.montecarlo.*;
import net.finmath.montecarlo.assetderivativevaluation.*;


import net.finmath.montecarlo.process.*;
import net.finmath.stochastic.RandomVariable;
import net.finmath.montecarlo.assetderivativevaluation.models.BlackScholesModel;

public class FinmathGBM {
    public static void main(String[] args) throws Exception {
        double initialValue = 100.0;
        double riskFreeRate = 0.05;
        double volatility = 0.25;

        int numberOfPaths = 10000;
        int numberOfTimeSteps = 252;
        double maturity = 1.0;

        TimeDiscretization timeDiscretization = new TenorFromArray(1,numberOfTimeSteps,
                (maturity+1) / numberOfTimeSteps       );

        var model = new BlackScholesModel(
                initialValue,     // S0
                riskFreeRate,     // risk-free rate
                volatility        // sigma
        );

        var stochasticDriver = new BrownianMotionFromMersenneRandomNumbers(
                timeDiscretization, 5, numberOfPaths
                , (int) (Math.random() * 1000));
        MonteCarloProcess process = new EulerSchemeFromProcessModel(
                new BlackScholesModel(initialValue, riskFreeRate, volatility), stochasticDriver);

        AssetModelMonteCarloSimulationModel simulation = new MonteCarloAssetModel(model, process);


        // Simulate asset path
        for (int i = 0; i <= numberOfTimeSteps; i++) {
            double time = timeDiscretization.getTime(i);
            RandomVariable assetValue = simulation.getAssetValue(time, 0);
            System.out.printf("Time %.2f: %.4f%n", time, assetValue.getAverage());
        }
    }
}