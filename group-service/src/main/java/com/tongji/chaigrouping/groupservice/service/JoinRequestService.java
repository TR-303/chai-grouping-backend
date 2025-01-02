package com.tongji.chaigrouping.groupservice.service;

import com.tongji.chaigrouping.commonutils.dto.CreateRequestDto;
import com.tongji.chaigrouping.commonutils.dto.RespondToRequestDto;

public interface JoinRequestService {
    public void createRequest(Integer userId, Integer GroupId, CreateRequestDto requestDto);
    public void respondToRequest(Integer leaderId, Integer requestId, RespondToRequestDto respondToRequestDto);
}
