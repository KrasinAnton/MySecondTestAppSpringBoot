package ru.krasin.MySecondTestAppSpringBoot.model;

import lombok.Getter;

@Getter
public enum Positions {
    DEV(2.2, false),    //Разработчик
    HR(1.2, false),     //Специалист по персоналу
    TL(2.6, true),      // Менеджер
    SALES (2.8, true), // Дополнительная позиция: Менеджер по продажам
    SE(2.0, false),     // Дополнительная позиция: Системный инженер
    BA(1.8, false);   // Дополнительная позиция: Бизнес-аналитик

    private final double positionCoefficient;
    private final boolean isManager;

    Positions(double positionCoefficient, boolean isManager) {
        this.positionCoefficient = positionCoefficient;
        this.isManager = isManager;
    }

    public boolean isManager() {
        return isManager;
    }
}
