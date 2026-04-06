package uk.gov.hmcts.reform.dev.exceptions;

public class TaskExistException extends RuntimeException{

    private String message;

    public TaskExistException(String message)
    {
        super(message);
        this.message = message;
    }

}
