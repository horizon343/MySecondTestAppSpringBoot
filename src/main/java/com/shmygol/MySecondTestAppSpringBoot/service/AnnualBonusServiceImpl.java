package com.shmygol.MySecondTestAppSpringBoot.service;

import com.shmygol.MySecondTestAppSpringBoot.model.Positions;
import org.springframework.stereotype.Service;

import java.time.Year;

@Service
public class AnnualBonusServiceImpl implements AnnualBonusService {
    @Override
    public double calculate(Positions positions, double salary, double bonus, int workDays) {
        int currentYear = Year.now().getValue();
        int daysInYear = Year.isLeap(currentYear) ? 366 : 365;

        return salary * bonus * daysInYear * positions.getPositionCoefficient() / workDays;
    }
}
