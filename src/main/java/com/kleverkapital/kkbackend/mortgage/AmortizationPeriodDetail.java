package com.kleverkapital.kkbackend.mortgage;


public class AmortizationPeriodDetail {
    int period;
    double principalPayment;
    double interestPayment;
    double loanBalance;

    public AmortizationPeriodDetail(int period, double principalPayment, double interestPayment, double loanBalance) {
        this.period = period;
        this.principalPayment = principalPayment;
        this.interestPayment = interestPayment;
        this.loanBalance = loanBalance;
    }

    public AmortizationPeriodDetail() {
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }

    public double getPrincipalPayment() {
        return principalPayment;
    }

    public void setPrincipalPayment(double principalPayment) {
        this.principalPayment = principalPayment;
    }

    public double getInterestPayment() {
        return interestPayment;
    }

    public void setInterestPayment(double interestPayment) {
        this.interestPayment = interestPayment;
    }

    public double getLoanBalance() {
        return loanBalance;
    }

    public void setLoanBalance(double loanBalance) {
        this.loanBalance = loanBalance;
    }
}
