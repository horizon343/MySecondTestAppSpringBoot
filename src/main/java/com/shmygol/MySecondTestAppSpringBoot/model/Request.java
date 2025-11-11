package com.shmygol.MySecondTestAppSpringBoot.model;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

/**
 * Класс представляет структуру входящего сообщения (request).
 * Содержит информацию о системе, операции и параметрах коммуникации.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Request {

    /**
     * Уникальный идентификатор сообщения.
     * Обязательное поле, до 32 символов.
     */
    @NotBlank
    @Length(min = 1, max = 32)
    @NotNull
    private String uid;

    /**
     * Уникальный идентификатор операции.
     * Обязательное поле, до 32 символов.
     */
    @NotBlank
    @Length(min = 1, max = 32)
    @NotNull
    private String operationUid;

    /**
     * Имя системы-отправителя (например, "CRM", "ERP").
     * Необязательное поле.
     */
    private Systems systemName;

    /**
     * Время создания сообщения в формате строки.
     * Обязательное поле (например: "2025-11-11T12:30:00Z").
     */
    @NotBlank
    @NotNull
    private String systemTime;

    /**
     * Наименование ресурса.
     * Необязательное поле.
     */
    private String source;

    /**
     * Должность сотрудника, связанная с запросом.
     * Необязательное поле.
     */
    private Positions positions;

    /**
     * Уникальный идентификатор коммуникации.
     * Обязательное поле.
     * Значение должно быть в диапазоне от 1 до 100000.
     */
    @Max(100000)
    @Min(1)
    @NotNull
    private int communicationId;

    /**
     * Уникальный идентификатор шаблона.
     * Необязательное поле.
     */
    private int templateId;

    /**
     * Код продукта, связанного с запросом.
     * Необязательное поле.
     */
    private int productCode;

    /**
     * СМС-код, используемый для подтверждения операции.
     * Необязательное поле.
     */
    private int smsCode;

    @Override
    public String toString() {
        return "{" +
                "uid=" + uid +
                ", operationUid=" + operationUid +
                ", systemName=" + systemName +
                ", systemTime=" + systemTime +
                ", source=" + source +
                ", communicationId=" + communicationId +
                ", templateId=" + templateId +
                ", productCode=" + productCode +
                ", smsCode=" + smsCode +
                "}";
    }
}
