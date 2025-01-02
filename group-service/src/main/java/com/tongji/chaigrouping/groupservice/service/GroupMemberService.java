package com.tongji.chaigrouping.groupservice.service;

import com.tongji.chaigrouping.commonutils.dto.GroupMemberDetailDto;

import java.nio.file.AccessDeniedException;

public interface GroupMemberService {
    GroupMemberDetailDto queryGroupMember(Integer userId, Integer groupId, Integer memberId) throws AccessDeniedException;
    void quitGroup(Integer userId, Integer groupId);
    void kickMember(Integer leaderId, Integer groupId, Integer memberId) throws AccessDeniedException;

    interface JoinRequestService {
//        void createJoinRequest(Integer userId, JoinRequestDto requestDto);

        void approveJoinRequest(Integer leaderId, Integer joinRequestId);
        void rejectJoinRequest(Integer leaderId, Integer joinRequestId);

    }
}
