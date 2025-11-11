package com.shmygol.MySecondTestAppSpringBoot.service;

import com.shmygol.MySecondTestAppSpringBoot.model.Positions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class QuarterlyBonusServiceTest {

    private QuarterlyBonusService quarterlyBonusService;

    @BeforeEach
    void setUp() {
        quarterlyBonusService = new QuarterlyBonusService();
    }

    @Test
    void shouldCalculateQuarterlyBonusForManager() {
        Positions position = Positions.TL;
        double salary = 1000.0;
        double bonusRate = 0.2;

        double result = quarterlyBonusService.calculateQuarterlyBonus(position, salary, bonusRate);

        double expected = salary * bonusRate * 3 * position.getPositionCoefficient();
        assertEquals(expected, result, 0.0001, "Квартальная премия рассчитана неверно");
    }

    @Test
    void shouldReturnZeroForNonManager() {
        Positions position = Positions.DEV;
        double salary = 1000.0;
        double bonusRate = 0.2;

        double result = quarterlyBonusService.calculateQuarterlyBonus(position, salary, bonusRate);

        assertEquals(0.0, result, "Для неуправленца премия должна быть равна 0");
    }

    @Test
    void shouldCalculateCorrectlyForCTO() {
        Positions position = Positions.CTO;
        double salary = 2000.0;
        double bonusRate = 0.15;

        double result = quarterlyBonusService.calculateQuarterlyBonus(position, salary, bonusRate);

        double expected = salary * bonusRate * 3 * position.getPositionCoefficient();
        assertEquals(expected, result, 0.0001);
    }
}
