package com.kleverkapital.kkbackend.mortgage;

import java.util.List;

public class AmortizationSchedule {

    private double monthlyPayment;
    private List<AmortizationPeriodDetail> details;

    public AmortizationSchedule(double monthlyPayment, List<AmortizationPeriodDetail> details) {
        this.monthlyPayment = monthlyPayment;
        this.details = details;
    }

    public AmortizationSchedule() {
    }

    public List<AmortizationPeriodDetail> getDetails() {
        return details;
    }

    public void setDetails(List<AmortizationPeriodDetail> details) {
        this.details = details;
    }

    public double getMonthlyPayment() {
        return monthlyPayment;
    }

    public void setMonthlyPayment(double monthlyPayment) {
        this.monthlyPayment = monthlyPayment;
    }
}
