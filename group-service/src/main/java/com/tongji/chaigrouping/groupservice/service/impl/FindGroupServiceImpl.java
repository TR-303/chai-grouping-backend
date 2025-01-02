package com.tongji.chaigrouping.groupservice.service.impl;

import com.tongji.chaigrouping.commonutils.dto.GroupInfoDto;
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
    public List<GroupInfoDto> findGroup(Integer userId) {
        groupMapper.findVisibleGroups(userId);
        return null;
    }

    @Override
    public List<GroupInfoDto> findGroupByAI(Integer userId) {
        return null;
    }
}
