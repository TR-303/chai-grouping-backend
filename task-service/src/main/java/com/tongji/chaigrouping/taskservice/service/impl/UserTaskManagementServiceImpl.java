package com.tongji.chaigrouping.taskservice.service.impl;

import com.tongji.chaigrouping.commonutils.dto.TaskListItemDto;
import com.tongji.chaigrouping.commonutils.entity.Task;
import com.tongji.chaigrouping.commonutils.mapper.TaskMapper;
import com.tongji.chaigrouping.taskservice.service.UserTaskManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserTaskManagementServiceImpl implements UserTaskManagementService {
    @Autowired
    private TaskMapper taskMapper;

    @Override
    public List<TaskListItemDto> getUserTaskList(Integer userId) {
        List<Task> tasks = taskMapper.selectListByUserId(userId);
        return tasks.stream().map(Task::toTaskListItemDto).collect(Collectors.toList());
    }
}
