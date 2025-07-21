package com.kleverkapital.kkbackend.optionsPricing.options;

import com.kleverkapital.kkbackend.optionsPricing.BlackScholesMethods;
import com.kleverkapital.kkbackend.optionsPricing.OptionProfitResult;
import com.kleverkapital.kkbackend.optionsPricing.ProfitLossCalculator;
import com.kleverkapital.kkbackend.optionsPricing.ProfitResult;
import com.kleverkapital.kkbackend.optionsPricing.model.CallOptionInvestment;
import com.kleverkapital.kkbackend.optionsPricing.model.PutOptionInvestment;
import net.finmath.functions.AnalyticFormulas;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class OptionPriceCalculatorController {

    private static final double DAYS_IN_YEAR = 365.0;
    private static final int DEFAULT_MIN_DAYS = 300;
    private static final double SIGMA_LOW_FACTOR = 0.5;
    private static final double SIGMA_HIGH_FACTOR = 2.0;

//    @GetMapping("/call-option-price")
//    public double calculateCallOptionPrice(double stockPrice,
//                                           double strikePrice,
//                                           double yearlyInterestRate,
//                                           int daysUntilExpiry,
//                                           double sigma) {
//        return BlackScholesMethods.callPrice(stockPrice,
//                strikePrice,
//                daysUntilExpiry / DAYS_IN_YEAR,
//                yearlyInterestRate,
//                sigma);
//    }

    public static record OptionGreeks( double delta,double vega,
                                       double theta,double rho, double gamma){

    }

    @GetMapping("/option-analysis")
    public OptionProfitResult[] analyzeOptionProfit(double stockPrice,
                                                    double yearlyInterestRate,
                                                    double sigma,
                                                    OptionInputWrapper wrapper) {
        var input1 = wrapper.OptionInput1();
        var input2 = wrapper.OptionInput2();

        OptionProfitResult[] results = new OptionProfitResult[2];
        results[0] = resultWithDifferentExpiries(stockPrice, yearlyInterestRate, sigma, input1, input2);
        results[1] = resultWithDifferentSigmas(stockPrice, yearlyInterestRate, sigma, input1, input2);

        for (OptionProfitResult optionResult : results) {
            var r = optionResult.getResults();
            for (ProfitResult profitResult : r) {
                double price = profitResult.getPrice();
                double stockProfit = wrapper.quantity * (price - stockPrice);
                profitResult.setProfit1(profitResult.getProfit1() + stockProfit);
                profitResult.setProfit2(profitResult.getProfit2() + stockProfit);
                profitResult.setProfit3(profitResult.getProfit3() + stockProfit);
            }
        }

        results[0].setGreeks(calculateGreeks(stockPrice, yearlyInterestRate,
                sigma, input1));
        results[1].setGreeks(calculateGreeks(stockPrice, yearlyInterestRate,
                sigma, input2));
        return results;
    }

    private OptionGreeks calculateGreeks(double stockPrice, double yearlyInterestRate, double sigma, OptionInput optionInput1) {
        var spot = stockPrice;
        var riskFreeRate = yearlyInterestRate;
        var volatility = sigma;
        var maturity = optionInput1.getDaysUntilExpiry()/365.0;
        var strike = optionInput1.getStrikePrice();
        double delta = AnalyticFormulas.blackScholesOptionDelta(spot,riskFreeRate,
                volatility, maturity,
                strike);
        double vega = AnalyticFormulas.blackScholesOptionVega(spot,riskFreeRate, volatility, maturity, strike);
        double gamma = AnalyticFormulas.blackScholesOptionGamma(spot, riskFreeRate, volatility, maturity, strike);
        double theta = AnalyticFormulas.blackScholesOptionTheta(spot,  riskFreeRate, volatility, maturity, strike);
        double rho = AnalyticFormulas.blackScholesOptionRho(spot,  riskFreeRate, volatility, maturity, strike);
        OptionGreeks greek1 = new OptionGreeks(delta, vega, theta, rho, gamma);
        return greek1;
    }

    private OptionProfitResult resultWithDifferentExpiries(double stockPrice,
                                                           double yearlyInterestRate,
                                                           double sigma,
                                                           OptionInput input1,
                                                           OptionInput input2) {
        int d = DEFAULT_MIN_DAYS;
        if (input1.contracts != 0 && input2.contracts != 0) {
            d = Math.min(input1.daysUntilExpiry, input2.daysUntilExpiry);
        } else if (input1.contracts != 0) {
            d = input1.daysUntilExpiry;
        } else if (input2.contracts != 0) {
            d = input2.daysUntilExpiry;
        } else {
            throw new IllegalArgumentException("Are you crazy!");
        }

        List<Integer> ranges = new ArrayList<>();
        ranges.add(0);
        ranges.add(d / 4);
        ranges.add(d / 2);

        OptionProfitResult result1 = getOptionProfitResult(stockPrice, yearlyInterestRate, sigma, input1, ranges);
        OptionProfitResult result2 = getOptionProfitResult(stockPrice, yearlyInterestRate, sigma, input2, ranges);

        OptionProfitResult profitResult = new OptionProfitResult(result1.getLabels());
        profitResult.mergeResults(result1, result2);
        return profitResult;
    }

    private OptionProfitResult resultWithDifferentSigmas(double stockPrice,
                                                         double yearlyInterestRate,
                                                         double sigma,
                                                         OptionInput input1,
                                                         OptionInput input2) {
        List<Double> sigmas = new ArrayList<>();
        sigmas.add(sigma * SIGMA_LOW_FACTOR);
        sigmas.add(sigma);
        sigmas.add(sigma * SIGMA_HIGH_FACTOR);

        OptionProfitResult result1 = getOptionProfitResultWithMultiSigma(stockPrice, yearlyInterestRate, sigmas, input1);
        OptionProfitResult result2 = getOptionProfitResultWithMultiSigma(stockPrice, yearlyInterestRate, sigmas, input2);

        OptionProfitResult profitResult = new OptionProfitResult(result1.getLabels());
        profitResult.mergeResults(result1, result2);
        return profitResult;
    }

    private OptionProfitResult getOptionProfitResult(double stockPrice,
                                                     double yearlyInterestRate,
                                                     double sigma,
                                                     OptionInput input1,
                                                     List<Integer> ranges) {
        return input1.getType().equals("Put")
                ? putResult(stockPrice, yearlyInterestRate, sigma, input1, ranges)
                : callResult(stockPrice, yearlyInterestRate, sigma, input1, ranges);
    }

    private OptionProfitResult getOptionProfitResultWithMultiSigma(double stockPrice,
                                                                   double yearlyInterestRate,
                                                                   List<Double> sigma,
                                                                   OptionInput input1) {
        return input1.getType().equals("Put")
                ? putResultWithMultiSigma(stockPrice, yearlyInterestRate, sigma, input1, input1.daysUntilExpiry)
                : callResultWithMultiSigma(stockPrice, yearlyInterestRate, sigma, input1, input1.daysUntilExpiry);
    }

    private OptionProfitResult putResult(double stockPrice,
                                         double yearlyInterestRate,
                                         double sigma,
                                         OptionInput input,
                                         List<Integer> ranges) {
        double premiumPaid = BlackScholesMethods.putPrice(stockPrice,
                input.strikePrice,
                input.daysUntilExpiry / DAYS_IN_YEAR,
                yearlyInterestRate,
                sigma);
        var option = new PutOptionInvestment(input.strikePrice, premiumPaid, input.contracts);
        return new ProfitLossCalculator().calculateOptionProfitResults(option, yearlyInterestRate, sigma, ranges, stockPrice);
    }

    private OptionProfitResult callResult(double stockPrice,
                                          double yearlyInterestRate,
                                          double sigma,
                                          OptionInput input,
                                          List<Integer> ranges) {
        double premiumPaid = BlackScholesMethods.callPrice(stockPrice,
                input.strikePrice,
                input.daysUntilExpiry / DAYS_IN_YEAR,
                yearlyInterestRate,
                sigma);
        var option = new CallOptionInvestment(input.strikePrice, premiumPaid, input.contracts);
        return new ProfitLossCalculator().calculateOptionProfitResults(option, yearlyInterestRate, sigma, ranges, stockPrice);
    }

    private OptionProfitResult callResultWithMultiSigma(double stockPrice,
                                                        double yearlyInterestRate,
                                                        List<Double> sigma,
                                                        OptionInput input,
                                                        int daysUntilExpiry) {
        double premiumPaid = BlackScholesMethods.callPrice(stockPrice,
                input.strikePrice,
                daysUntilExpiry / DAYS_IN_YEAR,
                yearlyInterestRate,
                sigma.get(1));
        var option = new CallOptionInvestment(input.strikePrice, premiumPaid, input.contracts);
        return new ProfitLossCalculator().calculateOptionProfitResultsWithMultiSigma(option, yearlyInterestRate, sigma, daysUntilExpiry, stockPrice);
    }

    private OptionProfitResult putResultWithMultiSigma(double stockPrice,
                                                       double yearlyInterestRate,
                                                       List<Double> sigma,
                                                       OptionInput input,
                                                       int daysUntilExpiry) {
        double premiumPaid = BlackScholesMethods.putPrice(stockPrice,
                input.strikePrice,
                daysUntilExpiry / DAYS_IN_YEAR,
                yearlyInterestRate,
                sigma.get(1));
        var option = new PutOptionInvestment(input.strikePrice, premiumPaid, input.contracts);
        return new ProfitLossCalculator().calculateOptionProfitResultsWithMultiSigma(option, yearlyInterestRate, sigma, daysUntilExpiry, stockPrice);
    }
}