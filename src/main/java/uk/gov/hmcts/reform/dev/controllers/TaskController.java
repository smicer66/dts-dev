package uk.gov.hmcts.reform.dev.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.reform.dev.models.DTSTask;
import uk.gov.hmcts.reform.dev.models.ExampleCase;
import uk.gov.hmcts.reform.dev.requests.CreateNewTaskRequest;
import uk.gov.hmcts.reform.dev.requests.UpdateTaskRequest;
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
    /*
    End point that creates the tasks.
    Method that handles this operation validates the request before processing it.
    Sends response indicating success or failure of the operation.
    If failed, an exception is thrown which is gandled by the Generic exception handler.
     */
    public ResponseEntity<TaskResponse> createNewCase(@Valid @RequestBody CreateNewTaskRequest createNewCaseRequest)
    {
        DTSTask dtsTask = this.taskService.createNewTask(createNewCaseRequest);

        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setData(dtsTask);
        taskResponse.setApiStatusCode(0);
        taskResponse.setApiMessage("New task created successfully.");

        return ResponseEntity.status(HttpStatus.CREATED).body(taskResponse);


    }

    @GetMapping(value = "/get-task-by-id/{id}", produces = "application/json")
    /*
    End point that fetches tasks by iD.
    Method that handles this operation requires an ID to be provided in the url.
    Sends response indicating success or failure of the operation.
    If failed, an exception is thrown which is gandled by the Generic exception handler.
    The task details fetched are in the data field of the response.
     */
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
    /*
    End point that fetches all tasks.
    Method that handles this operation fetches all the tasks in the table irrespective of if the deletedAt field is not null.
    Sends response indicating success or failure of the operation.
    If failed, an exception is thrown which is gandled by the Generic exception handler.
    The task details fetched are in the data field of the response.
     */
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
    /*
    End point that updates a task.
    Method that handles this operation validates the request which requires an ID of the task to be updated.
    Sends response indicating success or failure of the operation.
    If failed, an exception is thrown which is handled by the generic exception handler.
    The task details updated are in the data field of the response.
     */
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
    /*
    End point that deletes tasks by iD.
    Method that handles this operation requires an ID to be provided in the url.
    Sends response indicating success or failure of the operation.
    If failed, an exception is thrown which is handled by the generic exception handler.
     */
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
