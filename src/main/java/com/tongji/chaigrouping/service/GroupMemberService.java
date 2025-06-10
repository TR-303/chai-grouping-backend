package com.tongji.chaigrouping.service;

import com.tongji.chaigrouping.dto.GroupMemberDetailDto;
import com.tongji.chaigrouping.exception.AccessDeniedException;

public interface GroupMemberService {
    GroupMemberDetailDto queryGroupMember(Integer userId, Integer groupId, Integer memberId) throws AccessDeniedException;
    void quitGroup(Integer userId, Integer groupId);
    void kickMember(Integer leaderId, Integer groupId, Integer memberId) throws AccessDeniedException;
    void transferLeader(Integer leaderId, Integer groupId, Integer memberId) throws AccessDeniedException;
}