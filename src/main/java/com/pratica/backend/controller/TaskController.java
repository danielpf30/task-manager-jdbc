package com.pratica.backend.controller;

import com.pratica.backend.DTOs.TaskRequestDTO;
import com.pratica.backend.DTOs.TaskResponseDTO;
import com.pratica.backend.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping           //@RequestBody get the JSON
    public ResponseEntity<TaskResponseDTO> save(@Valid @RequestBody TaskRequestDTO task){
        TaskResponseDTO taskResponse = taskService.save(task);
        // Return Status 201 (Created)
        return ResponseEntity.status(HttpStatus.CREATED).body(taskResponse);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> update(@PathVariable Long id, @Valid @RequestBody TaskRequestDTO task){
        // @PathVariable retrieve the URL ID
        return ResponseEntity.ok(taskService.update(id, task));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TaskResponseDTO> updateParcial(@PathVariable Long id, @RequestBody TaskRequestDTO dto) {
        // Sem o @Valid, o Spring deixa entrar um JSON só com o "status", sem reclamar da data ou descrição nulas!
        return ResponseEntity.ok(taskService.updateParcial(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        taskService.delete(id);
        // Return Status 204
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<TaskResponseDTO>> getTasksAll() {
        return ResponseEntity.ok(taskService.findAll());
    }

    @GetMapping("/{id}")
    public TaskResponseDTO getTasksById(@PathVariable long id){
        return ResponseEntity.ok(taskService.findById(id)).getBody();
    }

    @GetMapping("/status")
    public ResponseEntity<List<TaskResponseDTO>> getTasksByStatus(@RequestParam String status) {
        // @RequestParam This indicates that the variable comes after the "?" in the URL.
        return ResponseEntity.ok(taskService.findByStatus(status));
    }

    @GetMapping("/date")
    public ResponseEntity<List<TaskResponseDTO>> getTasksByDateLimit(@RequestParam LocalDate dateLimit) {
        return ResponseEntity.ok(taskService.findByDateLimit(dateLimit));
    }
}
