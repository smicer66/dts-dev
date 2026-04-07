package uk.gov.hmcts.reform.dev.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.dev.enums.TaskStatus;
import uk.gov.hmcts.reform.dev.exceptions.TaskExistException;
import uk.gov.hmcts.reform.dev.exceptions.TaskNotExistException;
import uk.gov.hmcts.reform.dev.models.DTSTask;
import uk.gov.hmcts.reform.dev.repositories.IDTSTaskRepository;
import uk.gov.hmcts.reform.dev.requests.CreateNewCaseRequest;
import uk.gov.hmcts.reform.dev.requests.UpdateTaskRequest;
import uk.gov.hmcts.reform.dev.responses.TaskResponse;

import java.util.Collection;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService{

    //@Autowired
    private final IDTSTaskRepository idtsTaskRepository;

    //@Autowired
    public TaskServiceImpl(IDTSTaskRepository idtsTaskRepository) {
        this.idtsTaskRepository = idtsTaskRepository;
    }

    @Override
    public DTSTask createNewTask(CreateNewCaseRequest createNewCaseRequest)
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
        return dtsTask;
    }


    @Override
    public DTSTask getTaskById(Long id)
    {
        Optional<DTSTask> dtsTaskOptional = (Optional<DTSTask>)idtsTaskRepository.findById(id);
        DTSTask dtsTask = dtsTaskOptional.stream().findFirst().orElseThrow(() ->  new TaskNotExistException(String.format("There are no tasks matching the identifier.")));

        return dtsTask;
    }


    @Override
    public Collection<DTSTask> getAllTasks()
    {
        Collection<DTSTask> dtsTaskCollection = (Collection<DTSTask>) idtsTaskRepository.findAll();
        if(dtsTaskCollection.isEmpty())
        {
            throw new TaskNotExistException(String.format("There are no tasks currently created on the system."));
        }

        return dtsTaskCollection;

    }


    @Override
    public DTSTask updateTask(UpdateTaskRequest updateTaskRequest)
    {
        Optional<DTSTask> dtsTaskOptional = idtsTaskRepository.findById(updateTaskRequest.getTaskId());
        DTSTask dtsTask = dtsTaskOptional.stream().findFirst().orElseThrow(() ->  new TaskNotExistException(String.format("There are no tasks matching the identifier. To update a task, a valid identifier matching an existing task must be provided.")));

        if(!dtsTask.getTitle().equalsIgnoreCase(updateTaskRequest.getTitle())) {
            DTSTask dtsTaskMatchByTitle = idtsTaskRepository.getDTSTaskByTitleForUpdate(
                updateTaskRequest.getTitle(),
                updateTaskRequest.getTaskId()
            );
            if (dtsTaskMatchByTitle != null) {
                throw new TaskExistException(String.format(
                    "There already exists a task with that title. Please provide another title for this case"));
            }
        }


        dtsTask.setDescription(updateTaskRequest.getTitle());
        dtsTask.setTaskStatus(TaskStatus.valueOf(updateTaskRequest.getTaskStatus()));
        dtsTask.setDueDateTime(updateTaskRequest.getDueDateTime());
        dtsTask.setTitle(updateTaskRequest.getTitle());
        dtsTask = (DTSTask) idtsTaskRepository.save(dtsTask);

        return dtsTask;
    }

    @Override
    public void deleteTask(Long id)
    {
        Optional<DTSTask> dtsTaskOptional = (Optional<DTSTask>)idtsTaskRepository.findById(id);
        DTSTask dtsTask = dtsTaskOptional.stream().findFirst().orElseThrow(() ->  new TaskNotExistException(String.format("There are no tasks matching the identifier. To delete a task, a valid identifier matching an existing task must be provided.")));
        idtsTaskRepository.delete(dtsTask);

    }
}
