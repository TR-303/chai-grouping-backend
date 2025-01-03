package com.tongji.chaigrouping.groupservice.service.impl;

import com.tongji.chaigrouping.commonutils.dto.GroupFilterDto;
import com.tongji.chaigrouping.commonutils.dto.GroupListDto;
import com.tongji.chaigrouping.commonutils.mapper.GroupMapper;
import com.tongji.chaigrouping.groupservice.service.FindGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FindGroupServiceImpl implements FindGroupService {

    @Autowired
    private GroupMapper groupMapper;

    @Override
    public List<GroupListDto> findGroup(Integer userId) {
        return groupMapper.findVisibleGroups(userId);
    }

    /// TODO: AI 推荐
    @Override
    public List<GroupListDto> findGroupByAI(Integer userId) {
        return null;
    }

    @Override
    public List<GroupListDto> searchGroup(Integer userId, String keyword) {
        return groupMapper.matchVisibleGroups(userId, keyword);
    }

    @Override
    public List<GroupListDto> filterGroup(Integer userId, GroupFilterDto filterDto) {
        return groupMapper.filterGroup(userId, filterDto);
    }
}
