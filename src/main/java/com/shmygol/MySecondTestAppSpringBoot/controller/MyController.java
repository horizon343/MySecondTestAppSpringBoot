package com.shmygol.MySecondTestAppSpringBoot.controller;

import com.shmygol.MySecondTestAppSpringBoot.exception.UnsupportedCodeException;
import com.shmygol.MySecondTestAppSpringBoot.exception.ValidationFailedException;
import com.shmygol.MySecondTestAppSpringBoot.model.*;
import com.shmygol.MySecondTestAppSpringBoot.service.ModifyRequestService;
import com.shmygol.MySecondTestAppSpringBoot.service.ModifyResponseService;
import com.shmygol.MySecondTestAppSpringBoot.service.ValidationService;
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

import java.time.Instant;
import java.util.Date;

@RestController
@Slf4j
public class MyController {
    private final ValidationService validationService;
    private final ModifyRequestService modifySourceRequestService;
    private final ModifyRequestService modifySystemNameRequestService;
    private final ModifyResponseService modifyResponseService;

    @Autowired
    public MyController(
            ValidationService validationService,
            @Qualifier("ModifySystemTimeResponseService") ModifyResponseService modifyResponseService,
            @Qualifier("ModifySourceRequestService") ModifyRequestService modifySourceRequestService,
            @Qualifier("ModifySystemNameRequestService") ModifyRequestService modifySystemNameRequestService
    ) {
        this.validationService = validationService;
        this.modifySourceRequestService = modifySourceRequestService;
        this.modifySystemNameRequestService = modifySystemNameRequestService;
        this.modifyResponseService = modifyResponseService;
    }

    @PostMapping(value = "/feedback")
    public ResponseEntity<Response> feedback(@Valid @RequestBody Request request, BindingResult bindingResult) {

        log.info("request: {}", request);

        Instant receivedTime = Instant.now();
        if (request.getSystemTime() != null) {
            Instant sentTime = Instant.parse(request.getSystemTime());
            long diffMillis = java.time.Duration.between(sentTime, receivedTime).toMillis();
            log.info("Время между отправкой (Service 1) и получением (Service 2): {} мс", diffMillis);
        } else {
            log.warn("В запросе отсутствует поле sentTime — невозможно вычислить разницу во времени");
        }

        Response response = Response.builder()
                .uid(request.getUid())
                .operationUid(request.getOperationUid())
                .systemTime(DateTimeUtil.getCustomFormat().format(new Date()))
                .code(Codes.SUCCESS)
                .errorCode(ErrorCodes.EMPTY)
                .errorMessage(ErrorMessages.EMPTY)
                .build();

        log.info("response: {}", response);

        try {
            validationService.isValid(bindingResult);

            if (request.getUid().equals("123")) {
                throw new UnsupportedCodeException("uid == 123");
            }
        } catch (ValidationFailedException e) {
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.VALIDATION_EXCEPTION);
            response.setErrorMessage(ErrorMessages.VALIDATION);

            log.error("message: {}", ErrorMessages.VALIDATION);

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (UnsupportedCodeException e) {
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.UNSUPPORTED_EXCEPTION);
            response.setErrorMessage(ErrorMessages.UNSUPPORTED);

            log.error("message: {}", ErrorMessages.UNSUPPORTED);

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            response.setCode(Codes.FAILED);
            response.setErrorCode(ErrorCodes.UNKNOWN_EXCEPTION);
            response.setErrorMessage(ErrorMessages.UNKNOWN);

            log.error("message: {}", ErrorMessages.UNKNOWN);

            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        modifyResponseService.modify(response);
        modifySystemNameRequestService.modify(request);
        modifySourceRequestService.modify(request);

        return new ResponseEntity<>(modifyResponseService.modify(response), HttpStatus.OK);
    }
}
