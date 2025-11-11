package com.shmygol.MySecondTestAppSpringBoot.service;

import com.shmygol.MySecondTestAppSpringBoot.model.Positions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class AnnualBonusServiceImplTest {

    @Test
    void calculate() {
        Positions positions = Positions.HR;
        double bonus = 2.0;
        int workDays = 243;
        double salery = 100000.0;

        double result = new AnnualBonusServiceImpl().calculate(positions, salery, bonus, workDays);

        double expectedResult = 360493.8271604938;
        assertThat(result).isEqualTo(expectedResult);
    }
}
