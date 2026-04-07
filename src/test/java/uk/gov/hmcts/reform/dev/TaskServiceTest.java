package uk.gov.hmcts.reform.dev;

/*
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import uk.gov.hmcts.reform.dev.enums.TaskStatus;
import uk.gov.hmcts.reform.dev.models.DTSTask;
import uk.gov.hmcts.reform.dev.repositories.IDTSTaskRepository;
import uk.gov.hmcts.reform.dev.requests.CreateNewCaseRequest;
import uk.gov.hmcts.reform.dev.responses.TaskResponse;
import uk.gov.hmcts.reform.dev.services.TaskService;
import uk.gov.hmcts.reform.dev.services.TaskServiceImpl;

import java.time.LocalDateTime;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;*/

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import uk.gov.hmcts.reform.dev.enums.TaskStatus;
import uk.gov.hmcts.reform.dev.exceptions.TaskExistException;
import uk.gov.hmcts.reform.dev.exceptions.TaskNotExistException;
import uk.gov.hmcts.reform.dev.models.DTSTask;
import uk.gov.hmcts.reform.dev.repositories.IDTSTaskRepository;
import uk.gov.hmcts.reform.dev.requests.CreateNewTaskRequest;
import uk.gov.hmcts.reform.dev.requests.UpdateTaskRequest;
import uk.gov.hmcts.reform.dev.services.TaskServiceImpl;

import java.time.LocalDateTime;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TaskServiceTest {

    @Autowired
    private IDTSTaskRepository idtsTaskRepository;

    @Autowired
    private TaskServiceImpl taskService;

    private String randomTitleCode = RandomStringUtils.randomAlphanumeric(8);


    @BeforeEach
    void setup() {
        // Clear DB and preload test data
        taskService.truncateTable();

        idtsTaskRepository.save(new DTSTask("Title 01", "Title 01 Description", TaskStatus.CLOSED, LocalDateTime.now()));
        idtsTaskRepository.save(new DTSTask("Title 02", "Title 02 Description", TaskStatus.CLOSED, LocalDateTime.now()));
        idtsTaskRepository.save(new DTSTask("Title 03", "Title 03 Description", TaskStatus.CLOSED, LocalDateTime.now()));
        idtsTaskRepository.save(new DTSTask("Title 04", "Title 04 Description", TaskStatus.CLOSED, LocalDateTime.now()));
        idtsTaskRepository.save(new DTSTask("Title 05", "Title 05 Description", TaskStatus.CLOSED, LocalDateTime.now()));
        idtsTaskRepository.save(new DTSTask("Title 06", "Title 06 Description", TaskStatus.CLOSED, LocalDateTime.now()));
        idtsTaskRepository.save(new DTSTask("Title 07", "Title 07 Description", TaskStatus.CLOSED, LocalDateTime.now()));
        idtsTaskRepository.save(new DTSTask("Title 08", "Title 08 Description", TaskStatus.CLOSED, LocalDateTime.now()));
        idtsTaskRepository.save(new DTSTask("Title 09", "Title 09 Description", TaskStatus.CLOSED, LocalDateTime.now()));
        idtsTaskRepository.save(new DTSTask("Title 10", "Title 10 Description", TaskStatus.CLOSED, LocalDateTime.now()));
    }


    @Test
    public void createNewTaskValidRequestReturnsValidTask(){

        //Request for creating a new task
        CreateNewTaskRequest request = new CreateNewTaskRequest();
        request.setTaskStatus(TaskStatus.ACTIVE_TRIAL.getCode());
        request.setTitle("Test " + randomTitleCode);
        request.setDescription("Test Description - " + randomTitleCode);
        request.setDueDateTime(LocalDateTime.now().plusDays(30));


        DTSTask dtsTask = taskService.createNewTask(request);
        //Check to confirm the title of the new task created matches the title in the request
        assertEquals(dtsTask.getTitle(), "Test " + randomTitleCode);

        //Check that an id was auto-generated
        assertInstanceOf(Long.class, dtsTask.getId());

        //Check that an id is not null
        assertNotNull(dtsTask.getId());

    }

    @Test
    public void getTaskByIdSuccess(){
        //Call the service layer method that fetches tasks based on id
        DTSTask dtsTask = taskService.getTaskById(3L);

        //Check to confirm that it exists
        assertNotNull(dtsTask);

        //Check to confirm that the title equals the initial set value for the task
        assertEquals(dtsTask.getTitle(), "Title 03");

        //Check to confirm that the id matches the id used to fetch the task
        assertEquals(dtsTask.getId(), 3L);
    }

    @Test
    public void getTaskByIdFailDueToNoneExisting(){
        //Call the service layer method that fetches a task that does not exist. This should throw an exception
        TaskNotExistException tneex = assertThrows(
            TaskNotExistException.class,
            ()->taskService.getTaskById(1000L)
        );

        //Check to confirm that the exception message matches the expected message confirming that no tasks match the identifier passed to the service
        assertEquals(tneex.getMessage(), "There are no tasks matching the identifier.");
    }

    @Test
    public void getAllTasksSuccess(){
        //Fetch all tasks created
        Collection<DTSTask> dtsTaskList = taskService.getAllTasks();

        //Check to confirm that the list is not null
        assertNotNull(dtsTaskList);

        //Check to confirm that the size of the list is greater than zero
        assertFalse(dtsTaskList.isEmpty());
    }

    @Test
    public void deleteTaskSuccess(){
        //Get the last task created to delete the task. Its a random task that we consider safe to be deleted in this test
        DTSTask lastTask = idtsTaskRepository.findAll().stream().reduce((first, second)->second).get();
        taskService.deleteTask(lastTask.getId());

        //Check that the task no longer exists.
        assertTrue(idtsTaskRepository.findById(lastTask.getId()).isEmpty());
    }

    @Test
    public void deleteTaskFailDueToNoneExistingTask(){
        //Delete task that the id equals 1000. We know there is no task matching this.
        TaskNotExistException tneex = assertThrows(TaskNotExistException.class,
             ()->taskService.deleteTask(10000L)
        );

        //Check to confirm that the exception message thrown matches the message indicating no tasks match the ID
        assertEquals(tneex.getMessage(), "There are no tasks matching the identifier. To delete a task, a valid identifier matching an existing task must be provided.");
    }

    @Test
    public void updateTaskSuccessTest(){

        //Get the task matching the ID number 3
        DTSTask existingTask = taskService.getTaskById(3L);
        TaskStatus currentTaskStatus = existingTask.getTaskStatus();

        //Check that the task found exists
        assertNotNull(existingTask);

        //Check that the status of the task fetched equals to 'ACTIVE_TRIAL'
        assertEquals(currentTaskStatus, TaskStatus.CLOSED);

        //Update the task updating the status to 'ADJOURNED'
        UpdateTaskRequest request = new UpdateTaskRequest();
        request.setTaskStatus(TaskStatus.ADJOURNED.getCode());
        request.setTitle(existingTask.getTitle());
        request.setDescription(existingTask.getDescription());
        request.setDueDateTime(existingTask.getDueDateTime());
        request.setTaskId(existingTask.getId());
        DTSTask dtsTask = taskService.updateTask(request);

        //Check that the status of the task previously no longer is the same as the updated task
        assertNotEquals(existingTask.getTaskStatus(), dtsTask.getTaskStatus());

        //Check that the status has been updated to Adjourned
        assertEquals(dtsTask.getTaskStatus(), TaskStatus.ADJOURNED);

        //Check that the id of the previous task and the updated task are the same
        assertEquals(existingTask.getId(), dtsTask.getId());

        //Reset status back to previous status for subsequent running of tests
        request = new UpdateTaskRequest();
        request.setTaskStatus(currentTaskStatus.getCode());
        request.setTitle(existingTask.getTitle());
        request.setDescription(existingTask.getDescription());
        request.setDueDateTime(existingTask.getDueDateTime());
        request.setTaskId(existingTask.getId());
        taskService.updateTask(request);
    }


    @Test
    public void updateTaskFailDueToExistingTaskTitleTest(){

        //Get two tasks, one to be updated and another task that we want to get its title to update the first fetched tsak.
        DTSTask existingTask = taskService.getTaskById(3L);
        DTSTask existingTask2 = taskService.getTaskById(8L);

        //Check to confirm that both tasks fetched exist and are not null
        assertNotNull(existingTask);
        assertNotNull(existingTask2);

        //Set the title of the first task to be the same as the second one
        UpdateTaskRequest request = new UpdateTaskRequest();
        request.setTaskStatus(TaskStatus.DISMISSED.getCode());
        request.setTitle(existingTask2.getTitle());
        request.setDescription(existingTask.getDescription());
        request.setDueDateTime(existingTask.getDueDateTime());
        request.setTaskId(existingTask.getId());

        //Check to confirm that an exception is thrown because there already exists a task with the title
        TaskExistException teex = assertThrows(
            TaskExistException.class,
            ()-> taskService.updateTask(request)
        );
        assertEquals(teex.getMessage(), "There already exists a task with that title. Please provide another title for this case");

    }



}
