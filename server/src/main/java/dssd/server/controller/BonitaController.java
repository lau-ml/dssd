package dssd.server.controller;

import dssd.server.requests.*;
import dssd.server.service.BonitaService;
import jakarta.ws.rs.POST;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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



    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(@RequestBody RegisterBonitaRequest user) {
        return bonitaService.createUser(user);
    }


    @PostMapping("/createGroup")
    public ResponseEntity<?> createGroup(@RequestBody RegisterGroupRequest group) {
        return bonitaService.createGroup(group);
    }

    @PostMapping("/createRole")
    public ResponseEntity<?> createRole(@RequestBody RegisterRoleRequest role) {
        return bonitaService.createRole(role);
    }

    @PostMapping("/createMembership")
    public ResponseEntity<?> createMembership(@RequestBody MembershipBonitaRequest membership) {
        return bonitaService.createMembership(membership);
    }

    @DeleteMapping("/deleteMembership/{userId}/{groupId}/{roleId}")
    public ResponseEntity<?> deleteMembership(@PathVariable String userId, @PathVariable String groupId, @PathVariable String roleId) {
        return bonitaService.deleteMembership(userId, groupId, roleId);
    }

/*
http://localhost:61589/bonita/portal/resource/app/adminAppBonita/admin-task-list/API/bpm/flowNode?c=10&p=0&f=state=failed&d=rootContainerId&d=assigned_id&t=1731800334225
* */

    @GetMapping("/taskList")
    public ResponseEntity<String> getTaskList() {
        return bonitaService.getTaskList();
    }

    /*
    http://localhost:61589/bonita/portal/resource/app/adminAppBonita/admin-task-list/API/bpm/humanTask?c=10&p=0&f=state=ready&d=rootContainerId&d=assigned_id&t=1731800334225
    * */
    @GetMapping("/taskListReady")
    public ResponseEntity<?> getTaskListReady() {
        return bonitaService.getTaskListReady();
    }

    @GetMapping("/getArchivedTaskList")
    public ResponseEntity<?> getArchivedTaskList() {
        return bonitaService.getArchivedTask();
    }


    /*
    * http://localhost:8080/bonita/portal/resource/app/superAdminAppBonita/profile-list/API/portal/profileMember
    * */

    @PostMapping("/memberType")
    public ResponseEntity<?> memberType(@RequestBody ProfileRequest memberType) {
        return bonitaService.memberType(memberType);
    }

    @PostMapping("/groupSuperUser")
    public ResponseEntity<?> groupSuperUser(@RequestBody GroupSuperUserRequest groupSuperUser) {
        return bonitaService.groupSuperUser(groupSuperUser);
    }


    @PostMapping("/profileRoleIn")
    public ResponseEntity<?> profileRoleIn(@RequestBody ProfileRoleInRequest profileRoleIn) {
        return bonitaService.profileRoleIn(profileRoleIn);
    }

    @PostMapping("/roleSuperUser")
    public ResponseEntity<?> roleSuperUser(@RequestBody RoleSuperUserRequest roleSuperUser) {
        return bonitaService.roleSuperUser(roleSuperUser);
    }

    @GetMapping("/getMembershipByUserId/{userId}")
    public ResponseEntity<?> getMembershipByUserId(@PathVariable String userId) {
        return bonitaService.getMembershipByUserId(userId);
    }

    @GetMapping("/getVariableFromTaskId/{taskId}/{variableName}")
    public ResponseEntity<?> getVariableFromTaskId(@PathVariable String taskId, @PathVariable String variableName) {
        return bonitaService.getVariableFromTask(taskId, variableName);
    }

    @GetMapping("/assignVariableToTaskId/{taskId}/{variableName}/{variableValue}")
    public ResponseEntity<?> assignVariableToTaskId(@PathVariable String taskId, @PathVariable String variableName, @PathVariable String variableValue) {
        return bonitaService.assignVariableToTask(taskId, variableName, variableValue);
    }
}
