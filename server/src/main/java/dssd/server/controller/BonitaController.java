package dssd.server.controller;

import dssd.server.service.BonitaService;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/bonita")
public class BonitaController {

    BonitaService bonitaService;

    public BonitaController(BonitaService bonitaService) {
        this.bonitaService = bonitaService;
    }

    @GetMapping("/process")
    public ResponseEntity<String> getProcess() {
        return bonitaService.getAllProcess();
    }

    @GetMapping("/startprocess/{id}")
    public ResponseEntity<String> startProcess(@PathVariable String id) {
        return bonitaService.startProcess(id);
    }

    @GetMapping("/processInstances")
    public ResponseEntity<String> findProcessInstancesByName(@Param("name") String name) {
        return bonitaService.findProcessInstancesByName(name);
    }

    @GetMapping("/processInstances/delete/{id}")
    public ResponseEntity<String> deleteProcessInstance(@PathVariable String id) {
        return bonitaService.deleteProcessInstance(id);
    }

    @GetMapping("getProcessByName")
    public ResponseEntity<String> getProcessByName(@Param("name") String name) {
        return bonitaService.getProcessByName(name);
    }


    @GetMapping("/getProcessById/{id}")
    public ResponseEntity<String> getProcessById(@PathVariable String id) {
        return bonitaService.getProcessById(id);
    }

    @GetMapping("/process/count")
    public ResponseEntity<String> getProcessCount() {
        return bonitaService.getProcessCount();
    }

    @GetMapping("/activeProcess/{id}")
    public ResponseEntity<String> getActiveProcessById(@PathVariable String id) {
        return bonitaService.getActiveProcessById(id);
    }

    @GetMapping("/case/{id}/variable/{variableName}/tipo/{tipo}/valor/{variableValue}")
    public ResponseEntity<String> setVariableByCaseId(@PathVariable String id, @PathVariable String variableName,@PathVariable String variableValue) {
        return bonitaService.setVariableByCaseId(id, variableName, variableValue);
    }

    @GetMapping("/assignTask/{taskId}/{userId}")
    public ResponseEntity<String> assignTask(@PathVariable String taskId, @PathVariable String userId) {
        return bonitaService.assignTask(taskId, userId);
    }

    @GetMapping("/searchActivityByCaseId/{caseId}")
    public ResponseEntity<String> searchActivityByCaseId(@PathVariable String caseId) {
        return bonitaService.searchActivityByCaseId(caseId);
    }

    @GetMapping("/completeActivity/{activityId}")
    public ResponseEntity<String> completeActivity(@PathVariable String activityId) {
        return bonitaService.completeActivity(activityId);
    }

    @GetMapping("/variable/{caseId}/{variableName}")
    public ResponseEntity<String> getVariableByCaseId(@PathVariable String caseId, @PathVariable String variableName) {
        return bonitaService.getVariableByCaseId(caseId, variableName);
    }
@GetMapping("/user/{username}")
    public ResponseEntity<String> getUserByUserName(@PathVariable("username") String username) {
        return bonitaService.getUserByUserName(username);
    }


}
