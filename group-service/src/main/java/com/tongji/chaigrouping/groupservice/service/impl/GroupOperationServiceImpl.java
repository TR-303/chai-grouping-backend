package com.tongji.chaigrouping.groupservice.service.impl;

import com.tongji.chaigrouping.commonutils.dto.*;
import com.tongji.chaigrouping.commonutils.entity.Group;
import com.tongji.chaigrouping.commonutils.entity.JoinRequest;
import com.tongji.chaigrouping.commonutils.entity.Membership;
import com.tongji.chaigrouping.commonutils.mapper.GroupMapper;
import com.tongji.chaigrouping.commonutils.mapper.JoinRequestMapper;
import com.tongji.chaigrouping.commonutils.mapper.MembershipMapper;
import com.tongji.chaigrouping.groupservice.client.NotificationServiceClient;
import com.tongji.chaigrouping.groupservice.service.GroupOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.*;

@Service
public class GroupOperationServiceImpl implements GroupOperationService {

    @Autowired
    GroupMapper groupMapper;
    @Autowired
    MembershipMapper membershipMapper;
    @Autowired
    NotificationServiceClient notificationServiceClient;
    @Autowired
    JoinRequestMapper joinRequestMapper;

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
        Map<String, Object> result = new HashMap<>();
        if (!Objects.equals(leaderId, groupMapper.selectById(groupId).getLeaderId())){
            result.put("message", "您不是组长，无权解散此群组。");
            return result;
        }
        List<JoinRequest> requests = joinRequestMapper.selectByGroupId(groupId);
        for(JoinRequest request : requests){
            if(request.getState().equals("PENDING")) {
                request.setState("REJECT");
                notificationServiceClient.sendNotification(request.getUserId(), new CreateNotificationDto(
                        "您发送的加入请求失效",
                        "您想要加入的目标小组" + groupMapper.selectById(groupId).getName() + "已经解散。",
                        null));
            }
        }

        String groupName = groupMapper.selectById(groupId).getName();
        Group group = groupMapper.selectById(groupId);
        group.setDisbanded(1);
        groupMapper.updateById(group);
        result.put("message", "小组解散成功");
        sendNotificationToAllGroupMembers(groupId, new CreateNotificationDto("小组解散", "小组 "+ groupName + " 已解散", null));
        return result;
    }

    private void sendNotificationToAllGroupMembers(Integer groupId, CreateNotificationDto notification){
        List<GroupMemberBriefDto> members = groupMapper.getGroupMembers(groupId);
        for(GroupMemberBriefDto member :members){
            notificationServiceClient.sendNotification(member.getUserId(), notification);
        }
    }
}
