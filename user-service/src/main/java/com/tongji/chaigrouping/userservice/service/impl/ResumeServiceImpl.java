package com.tongji.chaigrouping.userservice.service.impl;

import com.tongji.chaigrouping.commonutils.dto.ResumeDto;
import com.tongji.chaigrouping.commonutils.entity.User;
import com.tongji.chaigrouping.userservice.exception.InvalidUserException;
import com.tongji.chaigrouping.commonutils.mapper.UserMapper;
import com.tongji.chaigrouping.userservice.service.ResumeService;
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
        if(user == null) {
            throw new InvalidUserException("User not found");
        }
        user.setResume(resumeDto);
        userMapper.updateById(user);
    }
}
