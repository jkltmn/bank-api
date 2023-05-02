package com.joshkaltman.emceestudios.bankapi.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

@Data
@Validated
public class CreateBankAccountRequest {
    @NotNull
    @Pattern(regexp = "\\d{4}")
    private String pin;

    @NotNull
    @Pattern(regexp = "[a-zA-Z]+")
    private String firstName;

    @NotNull
    @Pattern(regexp = "[a-zA-Z]+")
    private String lastName;

    @Pattern(regexp = "USD" /*"[A-Z]{3}"*/)
    private String currency = "USD";
}
