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
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<TaskResponse> handleValidationErrors(
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
    public ResponseEntity<TaskResponse> handleBusinessError(
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
    public ResponseEntity<TaskResponse> handleGenericError(Exception ex) {

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

}

