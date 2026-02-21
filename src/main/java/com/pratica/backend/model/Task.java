package com.pratica.backend.model;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {

    private Long id;

    @NotBlank(message = "The description field is mandatory")
    private String description;

    private Status status;

    @FutureOrPresent(message = "The date dont to be before the today")
    private LocalDate dateLimit;
}
