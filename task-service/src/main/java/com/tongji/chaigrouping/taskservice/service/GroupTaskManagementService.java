package com.tongji.chaigrouping.taskservice.service;

import com.tongji.chaigrouping.commonutils.dto.TaskDetailDto;
import com.tongji.chaigrouping.commonutils.dto.TaskListItemDto;

import java.util.List;

public interface GroupTaskManagementService {
    List<TaskListItemDto> getTaskList(Integer groupId);

    void reassignTask(Integer taskId, Integer assigneeId);

    TaskDetailDto getTaskDetail(Integer taskId);

}
