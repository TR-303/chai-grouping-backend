package com.tongji.chaigrouping.service.impl;

import com.tongji.chaigrouping.dto.GroupDetailResponseDto;
import com.tongji.chaigrouping.dto.GroupInfoDto;
import com.tongji.chaigrouping.dto.GroupMemberBriefDto;
import com.tongji.chaigrouping.dto.UserGroupListDto;
import com.tongji.chaigrouping.dto.CreateNotificationDto;
import com.tongji.chaigrouping.entity.Group;
import com.tongji.chaigrouping.entity.JoinRequest;
import com.tongji.chaigrouping.entity.Membership;
import com.tongji.chaigrouping.exception.AccessDeniedException;
import com.tongji.chaigrouping.mapper.GroupMapper;
import com.tongji.chaigrouping.mapper.JoinRequestMapper;
import com.tongji.chaigrouping.mapper.MembershipMapper;
import com.tongji.chaigrouping.service.GroupOperationService;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GroupOperationServiceImpl implements GroupOperationService {

    @Autowired
    GroupMapper groupMapper;
    @Autowired
    MembershipMapper membershipMapper;
    @Autowired
    JoinRequestMapper joinRequestMapper;
    @Autowired
    NotificationListServiceImpl notificationListServiceImpl;

    @Override
    public List<UserGroupListDto> groupList(Integer userId) {
        return groupMapper.getGroupList(userId);
    }

    @Override
    public GroupDetailResponseDto groupDetail(Integer memberId, Integer groupId) throws AccessDeniedException {
        GroupDetailResponseDto groupDetail = groupMapper.getGroupDetail(groupId);
        if (groupDetail == null) {
            throw new AccessDeniedException("群组已解散或不存在");
        }
        // 获取群组成员列表
        List<GroupMemberBriefDto> members = groupMapper.getGroupMembers(groupId);
        groupDetail.setMembers(members);

        return groupDetail;
    }

    /**
     * 创建一个新的群组，并将创建者设置为群组的领导者。
     *
     * @param userId        创建者的用户 ID
     * @param groupInfoDto  群组信息 DTO
     * @return 返回包含新创建群组信息的 Map
     */
    @Override
    @Transactional
    public Map<String, Object> createGroup(Integer userId, GroupInfoDto groupInfoDto) {


        Map<String, Object> result = new HashMap<>();

        // 参数校验
        if (groupInfoDto.getName() == null || groupInfoDto.getName().trim().isEmpty()
                || groupInfoDto.getDescription() == null
                || groupInfoDto.getVolume() == null || groupInfoDto.getVolume() <= 0
                || groupInfoDto.getVisibility() == null || (groupInfoDto.getVisibility() != 0 && groupInfoDto.getVisibility() != 1)
                || groupInfoDto.getApprovalRequired() == null || (groupInfoDto.getApprovalRequired() != 0 && groupInfoDto.getApprovalRequired() != 1)) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST, "参数不合法"
            );
        }

        // 创建 Group 实体并设置属性
        Group group = new Group();
        group.setName(groupInfoDto.getName());
        group.setDescription(groupInfoDto.getDescription());
        group.setVolume(groupInfoDto.getVolume());
        group.setVisibility(groupInfoDto.getVisibility());
        group.setApprovalRequired(groupInfoDto.getApprovalRequired());
        group.setDisbanded(0); // 默认未解散
        group.setLeaderId(userId);
        group.setCreationDate(new Date());

        // 插入群组信息
        groupMapper.insert(group);

        // 获取生成的 groupId
        Integer groupId = group.getGroupId();

        Membership membership = new Membership(groupId, userId, new Date());
        // 插入 membership 信息，将创建者设置为领导者
        membershipMapper.insert(membership);

        // 构建返回结果
        result.put("groupId", groupId);
        result.put("message", "群组创建成功");

        return result;
    }

    @Override
    @Transactional
    public Map<String, Object> updateGroup(Integer userId, Integer groupId, GroupInfoDto groupInfoDto) {
        Map<String, Object> result = new HashMap<>();

        // 验证群组是否存在且未解散
        Group group = groupMapper.selectById(groupId);
        if (group.getDisbanded() != 0) {
            result.put("message", "该群组已解散。");
            return result;
        }

        // 验证当前用户是否为群组领导者
        if (!groupMapper.isLeader(groupId, userId)) {
            result.put("message", "您无权更新此群组。");
            return result;
        }

        // 验证群组容量不小于当前成员数量
        int currentMemberCount = groupMapper.getMemberCount(groupId);
        if (groupInfoDto.getVolume() < currentMemberCount) {
            result.put("message", "群组容量不能小于当前成员数量。");
            return result;
        }

        // 执行更新操作
        int rowsAffected = groupMapper.updateGroupByDtoInfo(
                groupId,
                groupInfoDto.getName(),
                groupInfoDto.getDescription(),
                groupInfoDto.getVolume(),
                groupInfoDto.getVisibility(),
                groupInfoDto.getApprovalRequired()
        );

        if (rowsAffected > 0) {
            result.put("message", "群组更新成功。");
        } else {
            result.put("message", "群组更新失败。");
        }

        return result;
    }

    @Override
    @Transactional
    public Map<String, Object> disbandGroup(Integer leaderId, Integer groupId) {
        if (leaderId == null || groupId == null) {
            throw new IllegalArgumentException("不合法请求参数");
        }
        Group group = groupMapper.selectById(groupId);
        if (group == null) {
            throw new IllegalArgumentException("小组不存在");
        }
        if (!Objects.equals(leaderId, group.getLeaderId())) {
            throw new AccessDeniedException("您不是组长，无权解散此群组。");
        }
        List<JoinRequest> requests = joinRequestMapper.selectByGroupId(groupId);
        for (JoinRequest request : requests) {
            if ("PENDING".equals(request.getState())) {
                request.setState("REJECT");
                notificationListServiceImpl.sendNotification(
                    request.getUserId(),
                    new CreateNotificationDto(
                        "您发送的加入请求失效",
                        "您想要加入的目标小组" + group.getName() + "已经解散。",
                        null
                    )
                );
            }
        }
        String groupName = group.getName();
        group.setDisbanded(1);
        groupMapper.updateById(group);
        sendNotificationToAllGroupMembers(
            groupId,
            new CreateNotificationDto("小组解散", "小组 " + groupName + " 已解散", null)
        );
        Map<String, Object> result = new HashMap<>();
        result.put("message", "小组解散成功");
        return result;
    }

    private void sendNotificationToAllGroupMembers(Integer groupId, CreateNotificationDto notification){
        List<GroupMemberBriefDto> members = groupMapper.getGroupMembers(groupId);
        for(GroupMemberBriefDto member :members){
            notificationListServiceImpl.sendNotification(member.getUserId(), notification);
        }
    }
}