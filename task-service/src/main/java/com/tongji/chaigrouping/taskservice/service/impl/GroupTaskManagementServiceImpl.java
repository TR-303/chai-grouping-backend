package com.tongji.chaigrouping.taskservice.service.impl;

import com.tongji.chaigrouping.commonutils.dto.TaskDetailDto;
import com.tongji.chaigrouping.commonutils.dto.TaskListItemDto;
import com.tongji.chaigrouping.commonutils.entity.Task;
import com.tongji.chaigrouping.commonutils.mapper.TaskMapper;
import com.tongji.chaigrouping.taskservice.service.GroupTaskManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GroupTaskManagementServiceImpl implements GroupTaskManagementService {
    @Autowired
    private TaskMapper taskMapper;

    @Override
    public List<TaskListItemDto> getTaskList(Integer groupId) {
        List<Task> tasks = taskMapper.selectListByGroupId(groupId);
        return tasks.stream().map(Task::toTaskListItemDto).collect(Collectors.toList());
    }

    @Override
    public void reassignTask(Integer taskId, Integer assigneeId) {
        Task task = taskMapper.selectById(taskId);
        if (task == null) {
            throw new RuntimeException("任务不存在");
        }
        task.reassign(assigneeId);
    }

    @Override
    public TaskDetailDto getTaskDetail(Integer taskId) {
        Task task = taskMapper.selectDetailById(taskId);
        if (task == null) {
            throw new RuntimeException("任务不存在");
        }
        return task.toTaskInfoDto();
    }
}
