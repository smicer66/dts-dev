package uk.gov.hmcts.reform.dev.requests;



import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import uk.gov.hmcts.reform.dev.annotations.ValidTaskStatusCode;
import uk.gov.hmcts.reform.dev.enums.TaskStatus;

import java.time.LocalDateTime;


@Getter
@Setter
public class CreateNewCaseRequest {

    @NotBlank(message = "Provide the title of the case.")
    @Size(min = 3, max=255, message = "The title of the case must be of length between 3 and 255 characters.")
    private String title;

    @Size(max=500, message = "The description of the case must not exceed 500 characters in length.")
    private String description;

    @NotNull(message = "Provide the due data of the case.")
    @FutureOrPresent(message = "The due date must be in the future or the current date.")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dueDateTime;

    @NotNull(message = "Specify the status of the task.")
    @ValidTaskStatusCode(enumClass = TaskStatus.class, message = "Provide a valid task status code matching the possible valid status codes.")
    private String taskStatus;
}
