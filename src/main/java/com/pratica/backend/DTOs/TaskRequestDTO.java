package com.pratica.backend.DTOs;

import com.pratica.backend.model.Status;
import com.pratica.backend.model.Task;
import java.time.LocalDate;

public record TaskRequestDTO(String description, Status status, LocalDate dateLimit) {

    public Task toEntity() {
        Task task = new Task();

        task.setDescription(this.description());
        task.setStatus(this.status());
        task.setDateLimit(this.dateLimit());
        return task;
    }
}