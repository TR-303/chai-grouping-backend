package com.tongji.chaigrouping.taskservice.controller;

import com.tongji.chaigrouping.commonutils.dto.TaskCreationDto;
import com.tongji.chaigrouping.commonutils.dto.TaskDescriptionDto;
import com.tongji.chaigrouping.commonutils.dto.TaskDetailDto;
import com.tongji.chaigrouping.commonutils.dto.TaskListItemDto;
import com.tongji.chaigrouping.commonutils.exception.AiServiceNotAvailableException;
import com.tongji.chaigrouping.taskservice.service.GroupTaskManagementService;
import com.tongji.chaigrouping.taskservice.service.TaskCreationService;
import com.tongji.chaigrouping.taskservice.service.UserTaskManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tasks")
public class TasksController {
    @Autowired
    private TaskCreationService taskCreationService;

    @Autowired
    private GroupTaskManagementService groupTaskManagementService;

    @Autowired
    private UserTaskManagementService userTaskManagementService;

    @PostMapping("/{group_id}")
    public ResponseEntity<Object> createTask(@PathVariable("group_id") Integer groupId, @RequestBody TaskCreationDto taskCreationDto) {
        if (groupId == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "必须指定组id"));
        }
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("taskId", taskCreationService.createTask(groupId, taskCreationDto));
            response.put("message", "任务创建成功");
            return ResponseEntity.ok(response);
        } catch (AiServiceNotAvailableException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/auto_assign/{group_id}")
    public ResponseEntity<Object> autoAssignTask(@PathVariable("group_id") Integer groupId, @RequestBody TaskDescriptionDto taskDescriptionDto) {
        if (groupId == null) {
            return ResponseEntity.badRequest().body(Map.of("message", "必须指定组id"));
        }
        try {
            return ResponseEntity.ok(taskCreationService.recommendAssignee(groupId, taskDescriptionDto));
        } catch (AiServiceNotAvailableException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @PostMapping("/reassign/{task_id}/{user_id}")
    public ResponseEntity<Object> reassignTask(@PathVariable("task_id") Integer taskId, @PathVariable("user_id") Integer userId) {
        groupTaskManagementService.reassignTask(taskId, userId);
        return ResponseEntity.ok(Map.of("message", "任务重新分配成功"));
    }

    @GetMapping("/group/{group_id}")
    public ResponseEntity<List<TaskListItemDto>> getGroupTaskList(@PathVariable("group_id") Integer groupId) {
        return ResponseEntity.ok(groupTaskManagementService.getTaskList(groupId));
    }

    @GetMapping("/{task_id}")
    public ResponseEntity<TaskDetailDto> getTaskDetail(@PathVariable("task_id") Integer taskId){
        return ResponseEntity.ok(groupTaskManagementService.getTaskDetail(taskId));
    }

    @GetMapping("/user/{user_id}")
    public ResponseEntity<List<TaskListItemDto>> getUserTaskList(@PathVariable("user_id") Integer userId) {
        return ResponseEntity.ok(userTaskManagementService.getUserTaskList(userId));
    }

}

