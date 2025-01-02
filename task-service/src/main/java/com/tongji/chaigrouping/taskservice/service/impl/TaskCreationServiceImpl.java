package com.tongji.chaigrouping.taskservice.service.impl;

import com.tongji.chaigrouping.commonutils.dto.AssigneeRecommendationDto;
import com.tongji.chaigrouping.commonutils.dto.TaskCreationDto;
import com.tongji.chaigrouping.commonutils.dto.TaskDescriptionDto;
import com.tongji.chaigrouping.commonutils.exception.AiServiceNotAvailableException;
import com.tongji.chaigrouping.commonutils.entity.Task;
import com.tongji.chaigrouping.commonutils.mapper.TaskMapper;
import com.tongji.chaigrouping.taskservice.service.TaskCreationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaskCreationServiceImpl implements TaskCreationService {
    @Autowired
    private TaskMapper taskMapper;

    @Override
    public Integer createTask(Integer groupId, TaskCreationDto taskCreationDto) {

        Task task = new Task();
        task.initTask(groupId, taskCreationDto);
        taskMapper.insert(task);
        return task.getTaskId();
    }

    @Override
    public AssigneeRecommendationDto recommendAssignee(Integer groupId, TaskDescriptionDto taskDescriptionDto) throws AiServiceNotAvailableException {
        throw new AiServiceNotAvailableException("AI服务不可用");
    }

}
