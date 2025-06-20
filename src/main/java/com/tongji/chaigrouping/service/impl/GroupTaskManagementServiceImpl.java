package com.tongji.chaigrouping.service.impl;

import com.tongji.chaigrouping.dto.TaskDetailDto;
import com.tongji.chaigrouping.dto.TaskListItemDto;
import com.tongji.chaigrouping.entity.Task;
import com.tongji.chaigrouping.mapper.TaskMapper;
import com.tongji.chaigrouping.service.GroupTaskManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupTaskManagementServiceImpl implements GroupTaskManagementService {
    @Autowired
    private TaskMapper taskMapper;
    public GroupTaskManagementServiceImpl(TaskMapper taskMapper) {
        this.taskMapper = taskMapper;
    }
    @Override
    public List<TaskListItemDto> getTaskList(Integer groupId) {
        return taskMapper.getGroupTaskList(groupId);
    }

    @Override
    public void reassignTask(Integer taskId, Integer assigneeId) {
        if (taskId == null || assigneeId == null) {
            throw new IllegalArgumentException("taskId and assigneeId cannot be null");
        }
        Task task = taskMapper.selectById(taskId);

        if (task == null) {
            throw new IllegalArgumentException("Task not found for taskId: " + taskId);
        }

        if (task.getState() == null) {
            throw new IllegalArgumentException("State cannot be null");
        }

        if (task.getState().equals("已完成")) {
            throw new IllegalStateException("Task is already completed and cannot be reassigned");
        }

        task.reassign(assigneeId);
        taskMapper.updateById(task);
    }

    @Override
    public TaskDetailDto getTaskDetail(Integer taskId) {
        if (taskId == null || taskId <= 0) {
            throw new IllegalArgumentException("taskId must be a positive integer.");
        }
        return taskMapper.getTaskDetailById(taskId);
    }
}