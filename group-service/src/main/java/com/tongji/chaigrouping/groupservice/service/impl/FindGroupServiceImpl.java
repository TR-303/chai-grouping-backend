package com.tongji.chaigrouping.groupservice.service.impl;

import com.tongji.chaigrouping.commonutils.dto.GroupDetailResponseDto;
import com.tongji.chaigrouping.commonutils.dto.GroupFilterDto;
import com.tongji.chaigrouping.commonutils.dto.GroupListDto;
import com.tongji.chaigrouping.commonutils.entity.User;
import com.tongji.chaigrouping.commonutils.mapper.GroupMapper;
import com.tongji.chaigrouping.aiservice.*;
import com.tongji.chaigrouping.commonutils.mapper.MembershipMapper;
import com.tongji.chaigrouping.commonutils.mapper.UserMapper;
import com.tongji.chaigrouping.groupservice.service.FindGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FindGroupServiceImpl implements FindGroupService {

    @Autowired
    private GroupMapper groupMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MoonshotAiUtils moonshotAiUtils;
    @Autowired
    private MembershipMapper membershipMapper;


    /// TODO: AI 推荐
    @Override
    public List<MatchGroupResponseItem> findGroupByAI(Integer userId) {

        MatchGroupRequest matchGroupRequest = new MatchGroupRequest();

        // 获取这名用户的信息
        User user = userMapper.selectById(userId);
        MatchGroupRequest.ResumeItem applicator =
                new MatchGroupRequest.ResumeItem(
                        user.getSchool(),
                        user.getGrade(),
                        user.getSkillDescription());
        matchGroupRequest.setApplicator(applicator);

        // 获取用户可加入的组
        List<GroupListDto> groupList = groupMapper.findVisibleGroups(userId);
        groupList = groupList.subList(0, Math.min(20, groupList.size())); // 太多了不好


        // 为每个组获取详情和成员信息
        List<MatchGroupRequest.GroupItem> knownGroups =new ArrayList<MatchGroupRequest.GroupItem>();
        for(GroupListDto group: groupList){
            GroupDetailResponseDto groupDetail = groupMapper.getGroupDetail(group.getGroupId());
            List<MatchGroupRequest.ResumeItem> members = membershipMapper.queryGroupResumes(group.getGroupId());
            knownGroups.add(
                    new MatchGroupRequest.GroupItem(
                            group.getGroupId(),
                            group.getName(),
                            group.getDescription(),
                            members));
        }
        matchGroupRequest.setGroups(knownGroups);
        System.out.println(matchGroupRequest);
        return moonshotAiUtils.matchGroup(matchGroupRequest);
    }

    @Override
    public List<GroupListDto> filterGroup(Integer userId, GroupFilterDto filterDto) {
        return groupMapper.filterGroup(userId, filterDto);
    }
}
