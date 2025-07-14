package com.kleverkapital.kkbackend.mortgage;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class MortgageCalculatorTest
{
    @Test
    public void simpleCalculations()
    {
        MortgageCalculator calc = new MortgageCalculator();
        assertEquals(1330.604, calc.calculateMonthlyPayment(200000, 0.07, 30),0.001);
        assertEquals(1199.101, calc.calculateMonthlyPayment(200000,
                0.06, 30),0.001);
    }

    @Test
    public void simpleSchedule()
    {
        MortgageCalculator calc = new MortgageCalculator();
        var schedule = calc.generateSchedule(200000,
                7,
                30, 0);
       assertEquals(1330.604 ,schedule.getMonthlyPayment(), 0.001);
       assertEquals(360, schedule.getDetails().size());

       assertEquals(schedule.getDetails().get(schedule.getDetails().size()-1).loanBalance, 0, 0.01);
    }

    @Test
    public void loanShouldBePositive()
    {
        MortgageCalculator calc = new MortgageCalculator();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            // Code that should throw exception
            calc.calculateMonthlyPayment(-200000, 0.07, 30);
        });
    }

    @Test
    public void yearlyInterestRateShouldBePositive()
    {
        MortgageCalculator calc = new MortgageCalculator();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            // Code that should throw exception
            calc.calculateMonthlyPayment(200000, -0.07, 30);
        });
    }
    @Test
    public void yearsShouldBePositive()
    {
        MortgageCalculator calc = new MortgageCalculator();

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            // Code that should throw exception
            calc.calculateMonthlyPayment(200000, 0.07, -30);
        });
    }
}