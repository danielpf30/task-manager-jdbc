package com.pratica.backend.DTOs;

import com.pratica.backend.model.Status;
import com.pratica.backend.model.Task;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record TaskRequestDTO(
        @NotBlank(message = "A descrição é obrigatória e não pode estar em branco")
        String description,
        Status status,
        @NotNull(message = "A data limite é obrigatória")
        @FutureOrPresent(message = "A data limite não pode ser no passado")
        LocalDate dateLimit)
{

        public Task toEntity() {
            Task task = new Task();

            task.setDescription(this.description());
            task.setStatus(this.status());
            task.setDateLimit(this.dateLimit());
            return task;
        }
}