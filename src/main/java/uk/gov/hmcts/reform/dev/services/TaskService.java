package uk.gov.hmcts.reform.dev.services;


import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.dev.models.DTSTask;
import uk.gov.hmcts.reform.dev.requests.CreateNewTaskRequest;
import uk.gov.hmcts.reform.dev.requests.UpdateTaskRequest;

import java.util.Collection;


@Service
/*
Contract that defiines the operations possible in the system.
 */
public interface TaskService {

    //Operation to create a new task

    DTSTask createNewTask(CreateNewTaskRequest createNewCaseRequest);

    //Operation to find task by ID
    DTSTask getTaskById(Long id);

    //Operation to Find all tasks
    Collection<DTSTask> getAllTasks();

    //Operation to Update a task
    DTSTask updateTask(UpdateTaskRequest updateTaskRequest);

    //Operation to delete a task
    void deleteTask(Long id);
}
