package com.tongji.chaigrouping.service;

import com.tongji.chaigrouping.aiservice.TaskMatchResponse;
import com.tongji.chaigrouping.dto.TaskCreationDto;
import com.tongji.chaigrouping.dto.TaskDescriptionDto;
import com.tongji.chaigrouping.exception.AiServiceNotAvailableException;

public interface TaskCreationService {
    Integer createTask(Integer groupId, TaskCreationDto taskCreationDto);

    TaskMatchResponse recommendAssignee(Integer groupId, TaskDescriptionDto taskDescriptionDto) throws AiServiceNotAvailableException;

}