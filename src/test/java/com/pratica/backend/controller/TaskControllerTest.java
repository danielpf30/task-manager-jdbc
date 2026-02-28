package com.pratica.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pratica.backend.DTOs.TaskRequestDTO;
import com.pratica.backend.DTOs.TaskResponseDTO;
import com.pratica.backend.model.Status;
import com.pratica.backend.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDate;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//It projects only the specified real class.
@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc; //"Postman" invisible

    @Autowired
    private ObjectMapper objectMapper; // Java to JSON translator

    @MockBean
    private TaskService taskService; //The Service stunt double

    @Test
    void deveRetornarStatus201AoCriarTarefa() throws Exception {
        // Arrange (Preparar)
        TaskRequestDTO requestDTO = new TaskRequestDTO("Aprender MockMvc", Status.PENDENTE, LocalDate.now());
        TaskResponseDTO responseDTO = new TaskResponseDTO(1L, "Aprender MockMvc", Status.PENDENTE, LocalDate.now());

        // Teach the stunt performer: When the Controller calls save(), return the responseDTO.
        when(taskService.save(any(TaskRequestDTO.class))).thenReturn(responseDTO);

        // Act & Assert (Executar e Verificar juntos)
        mockMvc.perform(post("/tasks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))

                // JUnit checks integrated with MockMvcMockMvc
                .andExpect(status().isCreated()) // Check if Status 201 has returned.
                .andExpect(jsonPath("$.id").value(1L)) //Inspect the response JSON to see if the ID is 1.
                .andExpect(jsonPath("$.description").value("Aprender MockMvc"));
    }

    @Test
    void deveRetornarStatus200AoBuscarPorId() throws Exception {
        // Arrange
        TaskResponseDTO responseDTO = new TaskResponseDTO(5L, "Tarefa 5", Status.CONCLUIDA, null);
        when(taskService.findById(5L)).thenReturn(responseDTO);

        // Act e Assert
        mockMvc.perform(get("/tasks/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.status").value("Concluída"));
    }

    @Test
    void deveRetornarStatus200AoAtualizarTarefa() throws Exception {
        Long idAlvo = 1L;
        // Simulando que o usuário quer mudar o status para Em Progresso e atualizar o texto
        TaskRequestDTO requestDTO = new TaskRequestDTO("Aprender testes de Update", Status.EM_PROGRESSO, LocalDate.now());
        TaskResponseDTO responseDTO = new TaskResponseDTO(idAlvo, "Aprender testes de Update", Status.EM_PROGRESSO, LocalDate.now());

        // Roteiro do dublê: Quando chamarem o update com o ID 1 e qualquer DTO, devolva o responseDTO
        when(taskService.update(eq(idAlvo), any(TaskRequestDTO.class))).thenReturn(responseDTO);

        // 2 e 3. Act & Assert (Executar e Verificar)
        mockMvc.perform((put("/tasks/{id}", idAlvo) // Mandamos o ID na URL (PathVariable)
                .contentType(MediaType.APPLICATION_JSON) // Avisamos que tem JSON
                .content(objectMapper.writeValueAsString(requestDTO)))) // Mandamos o DTO no corpo (RequestBody)
                // Verificações
                .andExpect(status().isOk()) // Esperamos Status 200 (OK)
                .andExpect(jsonPath("$.id").value(idAlvo))
                .andExpect(jsonPath("$.description").value("Aprender testes de Update"))
                .andExpect(jsonPath("$.status").value("Em Progresso")); // Olha a sua mágica do @JsonValue agindo aqui!
    }

}