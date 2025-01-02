package com.tongji.chaigrouping.userservice.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tongji.chaigrouping.commonutils.utils.JwtTokenUtil;
import com.tongji.chaigrouping.commonutils.entity.User;
import com.tongji.chaigrouping.userservice.exception.InvalidLoginException;
import com.tongji.chaigrouping.commonutils.mapper.UserMapper;
import com.tongji.chaigrouping.userservice.service.AuthService;
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
    public String login(String username, String password) {
        User user = userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidLoginException("Invalid username or password");
        }
        return jwtTokenUtil.generateToken(user.getUserId());
    }
}
