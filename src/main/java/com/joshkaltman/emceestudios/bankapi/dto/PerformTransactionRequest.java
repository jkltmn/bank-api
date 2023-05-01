package com.joshkaltman.emceestudios.bankapi.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
public class PerformTransactionRequest {
    @NotNull
    @Pattern(regexp = "\\d{4}")
    private String pin;

    @Pattern(regexp = "(-)?\\d+\\.\\d{2}")
    private String amount;
}
