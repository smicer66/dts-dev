package uk.gov.hmcts.reform.dev.services;


import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uk.gov.hmcts.reform.dev.enums.TaskStatus;
import uk.gov.hmcts.reform.dev.exceptions.TaskExistException;
import uk.gov.hmcts.reform.dev.exceptions.TaskNotExistException;
import uk.gov.hmcts.reform.dev.exceptions.TaskSystemException;
import uk.gov.hmcts.reform.dev.models.DTSTask;
import uk.gov.hmcts.reform.dev.repositories.IDTSTaskRepository;
import uk.gov.hmcts.reform.dev.requests.CreateNewTaskRequest;
import uk.gov.hmcts.reform.dev.requests.UpdateTaskRequest;

import java.util.Collection;
import java.util.Optional;
import org.slf4j.Logger;

@Service
/*
A concrete implementation of the TaskService contraact.
EAch operation in the TaskService contract is implemented in this class.
 */
public class TaskServiceImpl implements TaskService{

    //@Autowired
    private final IDTSTaskRepository idtsTaskRepository;
    private static final Logger logger = LoggerFactory.getLogger(TaskServiceImpl.class);

    public TaskServiceImpl(IDTSTaskRepository idtsTaskRepository) {
        this.idtsTaskRepository = idtsTaskRepository;
    }


    @Transactional
    public void truncateTable()
    {
        idtsTaskRepository.truncateTable();
    }

    @Override
    @Transactional
    /*
    Implementation of the operation for creating a new task.
     */
    public DTSTask createNewTask(CreateNewTaskRequest createNewCaseRequest)
    {
        DTSTask dtsTaskMatchByTitle = idtsTaskRepository.getDTSTaskByTitle(createNewCaseRequest.getTitle());

        //If there exists a task with the title, throw an exception informing about the existence of another task with the same title.
        if(dtsTaskMatchByTitle!=null)
        {
            throw new TaskExistException(String.format("There already exists a task with that title."));
        }

        DTSTask dtsTask = new DTSTask();
        dtsTask.setDescription(createNewCaseRequest.getTitle());
        dtsTask.setTaskStatus(TaskStatus.valueOf(createNewCaseRequest.getTaskStatus()));

        dtsTask.setDueDateTime(createNewCaseRequest.getDueDateTime());
        dtsTask.setTitle(createNewCaseRequest.getTitle());

        //Save new task to the database
        try {
            dtsTask = (DTSTask) idtsTaskRepository.save(dtsTask);
            return dtsTask;
        }
        catch(Exception ex)
        {
            throw new TaskSystemException(String.format("We could not create a new task with the title - %s", createNewCaseRequest.getTitle()));
        }
    }


    @Override
    /*
    Implementation of the operation for fetching a task using its ID
     */
    public DTSTask getTaskById(Long id)
    {

        Optional<DTSTask> dtsTaskOptional = (Optional<DTSTask>)idtsTaskRepository.findById(id);
        DTSTask dtsTask = dtsTaskOptional.stream().findFirst().orElseThrow(() ->  new TaskNotExistException(String.format("There are no tasks matching the identifier.")));

        return dtsTask;

    }


    @Override
    /*
    Implementation of the operation for fetching all tasks
     */
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
    /*
    Implementation of the operation for updating an existing task
     */
    public DTSTask updateTask(UpdateTaskRequest updateTaskRequest)
    {
        Optional<DTSTask> dtsTaskOptional = idtsTaskRepository.findById(updateTaskRequest.getTaskId());
        DTSTask dtsTask = dtsTaskOptional.stream().findFirst().orElseThrow(() ->  new TaskNotExistException(String.format("There are no tasks matching the identifier. To update a task, a valid identifier matching an existing task must be provided.")));

        //If the title of the task is different from the titlein the update request:
        if(!dtsTask.getTitle().equalsIgnoreCase(updateTaskRequest.getTitle()))
        {
            //Check for other tasks that have the title in the update request.
            DTSTask dtsTaskMatchByTitle = idtsTaskRepository.getDTSTaskByTitleForUpdate(
                updateTaskRequest.getTitle(),
                updateTaskRequest.getTaskId()
            );

            //If there are any tasks with that title,
            //throw an exception as we cant have duplicate tasks having the same title
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
    /*
    Implementation of the operation for deleting an existing task.
    Throw an exception if no task matches the ID provided.
     */
    public void deleteTask(Long id)
    {
        Optional<DTSTask> dtsTaskOptional = (Optional<DTSTask>)idtsTaskRepository.findById(id);
        DTSTask dtsTask = dtsTaskOptional.stream().findFirst().orElseThrow(() ->  new TaskNotExistException(String.format("There are no tasks matching the identifier. To delete a task, a valid identifier matching an existing task must be provided.")));
        idtsTaskRepository.delete(dtsTask);

    }
}
