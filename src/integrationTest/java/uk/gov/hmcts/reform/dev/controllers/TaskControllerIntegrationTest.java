package uk.gov.hmcts.reform.dev.controllers;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.OrderBy;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import uk.gov.hmcts.reform.dev.enums.TaskStatus;
import uk.gov.hmcts.reform.dev.models.DTSTask;
import uk.gov.hmcts.reform.dev.repositories.IDTSTaskRepository;
import uk.gov.hmcts.reform.dev.requests.CreateNewCaseRequest;
import uk.gov.hmcts.reform.dev.requests.UpdateTaskRequest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TaskControllerIntegrationTest
{
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IDTSTaskRepository idtsTaskRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Test
    @Order(1)
    void whenValidRequest_thenReturns201ForSuccessAnd409ForDuplicates() throws Exception {
        objectMapper.registerModule(new JavaTimeModule());
        //Create a random code to enable the titles of the tasks be unique
        String randomTitleCode = RandomStringUtils.randomAlphanumeric(6);

        CreateNewCaseRequest request = new CreateNewCaseRequest();
        request.setTaskStatus(TaskStatus.ACTIVE_TRIAL.getCode());
        request.setTitle("Test Case - " + randomTitleCode);
        request.setDescription("Test Case Description - " + randomTitleCode);
        request.setDueDateTime(LocalDateTime.now().plusDays(30));

        //Send a post request to create a new task
        //- Check to confirm that the HTTP Status code is 201 indicating the task was created successfully
        //- Check to confirm that the response field apiStatusCode equals 0 which also indicates success
        //- Check to confirm that the id field of the response field data is numeric
        //- Check to confirm that the title field of the response field data matches the title field of the request body
        mockMvc.perform(post("/api/v1/case-worker-tasks/create-new-task")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.apiStatusCode").value(0))
            .andExpect(jsonPath("$.data.id").isNumber())
            .andExpect(jsonPath("$.data.title").value(request.getTitle()));

        //Send a post request to create a new task. This task should have the same title as the previous request.
        //-Check to confirm the HTTP Status code is 409 indicating that there is a duplicate
        //-Check to confirm that the errors field in the response indicates the message informing about duplicate
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
        //Create a random code to enable the titles of the tasks be unique
        String randomTitleCode = RandomStringUtils.randomAlphanumeric(6);

        //New request body that does not contain any of the fields. This request body is empty.
        CreateNewCaseRequest request = new CreateNewCaseRequest();

        //Send post request to create a new task posting the empty request body.
        //-Check to confirm that the HTTP response code is 400 (Bad request).
        //-Check to confirm that the apiStatusCode flag indicates that this operation failed.
        //-Check to confirm that the fields required are validated and appropriate validation message is returned.
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
        DTSTask dtsTask = idtsTaskRepository.findById(2L).stream().findFirst().orElse(null);
        TaskStatus taskStatusCurrent = dtsTask.getTaskStatus();

        //Create a random code to enable the titles of the tasks be unique
        String randomTitleCode = RandomStringUtils.randomAlphanumeric(6);

        //Update a task whose id equals 2
        UpdateTaskRequest request = new UpdateTaskRequest();
        request.setTaskStatus(TaskStatus.ACTIVE_TRIAL.getCode());
        request.setTaskId(2L);
        request.setTitle("Test Case - " + randomTitleCode);
        request.setDescription("Test Case Description - " + randomTitleCode);
        request.setDueDateTime(LocalDateTime.now().plusDays(30));

        //Send post request updating the task
        //-Check to confrm that the HTTP status code equals 200 indicating it was successful
        //-Check to confirm that the apiStatusCode field equals 0 which also indicates successful update.
        //-Check that the title of the task returned equals to the title set in the request.
        mockMvc.perform(post("/api/v1/case-worker-tasks/update-task")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.apiStatusCode").value(0))
            .andExpect(jsonPath("$.data.id").isNumber())
            .andExpect(jsonPath("$.data.title").value(request.getTitle()));


        //Revert task back to the previous state
        dtsTask.setTaskStatus(taskStatusCurrent);
        dtsTask.setTitle(dtsTask.getTitle());
        dtsTask.setDescription(dtsTask.getDescription());
        dtsTask.setDueDateTime(dtsTask.getDueDateTime());
        idtsTaskRepository.save(dtsTask);

    }


    @Test
    @Order(4)
    void whenDuplicateUpdateRequest_thenReturns409() throws Exception {
        objectMapper.registerModule(new JavaTimeModule());
        //Create a random code to enable the titles of the tasks be unique
        String randomTitleCode = RandomStringUtils.randomAlphanumeric(6);

        //Get task matching the ID 2. We will try to update the the title of the task we want to update to be the same as this task fetched so that there can be duplicates.
        Optional<DTSTask> dtsTaskOptional = idtsTaskRepository.findById(2L);
        DTSTask dtsTask = dtsTaskOptional.stream().findFirst().orElse(null);

        //Set the request containing the data to update the task whose ID equals 3.
        //We set the title of this task to be the title of the task whose ID equals 2.
        UpdateTaskRequest request = new UpdateTaskRequest();
        request.setTaskStatus(TaskStatus.ACTIVE_TRIAL.getCode());
        request.setTaskId(3L);
        request.setTitle(dtsTask.getTitle());
        request.setDescription(dtsTask.getDescription());
        request.setDueDateTime(LocalDateTime.now().plusDays(30));

        //Send Post request to the endpoint for updating tasks.
        //-Check to confirm that a HTTP response code indicating a conflict is returned
        //-Check to confirm that the apiStatusCode field equals 1 indicating that the update was not successful.
        //-Checjk to confirm that the appropriate error message is returned informing that there already exists a task with the title.
        mockMvc.perform(post("/api/v1/case-worker-tasks/update-task")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.apiStatusCode").value(1))
            .andExpect(jsonPath("$.errors").exists())
            .andExpect(jsonPath("$.errors.error").value("There already exists a task with that title. Please provide another title for this case"))
            .andExpect(jsonPath("$.data").isEmpty());

    }


    @Test
    @Order(5)
    void whenValidGetTaskById_thenReturns200() throws Exception {
        objectMapper.registerModule(new JavaTimeModule());

        //Send GET requst to get task whose ID equals 2
        //-Check to confirm that the apiStatusCode equals 0 indicating that the operation was successful.
        //-Check to conform that no errors are returned
        //-Check to confirm that the details of the task are returned in the data field.
        mockMvc.perform(get("/api/v1/case-worker-tasks/get-task-by-id/2")
                            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.apiStatusCode").value(0))
            .andExpect(jsonPath("$.errors").isEmpty())
            .andExpect(jsonPath("$.data").isMap());

    }


    @Test
    @Order(6)
    void whenGetNotExistingTaskById_thenReturns404() throws Exception {
        objectMapper.registerModule(new JavaTimeModule());

        //Fetch task whose ID equals 40000. We know this does not exist in the task table.
        //-Check that the apiStatusCode equals 1 indicating it was not successful.
        //-Chek that the appropriate error is returned indicating that there are no tasks matching the ID 40000
        mockMvc.perform(get("/api/v1/case-worker-tasks/get-task-by-id/40000")
                            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.apiStatusCode").value(1))
            .andExpect(jsonPath("$.errors").isNotEmpty())
            .andExpect(jsonPath("$.errors.error").value("There are no tasks matching the identifier."))
            .andExpect(jsonPath("$.data").isEmpty());

    }


    @Test
    @Order(7)
    void whenValidGetAllTasks_thenReturns200() throws Exception {
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc.perform(get("/api/v1/case-worker-tasks/get-all-tasks")
                            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.apiStatusCode").value(0))
            .andExpect(jsonPath("$.errors").isEmpty())
            .andExpect(jsonPath("$.data").isArray());

    }


    @Test
    @Order(8)
    void whenDeleteNonExistingTask_thenReturns404() throws Exception {
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc.perform(get("/api/v1/case-worker-tasks/delete-task/40000")
                            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.apiStatusCode").value(1))
            .andExpect(jsonPath("$.errors").isNotEmpty())
            .andExpect(jsonPath("$.errors.error").value("There are no tasks matching the identifier. To delete a task, a valid identifier matching an existing task must be provided."))
            .andExpect(jsonPath("$.data").isEmpty());

    }


    @Test
    @Order(9)
    void whenValidDeleteTask_thenReturns200() throws Exception {
        objectMapper.registerModule(new JavaTimeModule());
        mockMvc.perform(get("/api/v1/case-worker-tasks/delete-task/7")
                            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.apiStatusCode").value(0))
            .andExpect(jsonPath("$.errors").isEmpty())
            .andExpect(jsonPath("$.data").isEmpty())
            .andExpect(jsonPath("$.apiMessage").value("Task deleted successfully."));

    }

}
