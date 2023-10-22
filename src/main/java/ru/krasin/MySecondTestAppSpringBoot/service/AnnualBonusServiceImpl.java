package ru.krasin.MySecondTestAppSpringBoot.service;

import org.springframework.stereotype.Service;
import ru.krasin.MySecondTestAppSpringBoot.model.Positions;

@Service
public class AnnualBonusServiceImpl implements AnnualBonusService {
    @Override
    public double calculate(Positions positions, double salary, double bonus, int workDays) {
        // Определяем, является ли текущий год високосным
        int currentYear = java.time.Year.now().getValue();
        boolean isLeapYear = java.time.Year.isLeap(currentYear);

        // Используем соответствующее количество дней в году в расчете
        int daysInYear = isLeapYear ? 366 : 365;

        // Вычисляем бонус, учитывая количество дней в году
        return salary * bonus * daysInYear * positions.getPositionCoefficient() / workDays;
    }
}