package com.joshkaltman.emceestudios.bankapi.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
public class CreateSessionRequest {
    @NotNull
    @Pattern(regexp = "\\d{4}")
    private String pin;
}
