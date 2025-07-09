package com.kleverkapital.kkbackend;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class BMICalculator {
    @GetMapping("/BMI")
    public double calculateBMI(double height, double weight, String units) {
        System.out.println("BMI calculation");
        if (units.equalsIgnoreCase("customary")) {
            height = height * 0.0254;
            weight = weight * 0.453592;
        }
        return weight / (height * height);
    }
}
