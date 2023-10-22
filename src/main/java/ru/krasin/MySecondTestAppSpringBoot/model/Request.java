package ru.krasin.MySecondTestAppSpringBoot.model;

import lombok.*;

import javax.validation.constraints.*;
import java.util.Objects;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Request {

    @NotBlank(message = "Уникальный идентификатор сообщения обязателен")
    @Size(max = 32, message = "Уникальный идентификатор сообщения должен содержать не более 32 символов")
    private String uid;  // Уникальный идентификатор сообщения

    @NotBlank(message = "Уникальный идентификатор операции обязателен")
    @Size(max = 32, message = "Уникальный идентификатор операции должен содержать не более 32 символов")
    private String operationUid;  // Уникальный идентификатор операции

    private String systemName;  // Изменили тип на перечисление Systems

    @NotBlank(message = "Время создания сообщения обязательно")
    private String systemTime;  // Время создания сообщения

    private String source;
    @NotNull(message = "Должность должна быть указана")
    private Positions position;  // Должность
    private Double salary;
    private Double bonus;
    private Integer workDays;

    @Min(value = 1, message = "Уникальный идентификатор коммуникации должен быть не менее 1")
    @Max(value = 100000, message = "Уникальный идентификатор коммуникации должен быть не более 100000")
    private Integer communicationId;  // Уникальный идентификатор коммуникации

    private Integer templateId;
    private Integer productCode;
    private Integer smsCode;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Request request = (Request) o;
        return communicationId == request.communicationId &&
                templateId == request.templateId &&
                productCode == request.productCode &&
                smsCode == request.smsCode &&
                Double.compare(request.salary, salary) == 0 &&
                Double.compare(request.bonus, bonus) == 0 &&
                workDays == request.workDays &&
                uid.equals(request.uid) &&
                Objects.equals(operationUid, request.operationUid) &&
                systemName == request.systemName &&
                systemTime.equals(request.systemTime) &&
                source.equals(request.source) &&
                position == request.position;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uid, operationUid, systemName, systemTime,
                source, position, salary, bonus, workDays, communicationId,
                templateId, productCode, smsCode);
    }

    @Override
    public String toString() {
        return "Request{" +
                "uid='" + uid + '\'' +
                ", operationUid='" + operationUid + '\'' +
                ", systemName=" + systemName +
                ", systemTime='" + systemTime + '\'' +
                ", source='" + source + '\'' +
                ", position=" + position +
                ", salary=" + salary +
                ", bonus=" + bonus +
                ", workDays=" + workDays +
                ", communicationId=" + communicationId +
                ", templateId=" + templateId +
                ", productCode=" + productCode +
                ", smsCode=" + smsCode +
                '}';
    }

}
