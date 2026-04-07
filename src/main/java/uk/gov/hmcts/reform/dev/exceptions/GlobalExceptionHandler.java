package uk.gov.hmcts.reform.dev.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import uk.gov.hmcts.reform.dev.responses.TaskResponse;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
/*
Handles all exceptions in the system to present a custom response.
For the different exceptions, an appropriate HTTP status code is returned as response.
 */
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<TaskResponse> handleValidationError(
        MethodArgumentNotValidException ex) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                                                           errors.put(error.getField(), error.getDefaultMessage())
        );

        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setApiStatusCode(1);
        taskResponse.setApiMessage("System error encourntered");
        taskResponse.setErrors(errors);
        return ResponseEntity.badRequest().body(taskResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<TaskResponse> handleIllegalArgumentException(
        IllegalArgumentException ex) {

        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setApiStatusCode(1);
        taskResponse.setApiMessage("System error encountered");
        taskResponse.setErrors(errors);
        return ResponseEntity
            .badRequest()
            .body(taskResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<TaskResponse> handleGenericExceptionError(Exception ex) {

        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setApiStatusCode(1);
        taskResponse.setApiMessage("System error encountered");
        taskResponse.setErrors(errors);
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(taskResponse);
    }



    @ExceptionHandler(java.time.format.DateTimeParseException.class)
    //This is for handling the parsing of datetime exception
    public ResponseEntity<TaskResponse> handleMessageNotReadableError(Exception ex) {

        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setApiStatusCode(1);
        taskResponse.setApiMessage("System error encountered1");
        taskResponse.setErrors(errors);
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(taskResponse);
    }


    @ExceptionHandler(TaskExistException.class)
    //This is for exceptions thrown where there is a duplicate about to be created but we dont want the duplicate created.
    public ResponseEntity<TaskResponse> handleTaskExist(Exception ex) {

        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setApiStatusCode(1);
        taskResponse.setApiMessage("A similar task already exists.");
        taskResponse.setErrors(errors);
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .body(taskResponse);
    }


    @ExceptionHandler(TaskNotExistException.class)
    //This is an exception thrown then an id of a task not existing is passed to an operation.
    public ResponseEntity<TaskResponse> handleTaskNotExist(Exception ex) {

        Map<String, String> errors = new HashMap<>();
        errors.put("error", ex.getMessage());
        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setApiStatusCode(1);
        taskResponse.setApiMessage("No task matching the identifier currently exist.");
        taskResponse.setErrors(errors);
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(taskResponse);
    }

}

