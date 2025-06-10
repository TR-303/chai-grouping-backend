package com.tongji.chaigrouping.service;

import com.tongji.chaigrouping.dto.GroupDetailResponseDto;
import com.tongji.chaigrouping.dto.GroupInfoDto;
import com.tongji.chaigrouping.dto.UserGroupListDto;
import com.tongji.chaigrouping.exception.AccessDeniedException;

import java.util.List;
import java.util.Map;

public interface GroupOperationService {
    List<UserGroupListDto> groupList(Integer userId);
    GroupDetailResponseDto groupDetail(Integer memberId, Integer groupId) throws AccessDeniedException;
    Map<String, Object> createGroup(Integer userId, GroupInfoDto groupInfoDto);
    Map<String, Object> updateGroup(Integer userId, Integer groupId, GroupInfoDto groupInfoDto);
    Map<String, Object> disbandGroup(Integer leaderId, Integer groupId);
}