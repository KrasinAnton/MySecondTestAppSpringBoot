package ru.krasin.MySecondTestAppSpringBoot.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.krasin.MySecondTestAppSpringBoot.exception.UnsupportedCodeException;
import ru.krasin.MySecondTestAppSpringBoot.exception.ValidationFailedException;
import ru.krasin.MySecondTestAppSpringBoot.model.*;
import ru.krasin.MySecondTestAppSpringBoot.service.ModifyRequestService;
import ru.krasin.MySecondTestAppSpringBoot.service.ModifyResponseService;
import ru.krasin.MySecondTestAppSpringBoot.service.ValidationService;
import ru.krasin.MySecondTestAppSpringBoot.util.DateTimeUtil;

import javax.validation.Valid;
import java.util.Date;

@Slf4j
@RestController
public class MyController {
    private final ValidationService validationService;
    private final ModifyResponseService modifyResponseService;
    private final ModifyRequestService modifyRequestService;

    @Autowired
    public MyController(ValidationService validationService,
                        @Qualifier("ModifySystemTimeResponseService") ModifyResponseService modifyResponseService,
                         ModifyRequestService modifyRequestService) {
        this.validationService = validationService;
        this.modifyResponseService = modifyResponseService;
        this.modifyRequestService = modifyRequestService;
    }

    @PostMapping(value = "/feedback")
    public ResponseEntity<Response> feedback(@Valid @RequestBody Request request, BindingResult bindingResult) throws UnsupportedCodeException {
        log.info("request: {}", request);

        if ("123".equals(request.getUid())) {
            log.error("Received request with uid '123', throwing UnsupportedCodeException");
            throw new UnsupportedCodeException();
        }

        Response response = createResponse(request);

        try {
            validationService.isValid(bindingResult);
        } catch (ValidationFailedException e) {
            log.error("Validation failed: {}", e.getMessage());
            return new ResponseEntity<>(createErrorResponse(e), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(createErrorResponse(e), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        modifyResponseService.modify(response);
        modifyRequestService.modify(request);

        return new ResponseEntity<>(response, HttpStatus.OK);
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

    private Response createErrorResponse(Exception e) {
        log.error("An exception occurred: {}", e.getMessage());
        Response response = Response.builder()
                .code(Codes.FAILED)
                .errorCode(ErrorCodes.UNKNOWN_EXCEPTION)
                .errorMessage(ErrorMessages.UNKNOWN)
                .build();
        if (e instanceof ValidationFailedException) {
            response.setErrorCode(ErrorCodes.VALIDATION_EXCEPTION);
            response.setErrorMessage(ErrorMessages.VALIDATION);
        }
        return response;
    }

    @ExceptionHandler({ValidationFailedException.class, UnsupportedCodeException.class})
    public ResponseEntity<Response> handleException(Exception ex) {
        log.error("Handling exception: {}", ex.getMessage());
        return new ResponseEntity<>(createErrorResponse(ex), HttpStatus.BAD_REQUEST);
    }
}