package com.pratica.backend.service;

import com.pratica.backend.DTOs.TaskPatchDTO;
import com.pratica.backend.DTOs.TaskRequestDTO;
import com.pratica.backend.DTOs.TaskResponseDTO;
import com.pratica.backend.model.Status;
import com.pratica.backend.model.Task;
import com.pratica.backend.repository.TaskRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository){
        this.taskRepository = taskRepository;
    }

    // internal helper method
    // He seeks the pure Entity.
    private Task getTaskEntityById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Task not found with ID: " + id));
    }

    public TaskResponseDTO findById(Long id){
        Task task = getTaskEntityById(id);
        return TaskResponseDTO.fromEntity(task);
    }

    public TaskResponseDTO save(TaskRequestDTO dto) {
        Task task = dto.toEntity();

        if (task.getDescription() == null || task.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("The description is required");
        }

        if (task.getStatus() == null) {
            task.setStatus(Status.PENDENTE);
        }

        Task savedTask = taskRepository.save(task);

        return TaskResponseDTO.fromEntity(savedTask);
    }

    public TaskResponseDTO update(Long id, TaskRequestDTO dto) {
        Task taskExistente = getTaskEntityById(id);
        Task taskToUpdate = dto.toEntity();
        taskToUpdate.setId(id); //joins the ID with the request

        if (dto.status() == null) {
            taskToUpdate.setStatus(taskExistente.getStatus());
        }
        Task updatedTask = taskRepository.update(taskToUpdate);

        return TaskResponseDTO.fromEntity(updatedTask);
    }

    public TaskResponseDTO updateParcial(Long id, TaskPatchDTO dto) {
        Task taskExistente = getTaskEntityById(id);

        if (dto.description() != null) {
            taskExistente.setDescription(dto.description());
        }

        if (dto.status() != null) {
            taskExistente.setStatus(dto.status());
        }

        if (dto.dateLimit() != null) {
            taskExistente.setDateLimit(dto.dateLimit());
        }

        Task updatedTask = taskRepository.update(taskExistente);

        return TaskResponseDTO.fromEntity(updatedTask);
    }

    public void delete(Long id) {
        getTaskEntityById(id);
        taskRepository.delete(id);
    }

    // Retrieves tasks from the database, converts each Entity (Task) to a DTO (TaskResponseDTO)
    // and returns everything packaged in a List.

    public List<TaskResponseDTO> findAll() {
        return taskRepository.findAll().stream()
                .map(TaskResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<TaskResponseDTO> findByStatus(String status) {
        return taskRepository.findByStatus(Status.valueOf(status.toUpperCase())).stream()
                .map(TaskResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public List<TaskResponseDTO> findByDateLimit(java.time.LocalDate dateLimit) {
        return taskRepository.findByDateLimit(dateLimit).stream()
                .map(TaskResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }
}
