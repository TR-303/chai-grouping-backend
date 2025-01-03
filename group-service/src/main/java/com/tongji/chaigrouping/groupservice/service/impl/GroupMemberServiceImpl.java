package com.tongji.chaigrouping.groupservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tongji.chaigrouping.commonutils.dto.CreateNotificationDto;
import com.tongji.chaigrouping.commonutils.dto.GroupMemberBriefDto;
import com.tongji.chaigrouping.commonutils.dto.GroupMemberDetailDto;
import com.tongji.chaigrouping.commonutils.entity.Group;
import com.tongji.chaigrouping.commonutils.entity.Membership;
import com.tongji.chaigrouping.commonutils.entity.Task;
import com.tongji.chaigrouping.commonutils.mapper.GroupMapper;
import com.tongji.chaigrouping.commonutils.mapper.MembershipMapper;
import com.tongji.chaigrouping.commonutils.mapper.UserMapper;
import com.tongji.chaigrouping.groupservice.client.NotificationServiceClient;
import com.tongji.chaigrouping.groupservice.service.GroupMemberService;
import com.tongji.chaigrouping.commonutils.mapper.TaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Objects;

@Service
public class GroupMemberServiceImpl implements GroupMemberService {
    @Autowired
    GroupMapper groupMapper;
    @Autowired
    MembershipMapper membershipMapper;
    @Autowired
    TaskMapper taskMapper;
    @Autowired
    NotificationServiceClient notificationServiceClient;
    @Autowired
    private UserMapper userMapper;

    @Override
    public GroupMemberDetailDto queryGroupMember(Integer userId, Integer groupId, Integer memberId) throws AccessDeniedException {
        if(!membershipMapper.isMember(groupId, userId)){
            throw new AccessDeniedException("You are not a member of this group");
        }
        GroupMemberDetailDto result = membershipMapper.queryGroupMember(groupId, memberId);
        if(result == null){
            throw new AccessDeniedException("The member is not in this group");
        }
        if(groupMapper.isLeader(groupId, memberId)){
            result.setRole("leader");
        }
        else{
            result.setRole("member");
        }
        return result;
    }

    @Override
    @Transactional
    public void quitGroup(Integer userId, Integer groupId) throws RuntimeException {
        if(groupMapper.isLeader(groupId, userId)){
            throw new RuntimeException("您是组长。请先移交组长身份再退出小组。");
        }
        removeMember(groupId, userId);
    }

    @Override
    @Transactional
    public void kickMember(Integer leaderId, Integer groupId, Integer memberId) throws AccessDeniedException {
        if(!groupMapper.isLeader(groupId, leaderId)){
            throw new AccessDeniedException("You are not the leader of this group");
        }
        removeMember(groupId, memberId);
    }

    @Override
    @Transactional
    public void transferLeader(Integer leaderId, Integer groupId, Integer memberId) throws AccessDeniedException {
        if(!groupMapper.isLeader(groupId, leaderId)){
            throw new AccessDeniedException("You are not the leader of this group");
        }
        Group group = groupMapper.selectById(groupId);
        group.setLeaderId(memberId);
        groupMapper.updateById(group);

        String prevname = userMapper.selectById(leaderId).getUsername();
        String newname = userMapper.selectById(memberId).getUsername();
        String groupname = groupMapper.selectById(groupId).getName();
        String title = "组长变动";
        String message = groupname + " 小组前组长 " + prevname + " 移交组长身份给 "+ newname +" 。";
        CreateNotificationDto notification = new CreateNotificationDto(title, message, null);
        sendNotificationToAllGroupMembers(groupId, notification);
    }

    private void removeMember(Integer groupId, Integer memberId){
        QueryWrapper<Membership> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", memberId).eq("group_id", groupId);

        // 重置该成员名下的任务
        int onGoingTasks = 0;
        List<Task> tasks = taskMapper.selectListByGroupId(groupId);
        for (Task task : tasks) {
            if(task.getUserId().equals(memberId) && Objects.equals(task.getState(), "ongoing")){
                task.setUserId(null);
                task.setState("unassigned");
                taskMapper.updateById(task);
                onGoingTasks++;
            }
        }
        membershipMapper.delete(queryWrapper);

        // 为小组中的剩下的成员发送通知，告诉他们有人离开了
        String username = userMapper.selectById(memberId).getUsername();
        String groupname = groupMapper.selectById(groupId).getName();
        String message = "小组前成员 " + username + " 现已退出小组 "+ groupname +" 。";
        if(onGoingTasks > 0){
            message += " 他/她名下的 " + onGoingTasks + " 个任务仍在进行，这些任务已回退至“未分配”状态。";
        }
        CreateNotificationDto notification = new CreateNotificationDto("用户退出小组", message,null);
        sendNotificationToAllGroupMembers(groupId, notification);
    }

    private void sendNotificationToAllGroupMembers(Integer groupId, CreateNotificationDto notification){
        List<GroupMemberBriefDto> members = groupMapper.getGroupMembers(groupId);
        for(GroupMemberBriefDto member :members){
            notificationServiceClient.sendNotification(member.getUserId(), notification);
        }
    }
}
