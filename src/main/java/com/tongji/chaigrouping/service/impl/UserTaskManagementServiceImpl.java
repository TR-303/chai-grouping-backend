package com.tongji.chaigrouping.service.impl;

import com.tongji.chaigrouping.dto.TaskListItemDto;
import com.tongji.chaigrouping.entity.Task;
import com.tongji.chaigrouping.mapper.TaskMapper;
import com.tongji.chaigrouping.service.UserTaskManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserTaskManagementServiceImpl implements UserTaskManagementService {
    @Autowired
    private TaskMapper taskMapper;

    @Override
    public List<TaskListItemDto> getUserTaskList(Integer userId) {
        return taskMapper.getUserTaskList(userId);
    }
}