package uk.gov.hmcts.reform.dev.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.reform.dev.models.DTSTask;
import uk.gov.hmcts.reform.dev.models.ExampleCase;
import uk.gov.hmcts.reform.dev.requests.CreateNewCaseRequest;
import uk.gov.hmcts.reform.dev.requests.UpdateTaskRequest;
import uk.gov.hmcts.reform.dev.responses.CreateNewTaskResponse;
import uk.gov.hmcts.reform.dev.responses.TaskResponse;
//import uk.gov.hmcts.reform.dev.services.TaskService;
import uk.gov.hmcts.reform.dev.services.TaskServiceImpl;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@Validated
@RequestMapping("/api/v1/case-worker-tasks")
public class TaskController {


    @Autowired
    private TaskServiceImpl taskService;

    @GetMapping(value = "/get-example-case", produces = "application/json")
    public ResponseEntity<ExampleCase> getExampleCase() {
        return ok(new ExampleCase(1, "ABC12345", "Case Title",
                                  "Case Description", "Case Status", LocalDateTime.now()
        ));
    }

    @PostMapping(value = "/create-new-task", produces = "application/json")
    public ResponseEntity<TaskResponse> createNewCase(@Valid @RequestBody CreateNewCaseRequest createNewCaseRequest)
    {
        DTSTask dtsTask = this.taskService.createNewTask(createNewCaseRequest);

        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setData(dtsTask);
        taskResponse.setApiStatusCode(0);
        taskResponse.setApiMessage("New task created successfully.");

        return ResponseEntity.status(HttpStatus.CREATED).body(taskResponse);


    }

    @GetMapping(value = "/get-task-by-id/{id}", produces = "application/json")
    public ResponseEntity<TaskResponse> createNewCase(@Valid @PathVariable(required = true) Long id)
    {
        DTSTask dtsTask = this.taskService.getTaskById(id);

        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setApiStatusCode(0);
        taskResponse.setApiMessage("Task found.");
        taskResponse.setData(dtsTask);
        return ResponseEntity.ok().body(taskResponse);
    }

    @GetMapping(value = "/get-all-tasks", produces = "application/json")
    public ResponseEntity<TaskResponse> getAllTasks()
    {
        Collection<DTSTask> dtsTaskCollection = this.taskService.getAllTasks();

        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setApiStatusCode(0);
        taskResponse.setApiMessage("Tasks found.");
        taskResponse.setData(dtsTaskCollection);
        return ResponseEntity.ok().body(taskResponse);
    }

    @PostMapping(value = "/update-task", produces = "application/json")
    ResponseEntity<TaskResponse> updateTask(@Valid @RequestBody UpdateTaskRequest updateTaskRequest)
    {
        DTSTask dtsTask = this.taskService.updateTask(updateTaskRequest);

        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setData(dtsTask);
        taskResponse.setApiStatusCode(0);
        taskResponse.setApiMessage("Task updated successfully.");
        return ResponseEntity.ok().body(taskResponse);
    }

    @GetMapping(value = "/delete-task/{id}", produces = "application/json")
    public ResponseEntity<TaskResponse> deleteTask(@PathVariable(required = true) Long id)
    {
        this.taskService.deleteTask(id);


        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setData(null);
        taskResponse.setApiStatusCode(0);
        taskResponse.setApiMessage("Task deleted successfully.");

        return ResponseEntity.ok().body(taskResponse);
    }
}
