package uk.gov.hmcts.reform.dev.exceptions;



public class TaskNotExistException extends RuntimeException{

    private String message;

    public TaskNotExistException(String message)
    {
        super(message);
        this.message = message;
    }

}
