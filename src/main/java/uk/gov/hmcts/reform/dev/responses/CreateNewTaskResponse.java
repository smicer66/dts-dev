package uk.gov.hmcts.reform.dev.responses;


import lombok.Getter;
import lombok.Setter;
import uk.gov.hmcts.reform.dev.models.DTSTask;

@Getter
@Setter
public class CreateNewTaskResponse extends TaskResponse{

    private DTSTask createdTask;
}
