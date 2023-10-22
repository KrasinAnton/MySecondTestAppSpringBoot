package ru.krasin.MySecondTestAppSpringBoot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.krasin.MySecondTestAppSpringBoot.exception.UnsupportedCodeException;
import ru.krasin.MySecondTestAppSpringBoot.exception.ValidationFailedException;
import ru.krasin.MySecondTestAppSpringBoot.model.*;
import ru.krasin.MySecondTestAppSpringBoot.service.*;
import ru.krasin.MySecondTestAppSpringBoot.util.DateTimeUtil;

import javax.validation.Valid;
import java.util.Date;

@Slf4j
@RestController
public class MyController {
    private final ValidationService validationService;
    private final ModifyResponseService modifyResponseService;
    private final ModifyRequestService modifyRequestService;
    private final AnnualBonusService annualBonusService;
    private final QuarterlyBonusService quarterlyBonusService;

    @Autowired
    public MyController(
            ValidationService validationService,
            @Qualifier("ModifySystemTimeResponseService") ModifyResponseService modifyResponseService,
            ModifyRequestService modifyRequestService,
            AnnualBonusService annualBonusService,
            QuarterlyBonusService quarterlyBonusService) {
        this.validationService = validationService;
        this.modifyResponseService = modifyResponseService;
        this.modifyRequestService = modifyRequestService;
        this.annualBonusService = annualBonusService;
        this.quarterlyBonusService = quarterlyBonusService;
    }

    @PostMapping(value = "/feedback")
    public ResponseEntity<Response> feedback(@Valid @RequestBody Request request,
                                             BindingResult bindingResult) {
        log.info("request: {}", request);

        // Проверить, если uid равен "123", и, если так, выбросить исключение
        if ("123".equals(request.getUid())) {
            return handleException(new UnsupportedCodeException());
        }

        Response response = createResponse(request);

        // Вычислить годовую и квартальную премии
        calculateBonuses(request, response);

        // Выполнить валидацию и обработать исключения
        return handleValidation(request, bindingResult, response);
    }

    private void calculateBonuses(Request request, Response response) {
        double annualBonus = annualBonusService.calculate(
                request.getPosition(), request.getSalary(), request.getBonus(), request.getWorkDays()
        );
        response.setAnnualBonus(annualBonus);

        double quarterlyBonus = quarterlyBonusService.calculate(
                request.getPosition(), request.getSalary(), request.getBonus(), request.getWorkDays()
        );
        response.setQuarterlyBonus(quarterlyBonus);
    }

    private Response createResponse(Request request) {
        return Response.builder()
                .uid(request.getUid())
                .operationUid(request.getOperationUid())
                .systemTime(DateTimeUtil.getCustomFormat().format(new Date()))
                .code(Codes.SUCCESS)
                .errorCode(ErrorCodes.EMPTY)
                .errorMessage(ErrorMessages.EMPTY)
                .build();
    }

    private ResponseEntity<Response> handleValidation(Request request,
                                                      BindingResult bindingResult, Response response) {
        try {
            validationService.isValid(bindingResult);
        } catch (ValidationFailedException e) {
            log.error("Validation failed: {}", e.getMessage());
            response.setErrorCode(ErrorCodes.VALIDATION_EXCEPTION);
            response.setErrorMessage(ErrorMessages.VALIDATION);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return handleException(e);
        }

        modifyResponseService.modify(response);
        modifyRequestService.modify(request);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private ResponseEntity<Response> handleException(Exception ex) {
        log.error("Handling exception: {}", ex.getMessage());
        Response response = Response.builder()
                .code(Codes.FAILED)
                .errorCode(ErrorCodes.UNKNOWN_EXCEPTION)
                .errorMessage(ErrorMessages.UNKNOWN)
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
//Изменения включают в себя выделение логики в отдельные методы
// (calculateBonuses, handleValidation, handleException)
// для упрощения чтения и уменьшения дублирования кода.