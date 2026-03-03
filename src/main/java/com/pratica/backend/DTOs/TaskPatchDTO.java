package com.pratica.backend.DTOs;

import com.pratica.backend.model.Status;
import jakarta.validation.constraints.FutureOrPresent;
import java.time.LocalDate;

public record TaskPatchDTO(
        String description,
        Status status,

        @FutureOrPresent
        LocalDate dateLimit
) {
}
