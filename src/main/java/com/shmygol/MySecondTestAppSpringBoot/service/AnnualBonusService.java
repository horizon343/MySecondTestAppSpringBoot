package com.shmygol.MySecondTestAppSpringBoot.service;

import com.shmygol.MySecondTestAppSpringBoot.model.Positions;
import org.springframework.stereotype.Service;

@Service
public interface AnnualBonusService {
    double calculate(Positions positions, double selary, double bonus, int workDays);
}
