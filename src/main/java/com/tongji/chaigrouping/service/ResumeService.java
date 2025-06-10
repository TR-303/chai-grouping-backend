package com.tongji.chaigrouping.service;

import com.tongji.chaigrouping.dto.ResumeDto;

public interface ResumeService {
    ResumeDto getResume(Integer userId);

    void updateResume(Integer userId, ResumeDto resumeDto);

}