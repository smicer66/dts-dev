package uk.gov.hmcts.reform.dev.responses;

import lombok.Getter;
import lombok.Setter;
import uk.gov.hmcts.reform.dev.models.DTSTask;

import java.util.Map;


@Getter
@Setter
public class TaskResponse {
    private String apiMessage;
    private int apiStatusCode;
    private Map<String, String> errors;
    private Object data;
}
