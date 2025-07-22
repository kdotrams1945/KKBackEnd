package com.kleverkapital.kkbackend.optionsPricing.model;

public record OptionGreeks(double delta, double vega,
                           double theta, double rho, double gamma) {

}
