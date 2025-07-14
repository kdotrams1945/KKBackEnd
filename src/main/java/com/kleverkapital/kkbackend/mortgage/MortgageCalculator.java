package com.kleverkapital.kkbackend.mortgage;


import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;

public class MortgageCalculator {

    public static final int PAYMENTS_PER_YEAR = 12;

    @GetMapping("/monthlyPayment")
    public double calculateMonthlyPayment(double loan,
                                          double yearlyInterestRate,
                                          int years) {
        if (loan < 0 || yearlyInterestRate < 0 || years < 0) {
            throw new IllegalArgumentException("Values should be positive.");
        }
        double interestRate = yearlyInterestRate / PAYMENTS_PER_YEAR;
        int numPayments = years * PAYMENTS_PER_YEAR;
        double x = Math.pow((1 + interestRate), numPayments);
        return loan * ((interestRate * x) / (x - 1));
    }

    @GetMapping("/amortizationSchedule")
    public AmortizationSchedule generateSchedule(double loan,
                                                 double yearlyInterestRate,
                                                 int years,
                                                 double extraPayment) {
        double rate = yearlyInterestRate / 100;
        double monthlyPayment = calculateMonthlyPayment(loan, rate, years);
        ArrayList<AmortizationPeriodDetail> periodDetails = getAmortizationPeriodDetails(loan, years,
                extraPayment, rate, monthlyPayment);
        return new AmortizationSchedule(monthlyPayment, periodDetails);

    }

    private ArrayList<AmortizationPeriodDetail> getAmortizationPeriodDetails(double loan, int years, double extraPayment, double rate, double monthlyPayment) {
        double outstandingLoan = loan;
        ArrayList<AmortizationPeriodDetail> periodDetails = new ArrayList<>();
        for (int i = 1; i <= years * PAYMENTS_PER_YEAR; i++) {
            double interestPayment = outstandingLoan * rate / PAYMENTS_PER_YEAR;
            double principalPayment = monthlyPayment + extraPayment - interestPayment;
            if (outstandingLoan > principalPayment) {
                outstandingLoan -= principalPayment;
            } else {
                principalPayment = outstandingLoan;
                outstandingLoan = 0;
            }

            periodDetails.add(new AmortizationPeriodDetail(i, principalPayment, interestPayment, outstandingLoan));
            if (outstandingLoan <= 0)
                break;
        }
        return periodDetails;
    }
}