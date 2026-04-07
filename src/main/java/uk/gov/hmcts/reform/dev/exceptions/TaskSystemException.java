package uk.gov.hmcts.reform.dev.exceptions;

public class TaskSystemException extends RuntimeException{

    private String message;

    public TaskSystemException(String message)
    {
        super(message);
        this.message = message;
    }

}
