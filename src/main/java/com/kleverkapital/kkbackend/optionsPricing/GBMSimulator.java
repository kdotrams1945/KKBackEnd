package com.kleverkapital.kkbackend.optionsPricing;



import java.util.Random;

public class GBMSimulator {
    public static double[] simulateGBM(double S0, double mu, double sigma, double T, int steps) {
        double[] prices = new double[steps + 1];
        prices[0] = S0;

        double dt = T / steps;
        Random rand = new Random();

        for (int i = 1; i <= steps; i++) {
            double Z = rand.nextGaussian(); // Standard normal
            double drift = (mu - 0.5 * sigma * sigma) * dt;
            double diffusion = sigma * Math.sqrt(dt) * Z;
            prices[i] = prices[i - 1] * Math.exp(drift + diffusion);
        }

        return prices;
    }

    public static void main(String[] args) {
        double S0 = 100.0;     // Initial stock price
        double mu = 0.05;      // Annual drift (5%)
        double sigma = 0.24;    // Annual volatility (20%)
        double T = 1.0;        // Time horizon in years
        int steps = 252;       // Daily steps for 1 year

        double[] simulation = simulateGBM(S0, mu, sigma, T, steps);
        for (int i = 0; i < simulation.length; i++) {
            System.out.printf("Day %d: %.2f%n", i, simulation[i]);
        }
    }
}
