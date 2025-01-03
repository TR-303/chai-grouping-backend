package com.tongji.chaigrouping.groupservice.service;

import com.tongji.chaigrouping.commonutils.dto.GroupFilterDto;
import com.tongji.chaigrouping.commonutils.dto.GroupListDto;

import java.util.List;

public interface FindGroupService {

    // 可见、可加入的组
    List<GroupListDto> findGroup(Integer userId);

    List<GroupListDto> findGroupByAI(Integer userId);

    List<GroupListDto> searchGroup(Integer userId, String keyword);

    List<GroupListDto> filterGroup(Integer userId, GroupFilterDto filterDto);
}
