package com.tongji.chaigrouping.service.impl;

import com.tongji.chaigrouping.dto.CreateNotificationDto;
import com.tongji.chaigrouping.dto.TaskCreationDto;
import com.tongji.chaigrouping.dto.TaskDescriptionDto;
import com.tongji.chaigrouping.entity.Task;
import com.tongji.chaigrouping.exception.AiServiceNotAvailableException;
import com.tongji.chaigrouping.mapper.GroupMapper;
import com.tongji.chaigrouping.mapper.MembershipMapper;
import com.tongji.chaigrouping.mapper.TaskMapper;
import com.tongji.chaigrouping.aiservice.MoonshotAiUtils;
import com.tongji.chaigrouping.aiservice.TaskMatchRequest;
import com.tongji.chaigrouping.aiservice.TaskMatchResponse;
import com.tongji.chaigrouping.service.TaskCreationService;
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
    private NotificationListServiceImpl notificationListServiceImpl;
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
            notificationListServiceImpl.sendNotification(assigneeId, createNotificationDto);
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