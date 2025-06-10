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

    @Override
    public List<TaskListItemDto> getTaskList(Integer groupId) {
        return taskMapper.getGroupTaskList(groupId);
    }

    @Override
    public void reassignTask(Integer taskId, Integer assigneeId) {
        Task task = taskMapper.selectById(taskId);
        task.reassign(assigneeId);
        taskMapper.updateById(task);
    }

    @Override
    public TaskDetailDto getTaskDetail(Integer taskId) {
        return taskMapper.getTaskDetailById(taskId);
    }
}