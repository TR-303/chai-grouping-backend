package com.tongji.chaigrouping.groupservice.service.impl;

import com.tongji.chaigrouping.commonutils.dto.CreateRequestDto;
import com.tongji.chaigrouping.commonutils.dto.RespondToRequestDto;
import com.tongji.chaigrouping.commonutils.entity.JoinRequest;
import com.tongji.chaigrouping.commonutils.entity.Membership;
import com.tongji.chaigrouping.commonutils.mapper.GroupMapper;
import com.tongji.chaigrouping.commonutils.mapper.JoinRequestMapper;
import com.tongji.chaigrouping.commonutils.mapper.MembershipMapper;
import com.tongji.chaigrouping.groupservice.service.JoinRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

@Service
public class JoinRequestServiceImpl implements JoinRequestService {

    @Autowired
    JoinRequestMapper joinRequestMapper;
    @Autowired
    MembershipMapper membershipMapper;
    @Autowired
    GroupMapper groupMapper;


    @Override
    public void createRequest(Integer userId, Integer groupId, CreateRequestDto requestDto)throws RuntimeException {
        String description = requestDto.getDescription();
        JoinRequest joinRequest = new JoinRequest(null,userId,groupId,new Date(),description,"PENDING");
        joinRequestMapper.insert(joinRequest);

//        Integer memberCnt = groupMapper.getMemberCount(groupId);
//        Integer volume = groupMapper.selectById(groupId).getVolume();
//        boolean canAdd = memberCnt < volume;
        boolean needApproval = groupMapper.selectById(groupId).getApprovalRequired()!=0;

        if(!needApproval){ // 组长设置了不需要审批，则直接立即同意
            Integer requestId = joinRequest.getJoinRequestId();
            respondToRequest(groupMapper.selectById(groupId).getLeaderId(),requestId,new RespondToRequestDto("APPROVE"));
        }
    }

    @Override
    public void respondToRequest(Integer leaderId, Integer requestId, RespondToRequestDto respondToRequestDto)throws RuntimeException {
        boolean approve = Objects.equals(respondToRequestDto.getAction(), "APPROVE");
        boolean reject = Objects.equals(respondToRequestDto.getAction(), "REJECT");

        var request = joinRequestMapper.selectById(requestId);
        if(approve){
            Integer groupId = request.getGroupId();
            Integer memberCnt = groupMapper.getMemberCount(groupId);
            Integer volume = groupMapper.selectById(groupId).getVolume();
            boolean canAdd = memberCnt < volume;
            if(!canAdd)
                throw new RuntimeException("组已满，无法加入！");
            joinRequestMapper.selectById(requestId).setState("APPROVE");
            membershipMapper.insert(new Membership(request.getUserId(),groupId,new Date()));
        }else if(reject) {
            joinRequestMapper.selectById(requestId).setState("REJECT");
        }
        else {
            throw new RuntimeException("Unknown action");
        }
    }
}
