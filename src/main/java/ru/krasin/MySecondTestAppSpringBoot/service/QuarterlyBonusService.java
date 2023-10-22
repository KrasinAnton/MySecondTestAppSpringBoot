package ru.krasin.MySecondTestAppSpringBoot.service;

import org.springframework.stereotype.Service;
import ru.krasin.MySecondTestAppSpringBoot.model.Positions;

@Service
public interface QuarterlyBonusService {
    double calculate(Positions positions, double salary, double bonus, int workDays);
}


