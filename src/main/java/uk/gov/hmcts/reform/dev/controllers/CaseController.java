package uk.gov.hmcts.reform.dev.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.gov.hmcts.reform.dev.models.DTSTask;
import uk.gov.hmcts.reform.dev.models.ExampleCase;
import uk.gov.hmcts.reform.dev.repositories.IDTSTaskRepository;
import uk.gov.hmcts.reform.dev.requests.CreateNewCaseRequest;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.ok;

@RestController
public class CaseController {

    @Autowired
    private IDTSTaskRepository idtsTaskRepository;


    @GetMapping(value = "/get-example-case", produces = "application/json")
    public ResponseEntity<ExampleCase> getExampleCase() {
        return ok(new ExampleCase(1, "ABC12345", "Case Title",
                                  "Case Description", "Case Status", LocalDateTime.now()
        ));
    }

    @PostMapping(value = "/create-new-case", produces = "application/json")
    public ResponseEntity<DTSTask> createNewCase(@Valid @RequestBody CreateNewCaseRequest createNewCaseRequest)
    {
        DTSTask dtsTask = new DTSTask();
        dtsTask.setDescription(createNewCaseRequest.getTitle());
        dtsTask.setStatus("Test");
        dtsTask.setDueDateTime(createNewCaseRequest.getDueDateTime());
        dtsTask.setTitle(createNewCaseRequest.getTitle());
        idtsTaskRepository.save(dtsTask);
        return ResponseEntity.ok().body(dtsTask);
    }

    @GetMapping(value = "/get-case-by-id/{id}", produces = "application/json")
    public ResponseEntity<DTSTask> createNewCase(@PathVariable BigInteger id)
    {
        Optional<DTSTask> dtsTaskOptional = (Optional<DTSTask>)idtsTaskRepository.findById(id);
        return ResponseEntity.ok().body(dtsTaskOptional.get());
    }

    @GetMapping(value = "/get-all-cases", produces = "application/json")
    public ResponseEntity<Collection<DTSTask>> getAllCases()
    {
        Collection<DTSTask> dtsTaskCollection = (Collection<DTSTask>) idtsTaskRepository.findAll();
        return ResponseEntity.ok().body(dtsTaskCollection);
    }

    @GetMapping(value = "/update-case", produces = "application/json")
    public ResponseEntity<DTSTask> updateCase(DTSTask dtsTask)
    {
        idtsTaskRepository.save(dtsTask);
        return ResponseEntity.ok().body(dtsTask);
    }

    @GetMapping(value = "/delete-case/{id}", produces = "application/json")
    public ResponseEntity<Integer> updateCase(@PathVariable BigInteger id)
    {
        Optional<DTSTask> dtsTaskOptional = (Optional<DTSTask>)idtsTaskRepository.findById(id);
        DTSTask dtsTask = dtsTaskOptional.get();
        idtsTaskRepository.delete(dtsTask);
        return ResponseEntity.ok().body(1);
    }
}
