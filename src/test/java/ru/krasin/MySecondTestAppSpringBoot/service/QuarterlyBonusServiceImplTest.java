package ru.krasin.MySecondTestAppSpringBoot.service;

import org.junit.jupiter.api.Test;
import ru.krasin.MySecondTestAppSpringBoot.model.Positions;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class QuarterlyBonusServiceImplTest {

    @Test
    public void testCalculateForManager() {
        QuarterlyBonusService bonusService = new QuarterlyBonusServiceImpl();

        // Предположим, что менеджер с зарплатой 40000 и бонусом 2.0
        // Рабочих дней в квартале: 60
        double result = bonusService.calculate(Positions.TL, 40000, 2.0, 60);

        // Ожидаемый результат: (40000 / 4) * 2.0 / 60 = 66.67
        assertEquals(66.67, result, 0.01);  // Указываем допустимую погрешность
    }

    @Test
    public void testCalculateForNonManager() {
        QuarterlyBonusService bonusService = new QuarterlyBonusServiceImpl();

        // Предположим, что сотрудник (не-менеджер) с зарплатой 30000 и бонусом 1.5
        // Рабочих дней в квартале: 65
        double result = bonusService.calculate(Positions.DEV, 30000, 1.5, 65);

        // Ожидаемый результат для не-менеджера: 0
        assertEquals(0, result, 0.01);
    }
}
