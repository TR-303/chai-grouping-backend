package com.tongji.chaigrouping.service;

import com.tongji.chaigrouping.dto.CreateRequestDto;
import com.tongji.chaigrouping.dto.RespondToRequestDto;

public interface JoinRequestService {
    public void createRequest(Integer userId, Integer GroupId, CreateRequestDto requestDto);
    public void respondToRequest(Integer leaderId, Integer requestId, RespondToRequestDto respondToRequestDto);
}