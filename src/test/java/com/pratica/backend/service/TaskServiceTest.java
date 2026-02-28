package com.pratica.backend.service;

import com.pratica.backend.DTOs.TaskRequestDTO;
import com.pratica.backend.DTOs.TaskResponseDTO;
import com.pratica.backend.model.Status;
import com.pratica.backend.model.Task;
import com.pratica.backend.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Tell JUnit to connect the mokito to this class.
@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

    // Creates a mock database.
    @Mock
    private TaskRepository taskRepository;

    //Take the real service and inject a mock into it.
    @InjectMocks
    private TaskService taskService;

    @Test
    void deveLancarErroAoBuscarIdQueNaoExiste() {
        // Arrange(preparar): Tell the mock that if they ask for x, return empty.
        when(taskRepository.findById(99L)).thenReturn(Optional.empty());

        // Act(executar):checks if the service will throw the correct exception.
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            taskService.findById(99L);
        });
        //Assert(verificar)
        assertEquals("Task not found with ID: 99", exception.getMessage());

        // Mokito verification ensures that the mock was called exactly once.
        verify(taskRepository, times(1)).findById(99L);
    }

    @Test
    void deveLancarErroAoSalvarTarefaSemDescricao() {
        // Arrange: Creates an invalid DTO with an empty description.
        TaskRequestDTO dtoInvalido = new TaskRequestDTO("   ", Status.PENDENTE, LocalDate.now());

        // Act : the service has to block
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            taskService.save(dtoInvalido);
        });
        //Assert
        assertEquals("The description is required", exception.getMessage());

        // It ensures that the service blocked the operation before attempting to save to the database.
        verify(taskRepository, never()).save(any(Task.class));
    }

    @Test
    void deveSalvarTarefaComSucessoEDefinirStatusPendente() {
        TaskRequestDTO dtoValido = new TaskRequestDTO("Aprender Mockito", null, LocalDate.now());

        //fictitious task that the bank will return after saving.
        Task tarefaRetornoBanco = new Task();
        tarefaRetornoBanco.setId(1L);
        tarefaRetornoBanco.setDescription("Aprender Mockito");
        tarefaRetornoBanco.setStatus(Status.PENDENTE); // The service should have filled this in.
        tarefaRetornoBanco.setDateLimit(dtoValido.dateLimit());

        when(taskRepository.save(any(Task.class))).thenReturn(tarefaRetornoBanco);

        TaskResponseDTO response = taskService.save(dtoValido);

        assertNotNull(response.id());
        assertEquals("Aprender Mockito", response.description());
        assertEquals(Status.PENDENTE, response.status(), "A Service deveria ter definido o status como PENDENTE");

        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @Test
    void deveRetornarListaDeTaskResponseDTOAoBuscarTodasAsTarefas() {
        Task tarefa1 = new Task();
        tarefa1.setId(1L);
        tarefa1.setDescription("Tarefa 1");
        tarefa1.setStatus(Status.PENDENTE);
        tarefa1.setDateLimit(LocalDate.now());

        Task tarefa2 = new Task();
        tarefa2.setId(2L);
        tarefa2.setDescription("Tarefa 2");
        tarefa2.setStatus(Status.CONCLUIDA);
        tarefa2.setDateLimit(LocalDate.now().plusDays(5));

        when(taskRepository.findAll()).thenReturn(java.util.List.of(tarefa1, tarefa2));

        java.util.List<TaskResponseDTO> resultado = taskService.findAll();

        assertNotNull(resultado, "A lista não deveria ser nula");
        assertEquals(2, resultado.size(), "Deveria retornar exatamente 2 DTOs");

        // We checked if the conversion (Entity -> DTO) of the first item was successful.
        assertEquals(1L, resultado.get(0).id());
        assertEquals("Tarefa 1", resultado.get(0).description());
        assertEquals(Status.PENDENTE, resultado.get(0).status());

        verify(taskRepository, times(1)).findAll();
    }
}