package com.tongji.chaigrouping.taskservice.service.impl;

import com.tongji.chaigrouping.commonutils.dto.CreateNotificationDto;
import com.tongji.chaigrouping.commonutils.dto.TaskCreationDto;
import com.tongji.chaigrouping.commonutils.dto.TaskDescriptionDto;
import com.tongji.chaigrouping.commonutils.exception.AiServiceNotAvailableException;
import com.tongji.chaigrouping.commonutils.entity.Task;
import com.tongji.chaigrouping.aiservice.*;
import com.tongji.chaigrouping.commonutils.mapper.GroupMapper;
import com.tongji.chaigrouping.commonutils.mapper.MembershipMapper;
import com.tongji.chaigrouping.commonutils.mapper.TaskMapper;
import com.tongji.chaigrouping.taskservice.client.NotificationServiceClient;
import com.tongji.chaigrouping.taskservice.service.TaskCreationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskCreationServiceImpl implements TaskCreationService {
    @Autowired
    private TaskMapper taskMapper;
    @Autowired
    private MembershipMapper membershipMapper;
    @Autowired
    private MoonshotAiUtils moonshotAiUtils;
    @Autowired
    private NotificationServiceClient notificationServiceClient;
    @Autowired
    private GroupMapper groupMapper;

    @Override
    public Integer createTask(Integer groupId, TaskCreationDto taskCreationDto) {
        Task task = new Task();
        task.initTask(groupId, taskCreationDto);
        taskMapper.insert(task);
        Integer assigneeId = taskCreationDto.getAssigneeId();
        String groupName = groupMapper.selectById(groupId).getName();
        if (assigneeId != null) {
            CreateNotificationDto createNotificationDto = new CreateNotificationDto(
                    "任务分配",
                    "您被分配了一个新任务：" + task.getDescription()+ "，请进入小组 "+groupName+" 查看详情。",
                    null
            );
            notificationServiceClient.sendNotification(assigneeId, createNotificationDto);
        }
        return task.getTaskId();
    }

    @Override
    public TaskMatchResponse recommendAssignee(Integer groupId, TaskDescriptionDto taskDescriptionDto) throws AiServiceNotAvailableException {
        List<TaskMatchRequest.CandidateItem> candidates = membershipMapper.queryTaskCandidates(groupId);
        candidates.subList(0, Math.min(candidates.size(), 10)); // 不要给LLM过多信息

        TaskMatchRequest taskMatchRequest = new TaskMatchRequest(
                taskDescriptionDto.getDescription(),
                candidates);

        return moonshotAiUtils.matchTask(taskMatchRequest);
        // TODO 如何判断AI服务不可用？
        //        throw new AiServiceNotAvailableException("AI服务不可用");
    }
}
