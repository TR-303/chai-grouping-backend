package com.tongji.chaigrouping.groupservice.service.impl;

import com.tongji.chaigrouping.commonutils.dto.GroupDetailResponseDto;
import com.tongji.chaigrouping.commonutils.dto.GroupInfoDto;
import com.tongji.chaigrouping.commonutils.dto.GroupMemberDto;
import com.tongji.chaigrouping.commonutils.dto.UserGroupListDto;
import com.tongji.chaigrouping.commonutils.entity.Group;
import com.tongji.chaigrouping.commonutils.entity.Membership;
import com.tongji.chaigrouping.groupservice.mapper.GroupMapper;
import com.tongji.chaigrouping.groupservice.mapper.MembershipMapper;
import com.tongji.chaigrouping.groupservice.service.GroupOperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class GroupOperationServiceImpl implements GroupOperationService {

    @Autowired
    GroupMapper groupMapper;
    @Autowired
    MembershipMapper membershipMapper;

    @Override
    public List<UserGroupListDto> groupList(Integer userId) {
        return groupMapper.getGroupList(userId);
    }

    @Override
    /*
      获取群组详细信息，包括成员列表

      @param memberId 当前请求的用户 ID（可用于权限检查）
     * @param groupId 目标群组 ID
     * @return GroupDetailResponseDto 包含群组详情和成员列表
     */
    public GroupDetailResponseDto groupDetail(Integer memberId, Integer groupId) {
        // 获取群组详细信息
        GroupDetailResponseDto groupDetail = groupMapper.getGroupDetail(groupId);
//        if (groupDetail == null) {
//            // 处理群组不存在的情况
////            throw new GroupNotFoundException("群组不存在");
//        }

        // 获取群组成员列表
        List<GroupMemberDto> members = groupMapper.getGroupMembers(groupId);
        if (groupDetail != null) {
            groupDetail.setMembers(members);
        }

        // 可选：根据 memberId 进行权限检查或自定义返回内容
        // 例如，检查当前用户是否为群组成员或群组领导
        // 这里可以添加相应的逻辑

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
        Integer leaderId = groupMapper.getGroupLeaderId(groupId);
        if (leaderId == null) {
            result.put("message", "群组不存在或已解散。");
            return result;
        }

        // 验证当前用户是否为群组领导者
        if (!leaderId.equals(userId)) {
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
    public Map<String, Object> disbandGroup(Integer leaderId, Integer groupId) {
        Map<String, Object> result = new HashMap<>();
        if (!Objects.equals(leaderId, groupMapper.selectById(groupId).getLeaderId())){
            result.put("message", "您不是组长，无权解散此群组。");
            return result;
        }
        // TODO: 清除入队申请等其他操作

        groupMapper.selectById(groupId).setDisbanded(1);
        result.put("message", "小组解散成功");
        return result;
    }
}
