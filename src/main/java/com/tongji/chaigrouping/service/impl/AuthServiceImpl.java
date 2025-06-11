package com.tongji.chaigrouping.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tongji.chaigrouping.utils.JwtTokenUtil;
import com.tongji.chaigrouping.dto.LoginResultDto;
import com.tongji.chaigrouping.entity.User;
import com.tongji.chaigrouping.exception.InvalidLoginException;
import com.tongji.chaigrouping.mapper.UserMapper;
import com.tongji.chaigrouping.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void register(String username, String password) {
        if (userMapper.exists(new QueryWrapper<User>().eq("username", username))) {
            throw new InvalidLoginException("Username already exists");
        }
        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));
        userMapper.insert(user);
    }

    @Override
    public LoginResultDto login(String username, String password) {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidLoginException("Invalid username or password");
        }
        return new LoginResultDto("登录成功", user.getUserId(), jwtTokenUtil.generateToken(user.getUserId()));
    }
}