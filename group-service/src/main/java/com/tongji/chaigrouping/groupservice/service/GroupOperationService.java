package com.tongji.chaigrouping.groupservice.service;

import com.tongji.chaigrouping.commonutils.dto.GroupDetailResponseDto;
import com.tongji.chaigrouping.commonutils.dto.GroupInfoDto;
import com.tongji.chaigrouping.commonutils.dto.UserGroupListDto;

import java.util.List;
import java.util.Map;

public interface GroupOperationService {
    List<UserGroupListDto> groupList(Integer userId);
    GroupDetailResponseDto groupDetail(Integer memberId, Integer groupId);
    Map<String, Object> createGroup(Integer userId, GroupInfoDto groupInfoDto);
    Map<String, Object> updateGroup(Integer userId, Integer groupId, GroupInfoDto groupInfoDto);
    Map<String, Object> disbandGroup(Integer leaderId, Integer groupId);
}
