package ru.krasin.MySecondTestAppSpringBoot.service;

import org.springframework.stereotype.Service;
import ru.krasin.MySecondTestAppSpringBoot.model.Positions;

@Service
public class QuarterlyBonusServiceImpl implements QuarterlyBonusService {
    @Override
    public double calculate(Positions positions, double salary, double bonus, int workDays) {
        if (positions.isManager()) {
            // Квартальная премия для менеджеров
            // Для простоты, предположим, что квартал состоит из 3 месяцев
            // Исходя из этого, мы делим годовую зарплату на 4, так как квартальная премия
            // начисляется каждый квартал
            return (salary / 4) * bonus / workDays;
        } else {
            // Для остальных позиций (не-управленцев) квартальная премия равна 0
            return 0;
        }
    }
}
