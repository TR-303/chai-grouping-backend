package com.tongji.chaigrouping.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tongji.chaigrouping.dto.AccountInfoDto;
import com.tongji.chaigrouping.entity.User;
import com.tongji.chaigrouping.exception.InvalidAccountInfoException;
import com.tongji.chaigrouping.exception.InvalidUserException;
import com.tongji.chaigrouping.mapper.UserMapper;
import com.tongji.chaigrouping.service.AccountService;
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
