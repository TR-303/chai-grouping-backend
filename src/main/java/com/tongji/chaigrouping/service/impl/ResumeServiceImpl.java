package com.tongji.chaigrouping.service.impl;

import com.tongji.chaigrouping.dto.ResumeDto;
import com.tongji.chaigrouping.entity.User;
import com.tongji.chaigrouping.exception.InvalidUserException;
import com.tongji.chaigrouping.mapper.UserMapper;
import com.tongji.chaigrouping.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ResumeServiceImpl implements ResumeService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public ResumeDto getResume(Integer userId) {
        User user = userMapper.selectById(userId);
        if(user == null) {
            throw new InvalidUserException("User not found");
        }
        return user.getResume();
    }

    @Override
    public void updateResume(Integer userId, ResumeDto resumeDto) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new InvalidUserException("User not found");
        }
        if (resumeDto == null ||
                resumeDto.getSchool() == null || resumeDto.getSchool().isEmpty() ||
                resumeDto.getGrade() == null || resumeDto.getGrade().isEmpty() ||
                resumeDto.getSkillDescription() == null || resumeDto.getSkillDescription().isEmpty() ||
                !(resumeDto.getGrade().equals("大一") ||
                        resumeDto.getGrade().equals("大二") ||
                        resumeDto.getGrade().equals("大三") ||
                        resumeDto.getGrade().equals("大四"))) {
                throw new InvalidUserException("参数不合法");
        }
        user.setResume(resumeDto);
        userMapper.updateById(user);
    }
}