package com.shmygol.MySecondTestAppSpringBoot.service;

import com.shmygol.MySecondTestAppSpringBoot.model.Positions;
import org.springframework.stereotype.Service;

@Service
public class QuarterlyBonusService {
    public double calculateQuarterlyBonus(Positions position, double salary, double bonusRate) {
        if (!position.isManager()) {
            return 0.0;
        }

        return salary * bonusRate * 3 * position.getPositionCoefficient();
    }
}
