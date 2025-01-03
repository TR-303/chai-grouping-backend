package com.tongji.chaigrouping.groupservice.service;

import com.tongji.chaigrouping.aiservice.MatchGroupResponseItem;
import com.tongji.chaigrouping.commonutils.dto.GroupFilterDto;
import com.tongji.chaigrouping.commonutils.dto.GroupListDto;

import java.util.List;

public interface FindGroupService {

    List<MatchGroupResponseItem> findGroupByAI(Integer userId);

    // 可见、可加入的组
    List<GroupListDto> filterGroup(Integer userId, GroupFilterDto filterDto);
}
