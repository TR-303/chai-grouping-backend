package com.tongji.chaigrouping.service;

import com.tongji.chaigrouping.dto.TaskDetailDto;
import com.tongji.chaigrouping.dto.TaskListItemDto;

import java.util.List;

public interface GroupTaskManagementService {
    List<TaskListItemDto> getTaskList(Integer groupId);

    void reassignTask(Integer taskId, Integer assigneeId);

    TaskDetailDto getTaskDetail(Integer taskId);

}