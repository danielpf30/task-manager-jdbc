package com.pratica.backend.DTOs;

import com.pratica.backend.model.Status;
import com.pratica.backend.model.Task;
import java.time.LocalDate;

public record TaskResponseDTO(Long id, String description, Status status, LocalDate dateLimit) {

    public static TaskResponseDTO fromEntity(Task task) {
        return new TaskResponseDTO(
                task.getId(),
                task.getDescription(),
                task.getStatus(),
                task.getDateLimit()
        );
    }
}