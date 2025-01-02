package com.tongji.chaigrouping.taskservice.service;

import com.tongji.chaigrouping.commonutils.dto.TaskListItemDto;

import java.util.List;

public interface UserTaskManagementService {
    List<TaskListItemDto> getUserTaskList(Integer userId);

}
