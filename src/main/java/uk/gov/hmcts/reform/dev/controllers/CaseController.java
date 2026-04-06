package uk.gov.hmcts.reform.dev.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.reform.dev.models.DTSTask;
import uk.gov.hmcts.reform.dev.models.ExampleCase;
import uk.gov.hmcts.reform.dev.requests.CreateNewCaseRequest;
import uk.gov.hmcts.reform.dev.requests.UpdateTaskRequest;
import uk.gov.hmcts.reform.dev.responses.CreateNewTaskResponse;
import uk.gov.hmcts.reform.dev.responses.TaskResponse;
import uk.gov.hmcts.reform.dev.services.TaskService;

import java.time.LocalDateTime;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@Validated
@RequestMapping("/api/v1/case-worker-tasks")
public class CaseController {


    @Autowired
    private TaskService taskService;

    @GetMapping(value = "/get-example-case", produces = "application/json")
    public ResponseEntity<ExampleCase> getExampleCase() {
        return ok(new ExampleCase(1, "ABC12345", "Case Title",
                                  "Case Description", "Case Status", LocalDateTime.now()
        ));
    }

    @PostMapping(value = "/create-new-task", produces = "application/json")
    public ResponseEntity<CreateNewTaskResponse> createNewCase(@Valid @RequestBody CreateNewCaseRequest createNewCaseRequest)
    {
        CreateNewTaskResponse createNewTaskResponse = this.taskService.createNewTask(createNewCaseRequest);
        return ResponseEntity.ok().body(createNewTaskResponse);


    }

    @GetMapping(value = "/get-task-by-id/{id}", produces = "application/json")
    public ResponseEntity<TaskResponse> createNewCase(@Valid @PathVariable(required = true) Long id)
    {
        TaskResponse taskResponse = this.taskService.getTaskById(id);
        return ResponseEntity.ok().body(taskResponse);
    }

    @GetMapping(value = "/get-all-tasks", produces = "application/json")
    public ResponseEntity<TaskResponse> getAllTasks()
    {
        TaskResponse taskResponse = this.taskService.getAllTasks();
        return ResponseEntity.ok().body(taskResponse);
    }

    @PostMapping(value = "/update-task", produces = "application/json")
    public ResponseEntity<TaskResponse> updateTask(@Valid @RequestBody UpdateTaskRequest updateTaskRequest)
    {
        TaskResponse taskResponse = this.taskService.updateTask(updateTaskRequest);
        return ResponseEntity.ok().body(taskResponse);
    }

    @GetMapping(value = "/delete-case/{id}", produces = "application/json")
    public ResponseEntity<TaskResponse> deleteTask(@PathVariable(required = true) Long id)
    {
        TaskResponse taskResponse = this.taskService.deleteTask(id);
        return ResponseEntity.ok().body(taskResponse);
    }
}
