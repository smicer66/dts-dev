package uk.gov.hmcts.reform.dev.services;


import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.dev.models.DTSTask;
import uk.gov.hmcts.reform.dev.requests.CreateNewCaseRequest;
import uk.gov.hmcts.reform.dev.requests.UpdateTaskRequest;

import java.util.Collection;


@Service
public interface TaskService {

    DTSTask createNewTask(CreateNewCaseRequest createNewCaseRequest);
    DTSTask getTaskById(Long id);
    Collection<DTSTask> getAllTasks();
    DTSTask updateTask(UpdateTaskRequest updateTaskRequest);
    void deleteTask(Long id);
}
