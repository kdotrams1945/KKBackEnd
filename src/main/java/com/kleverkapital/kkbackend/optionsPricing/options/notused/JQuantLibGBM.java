package com.kleverkapital.kkbackend.optionsPricing.options.notused;


import org.jquantlib.math.distributions.NormalDistribution;

import java.util.Random;

public class JQuantLibGBM {

    public static double[] simulateGBM(double S0, double mu, double sigma, double T, int steps) {
        double[] prices = new double[steps + 1];
        prices[0] = S0;

        double dt = T / steps;
        Random rng = new Random();
        NormalDistribution normal = new NormalDistribution();

        for (int i = 1; i <= steps; i++) {
            double Z = rng.nextDouble(); // Convert uniform to normal
            double drift = (mu - 0.5 * sigma * sigma) * dt;
            double diffusion = sigma * Math.sqrt(dt) * Z;
            prices[i] = prices[i - 1] * Math.exp(drift + diffusion);
        }

        return prices;
    }

    public static void main(String[] args) {
        double S0 = 100.0;
        double mu = 0.05;
        double sigma = 0.2;
        double T = 1.0;
        int steps = 252;

        double[] path = simulateGBM(S0, mu, sigma, T, steps);

        for (int i = 0; i < path.length; i++) {
            System.out.printf("Day %d: %.2f%n", i, path[i]);
        }
    }
}
