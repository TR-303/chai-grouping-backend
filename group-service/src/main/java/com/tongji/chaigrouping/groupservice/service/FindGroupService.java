package com.tongji.chaigrouping.groupservice.service;

import com.tongji.chaigrouping.commonutils.dto.GroupInfoDto;

import java.util.List;

public interface FindGroupService {

    // 可见、可加入的组
    List<GroupInfoDto> findGroup(Integer userId);

    List<GroupInfoDto> findGroupByAI(Integer userId);
}
