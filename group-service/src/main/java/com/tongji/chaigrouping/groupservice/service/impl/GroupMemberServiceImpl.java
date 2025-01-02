package com.tongji.chaigrouping.groupservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tongji.chaigrouping.commonutils.dto.GroupMemberDetailDto;
import com.tongji.chaigrouping.commonutils.entity.Group;
import com.tongji.chaigrouping.commonutils.entity.Membership;
import com.tongji.chaigrouping.commonutils.mapper.GroupMapper;
import com.tongji.chaigrouping.commonutils.mapper.MembershipMapper;
import com.tongji.chaigrouping.groupservice.service.GroupMemberService;
import com.tongji.chaigrouping.commonutils.mapper.TaskMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.Date;

@Service
public class GroupMemberServiceImpl implements GroupMemberService {
    @Autowired
    GroupMapper groupMapper;
    @Autowired
    MembershipMapper membershipMapper;
    @Autowired
    TaskMapper taskMapper;

    @Override
    public GroupMemberDetailDto queryGroupMember(Integer userId, Integer groupId, Integer memberId) throws AccessDeniedException {
        if(!membershipMapper.isMember(groupId, userId)){
            throw new AccessDeniedException("You are not a member of this group");
        }
        return membershipMapper.queryGroupMember( groupId,  memberId);
    }

    @Override
    public void quitGroup(Integer userId, Integer groupId) throws RuntimeException {
        if(groupMapper.isLeader(groupId, userId)){
            throw new RuntimeException("You are the leader of this group");
            /// TODO: 移交身份
        }

        QueryWrapper<Membership> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId).eq("group_id", groupId);
        membershipMapper.delete(queryWrapper);
    }

    @Override
    public void kickMember(Integer leaderId, Integer groupId, Integer memberId) throws AccessDeniedException {
        if(!groupMapper.isLeader(groupId, leaderId)){
            throw new AccessDeniedException("You are not the leader of this group");
        }

        QueryWrapper<Membership> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", memberId).eq("group_id", groupId);

//        taskMapper.markUnassigned(groupId, memberId);
        ///  TODO
        membershipMapper.delete(queryWrapper);
    }

    @Override
    public void addMember(Integer leaderId, Integer groupId, Integer memberId) throws AccessDeniedException {
        if(!groupMapper.isLeader(groupId, leaderId)){
            throw new AccessDeniedException("You are not the leader of this group");
        }

        Membership membership = new Membership(groupId, memberId, new Date());
        membershipMapper.insert(membership);
    }

    @Override
    public void transferLeader(Integer leaderId, Integer groupId, Integer memberId) throws AccessDeniedException {
        if(!groupMapper.isLeader(groupId, leaderId)){
            throw new AccessDeniedException("You are not the leader of this group");
        }
        Group group = groupMapper.selectById(groupId);
        group.setLeaderId(memberId);
        groupMapper.updateById(group);
    }


}
