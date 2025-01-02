package com.tongji.chaigrouping.userservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tongji.chaigrouping.commonutils.dto.AccountInfoDto;
import com.tongji.chaigrouping.commonutils.entity.User;
import com.tongji.chaigrouping.userservice.exception.InvalidAccountInfoException;
import com.tongji.chaigrouping.userservice.exception.InvalidUserException;
import com.tongji.chaigrouping.commonutils.mapper.UserMapper;
import com.tongji.chaigrouping.userservice.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {
    @Autowired
    private UserMapper userMapper;

    @Override
    public AccountInfoDto getAccountInfo(Integer userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new InvalidUserException("User not found");
        }
        return user.getAccountInfo();
    }

    @Override
    public void updateAccountInfo(Integer userId, AccountInfoDto accountInfoDto) throws InvalidAccountInfoException {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new InvalidUserException("User not found");
        }
        if (userMapper.exists(new QueryWrapper<User>().eq("username", accountInfoDto.getUsername()).ne("user_id", userId))) {
            throw new InvalidAccountInfoException("Username already exists");
        }
        user.setAccountInfo(accountInfoDto);
        userMapper.updateById(user);
    }
}
