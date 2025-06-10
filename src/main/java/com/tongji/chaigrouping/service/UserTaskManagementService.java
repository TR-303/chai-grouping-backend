package com.tongji.chaigrouping.service;

import com.tongji.chaigrouping.dto.TaskListItemDto;

import java.util.List;

public interface UserTaskManagementService {
    List<TaskListItemDto> getUserTaskList(Integer userId);

}