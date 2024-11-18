package dssd.server.controller;

import com.fasterxml.jackson.databind.JsonNode;
import dssd.server.initializer.BonitaInitializer;
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

    BonitaInitializer bonitaInitializer;
    public BonitaController(BonitaService bonitaService,
                            BonitaInitializer bonitaInitializer) {
        this.bonitaService = bonitaService;
        this.bonitaInitializer = bonitaInitializer;
    }

    @GetMapping("/process")
    public ResponseEntity<?> getProcess() {
        return bonitaService.getAllProcess();
    }

    @GetMapping("/startprocess/{id}")
    public ResponseEntity<?> startProcess(@PathVariable String id) {
        return bonitaService.startProcess(id);
    }

    @GetMapping("/processInstances")
    public ResponseEntity<JsonNode> findProcessInstancesByName(@Param("name") String name) {
        return bonitaService.findProcessInstancesByName(name);
    }

    @GetMapping("/processInstances/delete/{id}")
    public ResponseEntity<JsonNode> deleteProcessInstance(@PathVariable String id) {
        return bonitaService.deleteProcessInstance(id);
    }

    @GetMapping("getProcessByName")
    public ResponseEntity<?> getProcessByName(@Param("name") String name) {
        return bonitaService.getProcessByName(name);
    }


    @GetMapping("/getProcessById/{id}")
    public ResponseEntity<?> getProcessById(@PathVariable String id) {
        return bonitaService.getProcessById(id);
    }

    @GetMapping("/process/count")
    public ResponseEntity<JsonNode> getProcessCount() {
        return bonitaService.getProcessCount();
    }

    @GetMapping("/activeProcess/{id}")
    public ResponseEntity<?> getActiveProcessById(@PathVariable String id) {
        return bonitaService.getActiveProcessById(id);
    }

    @GetMapping("/case/{id}/variable/{variableName}/valor/{variableValue}")
    public ResponseEntity<?> setVariableByCaseId(@PathVariable String id, @PathVariable String variableName,@PathVariable String variableValue) {
        return bonitaService.setVariableByCaseId(id, variableName, variableValue);
    }

    @GetMapping("/assignTask/{taskId}/{userId}")
    public ResponseEntity<?> assignTask(@PathVariable String taskId, @PathVariable String userId) {
        return bonitaService.assignTask(taskId, userId);
    }

    @GetMapping("/searchActivityByCaseId/{caseId}")
    public ResponseEntity<?> searchActivityByCaseId(@PathVariable String caseId) {
        return bonitaService.searchActivityByCaseId(caseId);
    }

    @GetMapping("/completeActivity/{activityId}")
    public ResponseEntity<?> completeActivity(@PathVariable String activityId) {
        return bonitaService.completeActivity(activityId);
    }

    @GetMapping("/variable/{caseId}/{variableName}")
    public ResponseEntity<?> getVariableByCaseId(@PathVariable String caseId, @PathVariable String variableName) {
        return bonitaService.getVariableByCaseId(caseId, variableName);
    }
@GetMapping("/user/{username}")
    public ResponseEntity<?> getUserByUserName(@PathVariable("username") String username) {
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

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginBonitaRequest) {
        return bonitaService.login(loginBonitaRequest);
    }

/*
http://localhost:61589/bonita/portal/resource/app/adminAppBonita/admin-task-list/API/bpm/flowNode?c=10&p=0&f=state=failed&d=rootContainerId&d=assigned_id&t=1731800334225
* */

    @PostMapping("/initializeBonita")
    public ResponseEntity<?> initializeBonita() {
        return bonitaInitializer.initializeBonita();
    }

    @GetMapping("/taskList")
    public ResponseEntity<?> getTaskList() {
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

    @PostMapping("/logoutservice")
    public ResponseEntity<?> logoutService() {
        return bonitaService.logoutService();
    }

    @GetMapping("/assignVariableToTaskId/{taskId}/{variableName}/{variableValue}")
    public ResponseEntity<?> assignVariableToTaskId(@PathVariable String taskId, @PathVariable String variableName, @PathVariable String variableValue) {
        return bonitaService.assignVariableToTask(taskId, variableName, variableValue);
    }

    @GetMapping("/getGroupByName/{name}")
    public ResponseEntity<?> getGroupByName(@PathVariable String name) {
        return bonitaService.getGroupByName(name);
    }

    @GetMapping("/getRoleByName/{name}")
    public ResponseEntity<?> getRoleByName(@PathVariable String name) {
        return bonitaService.getRoleByName(name);
    }

    @GetMapping("/getUserByUsername/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        return bonitaService.getUserByUsername(username);
    }
}
