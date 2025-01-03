package com.tongji.chaigrouping.taskservice.service;

import com.tongji.chaigrouping.aiservice.TaskMatchResponse;
import com.tongji.chaigrouping.commonutils.dto.TaskCreationDto;
import com.tongji.chaigrouping.commonutils.dto.TaskDescriptionDto;
import com.tongji.chaigrouping.commonutils.exception.AiServiceNotAvailableException;

public interface TaskCreationService {
    Integer createTask(Integer groupId, TaskCreationDto taskCreationDto);

    TaskMatchResponse recommendAssignee(Integer groupId, TaskDescriptionDto taskDescriptionDto) throws AiServiceNotAvailableException;

}
