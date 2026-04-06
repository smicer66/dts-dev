package uk.gov.hmcts.reform.dev.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.hmcts.reform.dev.enums.TaskStatus;
import uk.gov.hmcts.reform.dev.repositories.IDTSTaskRepository;
import uk.gov.hmcts.reform.dev.requests.CreateNewCaseRequest;
import uk.gov.hmcts.reform.dev.requests.UpdateTaskRequest;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class TaskControllerIntegrationTest
{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IDTSTaskRepository idtsTaskRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Test
    @Order(1)
    void whenValidRequest_thenReturns201AndTaskResponse() throws Exception {
        objectMapper.registerModule(new JavaTimeModule());
        String randomTitleCode = RandomStringUtils.randomAlphanumeric(6);

        CreateNewCaseRequest request = new CreateNewCaseRequest();
        request.setTaskStatus(TaskStatus.ACTIVE_TRIAL.getCode());
        request.setTitle("Test Case - " + randomTitleCode);
        request.setDescription("Test Case Description - " + randomTitleCode);
        request.setDueDateTime(LocalDateTime.now().plusDays(30));

        mockMvc.perform(post("/api/v1/case-worker-tasks/create-new-task")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.apiStatusCode").value(0))
            .andExpect(jsonPath("$.data.id").isNumber())
            .andExpect(jsonPath("$.data.title").value(request.getTitle()));

        mockMvc.perform(post("/api/v1/case-worker-tasks/create-new-task")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.apiStatusCode").value(1))
            .andExpect(jsonPath("$.errors.error").value("There already exists a task with that title."))
            .andExpect(jsonPath("$.data").doesNotExist());
    }



    @Test
    @Order(2)
    void whenRequestFieldsFailsValidation_thenReturns400AndTaskResponse() throws Exception {
        objectMapper.registerModule(new JavaTimeModule());
        String randomTitleCode = RandomStringUtils.randomAlphanumeric(6);

        CreateNewCaseRequest request = new CreateNewCaseRequest();



        mockMvc.perform(post("/api/v1/case-worker-tasks/create-new-task")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.apiStatusCode").value(1))
            .andExpect(jsonPath("$.errors").isMap())
            .andExpect(jsonPath("$.errors.dueDateTime").value("Provide the due data of the case."))
            .andExpect(jsonPath("$.errors.title").value("Provide the title of the case."))
            .andExpect(jsonPath("$.errors.taskStatus").value("Specify the status of the task."));

    }


    @Test
    @Order(3)
    void whenValidUpdateRequest_thenReturns200AndTaskResponse() throws Exception {
        objectMapper.registerModule(new JavaTimeModule());
        String randomTitleCode = RandomStringUtils.randomAlphanumeric(6);

        UpdateTaskRequest request = new UpdateTaskRequest();
        request.setTaskStatus(TaskStatus.ACTIVE_TRIAL.getCode());
        request.setTaskId(2L);
        request.setTitle("Test Case - " + randomTitleCode);
        request.setDescription("Test Case Description - " + randomTitleCode);
        request.setDueDateTime(LocalDateTime.now().plusDays(30));

        mockMvc.perform(post("/api/v1/case-worker-tasks/update-task")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.apiStatusCode").value(0))
            .andExpect(jsonPath("$.data.id").isNumber())
            .andExpect(jsonPath("$.data.title").value(request.getTitle()));

    }


    @Test
    @Order(4)
    void whenValidUpdateRequest_thenReturns200AndTaskResponse() throws Exception {
        objectMapper.registerModule(new JavaTimeModule());
        String randomTitleCode = RandomStringUtils.randomAlphanumeric(6);

        UpdateTaskRequest request = new UpdateTaskRequest();
        request.setTaskStatus(TaskStatus.ACTIVE_TRIAL.getCode());
        request.setTaskId(2L);
        request.setTitle("Test Case - " + randomTitleCode);
        request.setDescription("Test Case Description - " + randomTitleCode);
        request.setDueDateTime(LocalDateTime.now().plusDays(30));

        mockMvc.perform(post("/api/v1/case-worker-tasks/update-task")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.apiStatusCode").value(0))
            .andExpect(jsonPath("$.data.id").isNumber())
            .andExpect(jsonPath("$.data.title").value(request.getTitle()));

    }

}
