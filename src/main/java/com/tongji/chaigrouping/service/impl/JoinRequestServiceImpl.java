package com.tongji.chaigrouping.service.impl;

import com.tongji.chaigrouping.dto.CreateNotificationDto;
import com.tongji.chaigrouping.dto.CreateRequestDto;
import com.tongji.chaigrouping.dto.RespondToRequestDto;
import com.tongji.chaigrouping.entity.Group;
import com.tongji.chaigrouping.entity.JoinRequest;
import com.tongji.chaigrouping.entity.Membership;
import com.tongji.chaigrouping.mapper.*;
import com.tongji.chaigrouping.service.JoinRequestService;
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
    NotificationListServiceImpl notificationListServiceImpl;
    @Autowired
    NotificationMapper notificationMapper;
    @Autowired
    GroupMemberServiceImpl groupMemberServiceImpl;

    @Override
    @Transactional
    public void createRequest(Integer userId, Integer groupId, CreateRequestDto requestDto)throws RuntimeException {
        // 获取关于目标小组的信息
        Group group = groupMapper.selectById(groupId);
        if ((group == null || group.getDisbanded() != 0)) {
            throw new RuntimeException("小组不存在");
        }
        boolean needApproval = group.getApprovalRequired() != 0;
        String groupName = group.getName();
        Integer leaderId = group.getLeaderId();
        if(groupMemberServiceImpl.isMember(groupId, userId)) {
            throw new RuntimeException("您已经是该小组的成员了");
        }

        // 获取关于申请人的信息
        String username = userMapper.selectById(userId).getUsername();

        if (needApproval) {
            // 创建一个新的加入请求
            JoinRequest joinRequest = new JoinRequest(null, userId, groupId, new Date(), requestDto.getDescription(), "PENDING");
            joinRequestMapper.insert(joinRequest);
            Integer requestId = joinRequest.getJoinRequestId();
            notificationListServiceImpl.sendNotification(leaderId, new CreateNotificationDto(
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
            notificationListServiceImpl.sendNotification(leaderId, new CreateNotificationDto(
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
            notificationMapper.markJoinRequestNotificationAsRead(requestId);
            membershipMapper.insert(new Membership(groupId, request.getUserId(), new Date()));
        } else if (reject) {
            request.setState("REJECTED");
            joinRequestMapper.updateById(request);
            notificationMapper.markJoinRequestNotificationAsRead(requestId);
        } else {
            throw new RuntimeException("Unknown action");
        }
    }

}