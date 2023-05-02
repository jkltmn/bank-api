package com.joshkaltman.emceestudios.bankapi.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
public class PerformTransactionRequest {
    @Pattern(regexp = "(-)?\\d+\\.\\d{2}")
    private String amount;
}
