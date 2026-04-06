package uk.gov.hmcts.reform.dev.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.dev.enums.TaskStatus;
import uk.gov.hmcts.reform.dev.exceptions.TaskExistException;
import uk.gov.hmcts.reform.dev.models.DTSTask;
import uk.gov.hmcts.reform.dev.repositories.IDTSTaskRepository;
import uk.gov.hmcts.reform.dev.requests.CreateNewCaseRequest;
import uk.gov.hmcts.reform.dev.requests.UpdateTaskRequest;
import uk.gov.hmcts.reform.dev.responses.CreateNewTaskResponse;
import uk.gov.hmcts.reform.dev.responses.TaskResponse;

import java.util.Collection;
import java.util.Optional;

@Service
public class TaskService {

    @Autowired
    private IDTSTaskRepository idtsTaskRepository;
    public CreateNewTaskResponse createNewTask(CreateNewCaseRequest createNewCaseRequest)
    {
        DTSTask dtsTaskMatchByTitle = idtsTaskRepository.getDTSTaskByTitle(createNewCaseRequest.getTitle());
        if(dtsTaskMatchByTitle!=null)
        {
            throw new TaskExistException(String.format("There already exists a task with that title."));
        }

        DTSTask dtsTask = new DTSTask();
        dtsTask.setDescription(createNewCaseRequest.getTitle());
        dtsTask.setTaskStatus(TaskStatus.valueOf(createNewCaseRequest.getTaskStatus()));
        dtsTask.setDueDateTime(createNewCaseRequest.getDueDateTime());
        dtsTask.setTitle(createNewCaseRequest.getTitle());
        dtsTask = (DTSTask) idtsTaskRepository.save(dtsTask);

        CreateNewTaskResponse createNewTaskResponse = new CreateNewTaskResponse();
        createNewTaskResponse.setCreatedTask(dtsTask);
        createNewTaskResponse.setApiStatusCode(0);
        createNewTaskResponse.setApiMessage("New task created successfully.");
        return createNewTaskResponse;
    }


    public TaskResponse getTaskById(Long id)
    {
        Optional<DTSTask> dtsTaskOptional = (Optional<DTSTask>)idtsTaskRepository.findById(id);
        DTSTask dtsTask = dtsTaskOptional.stream().findFirst().orElseThrow(() ->  new TaskExistException(String.format("There are no tasks matching the identifier.")));


        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setApiStatusCode(0);
        taskResponse.setApiMessage("Task found.");
        taskResponse.setData(dtsTask);
        return taskResponse;
    }


    public TaskResponse getAllTasks()
    {
        Collection<DTSTask> dtsTaskCollection = (Collection<DTSTask>) idtsTaskRepository.findAll();
        if(dtsTaskCollection.isEmpty())
        {
            throw new TaskExistException(String.format("There are no tasks currently created on the system."));
        }

        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setApiStatusCode(0);
        taskResponse.setApiMessage("Tasks found.");
        taskResponse.setData(dtsTaskCollection);
        return taskResponse;

    }


    public TaskResponse updateTask(UpdateTaskRequest updateTaskRequest)
    {
        DTSTask dtsTaskMatchByTitle = idtsTaskRepository.getDTSTaskByTitleForUpdate(
            updateTaskRequest.getTitle(),
            updateTaskRequest.getTaskId()
        );
        if(dtsTaskMatchByTitle!=null)
        {
            throw new TaskExistException(String.format("There already exists a task with that title. Please provide another title for this case"));
        }

        Optional<DTSTask> dtsTaskOptional = idtsTaskRepository.findById(updateTaskRequest.getTaskId());
        DTSTask dtsTask = dtsTaskOptional.stream().findFirst().orElseThrow(() ->  new TaskExistException(String.format("There are no tasks matching the identifier. To update a task, a valid identifier matching an existing task must be provided.")));
        dtsTask.setDescription(updateTaskRequest.getTitle());
        dtsTask.setTaskStatus(TaskStatus.valueOf(updateTaskRequest.getTaskStatus()));
        dtsTask.setDueDateTime(updateTaskRequest.getDueDateTime());
        dtsTask.setTitle(updateTaskRequest.getTitle());
        dtsTask = (DTSTask) idtsTaskRepository.save(dtsTask);

        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setData(dtsTask);
        taskResponse.setApiStatusCode(0);
        taskResponse.setApiMessage("Task updated successfully.");
        return taskResponse;
    }


    public TaskResponse deleteTask(Long id)
    {
        Optional<DTSTask> dtsTaskOptional = (Optional<DTSTask>)idtsTaskRepository.findById(id);
        DTSTask dtsTask = dtsTaskOptional.stream().findFirst().orElseThrow(() ->  new TaskExistException(String.format("There are no tasks matching the identifier. To delete a task, a valid identifier matching an existing task must be provided.")));
        idtsTaskRepository.delete(dtsTask);

        TaskResponse taskResponse = new TaskResponse();
        taskResponse.setData(null);
        taskResponse.setApiStatusCode(0);
        taskResponse.setApiMessage("Task deleted successfully.");
        return taskResponse;
    }
}
