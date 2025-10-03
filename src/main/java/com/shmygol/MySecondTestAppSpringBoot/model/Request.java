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

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Request {
    @NotBlank
    @Length(min = 1, max = 32)
    @NotNull
    private String uid;

    @NotBlank
    @Length(min = 1, max = 32)
    @NotNull
    private String operationUid;

    private String systemName;

    @NotBlank
    @NotNull
    private String systemTime;

    private String source;

    @Max(100000)
    @Min(1)
    @NotNull
    private int communicationId;

    private int templateId;

    private int productCode;

    private int smsCode;
}
