package com.tongji.chaigrouping.groupservice.service.impl;

import com.tongji.chaigrouping.commonutils.dto.CreateNotificationDto;
import com.tongji.chaigrouping.commonutils.dto.CreateRequestDto;
import com.tongji.chaigrouping.commonutils.dto.RespondToRequestDto;
import com.tongji.chaigrouping.commonutils.entity.Group;
import com.tongji.chaigrouping.commonutils.entity.JoinRequest;
import com.tongji.chaigrouping.commonutils.entity.Membership;
import com.tongji.chaigrouping.commonutils.mapper.GroupMapper;
import com.tongji.chaigrouping.commonutils.mapper.JoinRequestMapper;
import com.tongji.chaigrouping.commonutils.mapper.MembershipMapper;
import com.tongji.chaigrouping.commonutils.mapper.UserMapper;
import com.tongji.chaigrouping.groupservice.client.NotificationServiceClient;
import com.tongji.chaigrouping.groupservice.service.JoinRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Objects;

@Service
public class JoinRequestServiceImpl implements JoinRequestService {

    @Autowired
    JoinRequestMapper joinRequestMapper;
    @Autowired
    MembershipMapper membershipMapper;
    @Autowired
    GroupMapper groupMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    NotificationServiceClient notificationServiceClient;

    @Override
    @Transactional
    public void createRequest(Integer userId, Integer groupId, CreateRequestDto requestDto)throws RuntimeException {
        // 获取关于目标小组的信息
        Group group = groupMapper.selectById(groupId);
        boolean needApproval = group.getApprovalRequired() != 0;
        String groupName = group.getName();
        Integer leaderId = group.getLeaderId();

        // 获取关于申请人的信息
        String username = userMapper.selectById(userId).getUsername();

        if (needApproval) {
            // 创建一个新的加入请求
            JoinRequest joinRequest = new JoinRequest(null, userId, groupId, new Date(), requestDto.getDescription(), "PENDING");
            joinRequestMapper.insert(joinRequest);
            Integer requestId = joinRequest.getJoinRequestId();
            notificationServiceClient.sendNotification(leaderId, new CreateNotificationDto(
                    "有新的加入请求",
                    "用户 " + username + " 请求加入您的小组 " + groupName,
                    requestId
            ));
        } else {
            Integer memberCnt = groupMapper.getMemberCount(groupId);
            Integer volume = groupMapper.selectById(groupId).getVolume();
            boolean canAdd = memberCnt < volume;
            if (!canAdd)
                throw new RuntimeException("组已满，无法加入！");
            membershipMapper.insert(new Membership(groupId, userId, new Date()));
            notificationServiceClient.sendNotification(leaderId, new CreateNotificationDto(
                    "新组员加入组",
                    "用户 " + username + " 加入您的组 " + groupName,
                    null
            ));
        }
    }

    @Override
    @Transactional
    public void respondToRequest(Integer leaderId, Integer requestId, RespondToRequestDto respondToRequestDto)throws RuntimeException {
        boolean approve = Objects.equals(respondToRequestDto.getAction(), "APPROVE");
        boolean reject = Objects.equals(respondToRequestDto.getAction(), "REJECT");

        var request = joinRequestMapper.selectById(requestId);
        if (approve) {
            Integer groupId = request.getGroupId();
            Integer memberCnt = groupMapper.getMemberCount(groupId);
            Integer volume = groupMapper.selectById(groupId).getVolume();
            boolean canAdd = memberCnt < volume;
            if (!canAdd)
                throw new RuntimeException("组已满，无法加入！");
            request.setState("APPROVED");
            joinRequestMapper.updateById(request);
            membershipMapper.insert(new Membership(groupId, request.getUserId(), new Date()));
        } else if (reject) {
            request.setState("REJECTED");
            joinRequestMapper.updateById(request);
        } else {
            throw new RuntimeException("Unknown action");
        }
    }

}
