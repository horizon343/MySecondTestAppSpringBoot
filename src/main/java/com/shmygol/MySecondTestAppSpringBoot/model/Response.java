package com.shmygol.MySecondTestAppSpringBoot.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

/**
 * Класс представляет структуру ответного сообщения (response).
 * Содержит результат обработки входящего запроса.
 */
@Data
@Builder
public class Response {

    /**
     * Уникальный идентификатор сообщения.
     * Обязательное поле.
     */
    @NotBlank
    private String uid;

    /**
     * Уникальный идентификатор операции.
     * Обязательное поле.
     */
    @NotBlank
    private String operationUid;

    /**
     * Имя системы-отправителя ответа.
     * Обязательное поле (например: "ERP").
     */
    @NotBlank
    private String systemTime;

    /**
     * Код результата выполнения операции.
     * Возможные значения: "success" или "failed".
     * Обязательное поле.
     */
    @NotBlank
    private Codes code;

    /**
     * Код ошибки, если операция завершилась неуспешно.
     * Возможные значения: "UnsupportedCodeException", "ValidationException", "UnknownException".
     * Обязательное поле.
     */
    @NotBlank
    private ErrorCodes errorCode;

    /**
     * Текстовое описание ошибки.
     * Возможные значения: "Не поддерживаемая ошибка", "Ошибка валидации", "Произошла непредвиденная ошибка".
     * Обязательное поле.
     */
    @NotBlank
    private ErrorMessages errorMessage;
}
