package com.tongji.chaigrouping.service;

import com.tongji.chaigrouping.aiservice.MatchGroupResponseItem;
import com.tongji.chaigrouping.dto.GroupFilterDto;
import com.tongji.chaigrouping.dto.GroupListDto;

import java.util.List;

public interface FindGroupService {

    List<MatchGroupResponseItem> findGroupByAI(Integer userId);

    // 可见、可加入的组
    List<GroupListDto> filterGroup(Integer userId, GroupFilterDto filterDto);
}