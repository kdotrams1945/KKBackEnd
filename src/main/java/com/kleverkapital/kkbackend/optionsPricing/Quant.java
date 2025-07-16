package com.kleverkapital.kkbackend.optionsPricing;

import org.jquantlib.QL;
import org.jquantlib.Settings;
import org.jquantlib.daycounters.Actual365Fixed;
import org.jquantlib.daycounters.DayCounter;
import org.jquantlib.exercise.AmericanExercise;
import org.jquantlib.exercise.BermudanExercise;
import org.jquantlib.exercise.EuropeanExercise;
import org.jquantlib.exercise.Exercise;

import org.jquantlib.instruments.Option;
import org.jquantlib.instruments.Payoff;
import org.jquantlib.instruments.PlainVanillaPayoff;
import org.jquantlib.instruments.VanillaOption;
import org.jquantlib.methods.lattices.AdditiveEQPBinomialTree;
import org.jquantlib.methods.lattices.CoxRossRubinstein;
import org.jquantlib.methods.lattices.JarrowRudd;
import org.jquantlib.methods.lattices.Joshi4;
import org.jquantlib.methods.lattices.LeisenReimer;
import org.jquantlib.methods.lattices.Tian;
import org.jquantlib.methods.lattices.Trigeorgis;
import org.jquantlib.pricingengines.AnalyticEuropeanEngine;
import org.jquantlib.pricingengines.vanilla.BaroneAdesiWhaleyApproximationEngine;
import org.jquantlib.pricingengines.vanilla.BinomialVanillaEngine;
import org.jquantlib.pricingengines.vanilla.BjerksundStenslandApproximationEngine;
import org.jquantlib.pricingengines.vanilla.IntegralEngine;
import org.jquantlib.pricingengines.vanilla.JuQuadraticApproximationEngine;
import org.jquantlib.pricingengines.vanilla.finitedifferences.FDAmericanEngine;
import org.jquantlib.pricingengines.vanilla.finitedifferences.FDBermudanEngine;
import org.jquantlib.pricingengines.vanilla.finitedifferences.FDEuropeanEngine;
import org.jquantlib.processes.BlackScholesMertonProcess;
import org.jquantlib.quotes.Handle;
import org.jquantlib.quotes.Quote;
import org.jquantlib.quotes.SimpleQuote;

import org.jquantlib.termstructures.BlackVolTermStructure;
import org.jquantlib.termstructures.YieldTermStructure;
import org.jquantlib.termstructures.volatilities.BlackConstantVol;
import org.jquantlib.termstructures.yieldcurves.FlatForward;
import org.jquantlib.time.Calendar;
import org.jquantlib.time.Date;
import org.jquantlib.time.Month;
import org.jquantlib.time.Period;
import org.jquantlib.time.TimeUnit;
import org.jquantlib.time.calendars.Target;



public class Quant implements Runnable {

    public static void main(final String[] args) {
        new Quant().run();
    }

    @Override
    public void run() {

        QL.info("::::: " + this.getClass().getSimpleName() + " :::::");



        // set up dates
        final Calendar calendar = new Target();
        final Date todaysDate = new Date(3, Month.July, 2025);
        final Date settlementDate = new Date(8, Month.July, 2025);
        new Settings().setEvaluationDate(todaysDate);

        // our options
        final Option.Type type = Option.Type.Put;
        final double strike = 210.0;
        final double underlying = 200.0;
        /*@Rate*/final double riskFreeRate = 0.06;
        final double volatility = 0.3;
        final double dividendYield = 0.00;


        final Date maturity = new Date(3, Month.July, 2026);
        final DayCounter dayCounter = new Actual365Fixed();

        // define line formatting
        //              "         1         2         3         4         5         6         7         8         9"
        //              "123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890"
        //              "                            Method      European      Bermudan      American";
        //              "================================== ============= ============= ============="
        //              "12345678901234567890123456789012345678901234 123.567890123 123.567890123 123.567890123";
        final String fmt    = "%34s %13.9f %13.9f %13.9f\n";


        // write column headings
        //                 "         1         2         3         4         5         6         7         8"
        //                 "12345678901234567890123456789012345678901234567890123456789012345678901234567890"
        System.out.println("                            Method      European      Bermudan      American");
        System.out.println("================================== ============= ============= =============");

        // Define exercise for European Options
//        final Exercise europeanExercise = new EuropeanExercise(maturity);

        // Define exercise for Bermudan Options


        // Define exercise for American Options
        final Exercise americanExercise = new AmericanExercise(settlementDate, maturity);

        // bootstrap the yield/dividend/volatility curves
        final Handle<Quote> underlyingH = new Handle<Quote>(new SimpleQuote(underlying));
        final Handle<YieldTermStructure> flatDividendTS = new Handle<YieldTermStructure>(new FlatForward(settlementDate, dividendYield, dayCounter));
        final Handle<YieldTermStructure> flatTermStructure = new Handle<YieldTermStructure>(new FlatForward(settlementDate, riskFreeRate, dayCounter));
        final Handle<BlackVolTermStructure> flatVolTS = new Handle<BlackVolTermStructure>(new BlackConstantVol(settlementDate, calendar, volatility, dayCounter));
        final Payoff payoff = new PlainVanillaPayoff(type, strike);

        final BlackScholesMertonProcess bsmProcess = new BlackScholesMertonProcess(underlyingH, flatDividendTS, flatTermStructure, flatVolTS);


        // American Options
        final VanillaOption americanOption = new VanillaOption(payoff, americanExercise);


        // Analytic formulas:

        // Black-Scholes for European
        String method = "Black-Scholes";


        // Barone-Adesi and Whaley approximation for American
        method = "Barone-Adesi/Whaley";
        americanOption.setPricingEngine(new BaroneAdesiWhaleyApproximationEngine(bsmProcess));
        System.out.printf(fmt, method, Double.NaN, Double.NaN, americanOption.NPV() );

        // Bjerksund and Stensland approximation for American
        method = "Bjerksund/Stensland";
        americanOption.setPricingEngine(new BjerksundStenslandApproximationEngine(bsmProcess));
        System.out.printf(fmt, method, Double.NaN, Double.NaN, americanOption.NPV() );

        // Ju Quadratic approximation for American
        method = "Ju Quadratic";
        americanOption.setPricingEngine(new JuQuadraticApproximationEngine(bsmProcess));
        System.out.printf(fmt, method, Double.NaN, Double.NaN, americanOption.NPV() );

        // Integral
        method = "Integral";

        int timeSteps = 801;

        // Binomial method
        method = "Binomial Jarrow-Rudd";
        americanOption.setPricingEngine(new BinomialVanillaEngine<JarrowRudd>(JarrowRudd.class, bsmProcess, timeSteps));

        System.out.printf(fmt, method, 0.0, 0.0, americanOption.NPV() );

        method = "Binomial Cox-Ross-Rubinstein";
        ////europeanOption.setPricingEngine(new BinomialVanillaEngine<CoxRossRubinstein>(CoxRossRubinstein.class, bsmProcess, timeSteps));
        //bermudanOptionDel.setPricingEngine(new BinomialVanillaEngine<CoxRossRubinstein>(CoxRossRubinstein.class, bsmProcess, timeSteps));
        americanOption.setPricingEngine(new BinomialVanillaEngine<CoxRossRubinstein>(CoxRossRubinstein.class, bsmProcess, timeSteps));
        if (System.getProperty("EXPERIMENTAL") != null) {

        }
        System.out.printf(fmt, method,0.0, 0.0, americanOption.NPV() );

        method = "Additive EquiProbabilities";
        ////europeanOption.setPricingEngine(new BinomialVanillaEngine<AdditiveEQPBinomialTree>(AdditiveEQPBinomialTree.class, bsmProcess, timeSteps));
        //bermudanOptionDel.setPricingEngine(new BinomialVanillaEngine<AdditiveEQPBinomialTree>(AdditiveEQPBinomialTree.class, bsmProcess, timeSteps));
        americanOption.setPricingEngine(new BinomialVanillaEngine<AdditiveEQPBinomialTree>(AdditiveEQPBinomialTree.class, bsmProcess, timeSteps));
        if (System.getProperty("EXPERIMENTAL") != null) {

        }
        System.out.printf(fmt, method,0.0, 0.0, americanOption.NPV() );

        method = "Binomial Trigeorgis";
        ////europeanOption.setPricingEngine(new BinomialVanillaEngine<Trigeorgis>(Trigeorgis.class, bsmProcess, timeSteps));
        //bermudanOptionDel.setPricingEngine(new BinomialVanillaEngine<Trigeorgis>(Trigeorgis.class, bsmProcess, timeSteps));
        americanOption.setPricingEngine(new BinomialVanillaEngine<Trigeorgis>(Trigeorgis.class, bsmProcess, timeSteps));
        if (System.getProperty("EXPERIMENTAL") != null) {

        }
        System.out.printf(fmt, method,0.0, 0.0, americanOption.NPV() );

        method = "Binomial Tian";
        ////europeanOption.setPricingEngine(new BinomialVanillaEngine<Tian>(Tian.class, bsmProcess, timeSteps));
        //bermudanOptionDel.setPricingEngine(new BinomialVanillaEngine<Tian>(Tian.class, bsmProcess, timeSteps));
        americanOption.setPricingEngine(new BinomialVanillaEngine<Tian>(Tian.class, bsmProcess, timeSteps));
        if (System.getProperty("EXPERIMENTAL") != null) {

        }
        System.out.printf(fmt, method,0.0, 0.0, americanOption.NPV() );

        method = "Binomial Leisen-Reimer";
        ////europeanOption.setPricingEngine(new BinomialVanillaEngine<LeisenReimer>(LeisenReimer.class, bsmProcess, timeSteps));
        //bermudanOptionDel.setPricingEngine(new BinomialVanillaEngine<LeisenReimer>(LeisenReimer.class, bsmProcess, timeSteps));
        americanOption.setPricingEngine(new BinomialVanillaEngine<LeisenReimer>(LeisenReimer.class, bsmProcess, timeSteps));
        if (System.getProperty("EXPERIMENTAL") != null) {

        }
        System.out.printf(fmt, method,0.0, 0.0, americanOption.NPV() );

        method = "Binomial Joshi";
        ////europeanOption.setPricingEngine(new BinomialVanillaEngine<Joshi4>(Joshi4.class, bsmProcess, timeSteps));
        //bermudanOptionDel.setPricingEngine(new BinomialVanillaEngine<Joshi4>(Joshi4.class, bsmProcess, timeSteps));
        americanOption.setPricingEngine(new BinomialVanillaEngine<Joshi4>(Joshi4.class, bsmProcess, timeSteps));
        if (System.getProperty("EXPERIMENTAL") != null) {

        }
        System.out.printf(fmt, method,0.0, 0.0, americanOption.NPV() );


        //
        //
        //

        // Finite differences
        method = "Finite differences";
        ////europeanOption.setPricingEngine(new FDEuropeanEngine(bsmProcess, timeSteps, timeSteps-1, false));
        //bermudanOptionDel.setPricingEngine(new FDBermudanEngine(bsmProcess, timeSteps, timeSteps-1));
        americanOption.setPricingEngine(new FDAmericanEngine(bsmProcess, timeSteps, timeSteps-1, false));
        if (System.getProperty("EXPERIMENTAL") != null) {

        }
        System.out.printf(fmt, method,0.0, 0.0, americanOption.NPV() );

        //
        //
        //


        // Monte Carlo Method
        timeSteps = 1;
        final int mcSeed = 42;
        final int nSamples = 32768; // 2^15
        final int maxSamples = 1048576; // 2^20

        method = "Monte Carlo (crude)";

        // ========================================================================================
        //        ////europeanOption.setPricingEngine(
        //            new MCEuropeanEngine(
        //                "PseudoRandom", timeSteps, 252,
        //                false, false, false,
        //                nSamples, 0.02, maxSamples, mcSeed));
        //        System.out.printf(fmt, method, 0, Double.NaN, Double.NaN);
        // ========================================================================================

        method = "Monte Carlo (Sobol)";
        //        ////europeanOption.setPricingEngine(
        //            new MCEuropeanEngine(
        //                "LowDiscrepancy", timeSteps, 252,
        //                false, false, false,
        //                nSamples, 0.02, maxSamples, mcSeed));
        //        System.out.printf(fmt, method, 0, Double.NaN, Double.NaN);
        //


        //        MakeMCAmericanEngine<PseudoRandom>().withSteps(100)
        //            .withAntitheticVariate()
        //            .withCalibrationSamples(4096)
        //            .withTolerance(0.02)
        // .           withSeed(mcSeed);
        //        System.out.printf(fmt, method, 0, Double.NaN, Double.NaN);



    }

}