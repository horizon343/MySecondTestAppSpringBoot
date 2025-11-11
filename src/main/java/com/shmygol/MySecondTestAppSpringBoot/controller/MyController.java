package com.shmygol.MySecondTestAppSpringBoot.controller;

import com.shmygol.MySecondTestAppSpringBoot.exception.UnsupportedCodeException;
import com.shmygol.MySecondTestAppSpringBoot.exception.ValidationFailedException;
import com.shmygol.MySecondTestAppSpringBoot.model.*;
import com.shmygol.MySecondTestAppSpringBoot.service.*;
import com.shmygol.MySecondTestAppSpringBoot.utils.DateTimeUtil;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;

@RestController
@Slf4j
public class MyController {

    private final ValidationService validationService;
    private final ModifyRequestService modifySourceRequestService;
    private final ModifyRequestService modifySystemNameRequestService;
    private final ModifyResponseService modifyResponseService;
    private final AnnualBonusService annualBonusService;

    @Autowired
    public MyController(
            ValidationService validationService,
            @Qualifier("ModifySystemTimeResponseService") ModifyResponseService modifyResponseService,
            @Qualifier("ModifySourceRequestService") ModifyRequestService modifySourceRequestService,
            @Qualifier("ModifySystemNameRequestService") ModifyRequestService modifySystemNameRequestService,
            AnnualBonusService annualBonusService
    ) {
        this.validationService = validationService;
        this.modifySourceRequestService = modifySourceRequestService;
        this.modifySystemNameRequestService = modifySystemNameRequestService;
        this.modifyResponseService = modifyResponseService;
        this.annualBonusService = annualBonusService;
    }

    @PostMapping("/feedback")
    public ResponseEntity<Response> feedback(@Valid @RequestBody Request request, BindingResult bindingResult) {
        log.info("request: {}", request);
        logRequestTiming(request);

        Response response = buildBaseResponse(request);

        try {
            validateRequest(request, bindingResult);
        } catch (ValidationFailedException e) {
            return buildErrorResponse(response, ErrorCodes.VALIDATION_EXCEPTION, ErrorMessages.VALIDATION, HttpStatus.BAD_REQUEST);
        } catch (UnsupportedCodeException e) {
            return buildErrorResponse(response, ErrorCodes.UNSUPPORTED_EXCEPTION, ErrorMessages.UNSUPPORTED, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return buildErrorResponse(response, ErrorCodes.UNKNOWN_EXCEPTION, ErrorMessages.UNKNOWN, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        modifyRequestAndResponse(request, response);
        return ResponseEntity.ok(modifyResponseService.modify(response));
    }

    private void logRequestTiming(Request request) {
        try {
            if (request.getSystemTime() != null) {
                Instant sentTime = Instant.parse(request.getSystemTime());
                long diffMillis = Duration.between(sentTime, Instant.now()).toMillis();
                log.info("Время между отправкой и получением: {} мс", diffMillis);
            } else {
                log.warn("Отсутствует поле systemTime — невозможно вычислить разницу во времени");
            }
        } catch (Exception e) {
            log.warn("Ошибка при разборе systemTime: {}", e.getMessage());
        }
    }

    private Response buildBaseResponse(Request request) {
        return Response.builder()
                .uid(request.getUid())
                .operationUid(request.getOperationUid())
                .systemTime(DateTimeUtil.getCustomFormat().format(new Date()))
                .code(Codes.SUCCESS)
                .errorCode(ErrorCodes.EMPTY)
                .errorMessage(ErrorMessages.EMPTY)
                .build();
    }

    private void validateRequest(Request request, BindingResult bindingResult) {
        validationService.isValid(bindingResult);
        if ("123".equals(request.getUid())) {
            throw new UnsupportedCodeException("uid == 123");
        }
    }

    private ResponseEntity<Response> buildErrorResponse(Response response,
                                                        ErrorCodes errorCode,
                                                        ErrorMessages errorMessage,
                                                        HttpStatus status) {
        response.setCode(Codes.FAILED);
        response.setErrorCode(errorCode);
        response.setErrorMessage(errorMessage);
        log.error("Ошибка обработки запроса: {}", errorMessage);
        return new ResponseEntity<>(response, status);
    }

    private void modifyRequestAndResponse(Request request, Response response) {
//        modifySystemNameRequestService.modify(request);
//        modifySourceRequestService.modify(request);
        modifyResponseService.modify(response);
    }
}
