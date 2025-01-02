package com.tongji.chaigrouping.userservice.service;

import com.tongji.chaigrouping.commonutils.dto.ResumeDto;

public interface ResumeService {
    ResumeDto getResume(Integer userId);

    void updateResume(Integer userId, ResumeDto resumeDto);

}
