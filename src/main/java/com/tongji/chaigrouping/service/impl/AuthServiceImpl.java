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
        // 1) 用户名空或全空白
        if (username == null || username.isBlank()) {
            throw new InvalidLoginException("Username cannot be empty");
        }
        // 2) 用户名过长
        if (username.length() > 64) {
            throw new InvalidLoginException("Username too long");
        }
        // 3) 密码空
        if (password == null || password.isEmpty()) {
            throw new InvalidLoginException("Password cannot be empty");
        }
        // 4) 密码过长
        if (password.length() > 128) {
            throw new InvalidLoginException("Password too long");
        }
        // 5) 已存在校验
        if (userMapper.exists(new QueryWrapper<User>().eq("username", username))) {
            throw new InvalidLoginException("Username already exists");
        }
        // 6) 真正插入
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