package com.tongji.chaigrouping.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.tongji.chaigrouping.dto.LoginResultDto;
import com.tongji.chaigrouping.entity.User;
import com.tongji.chaigrouping.exception.InvalidLoginException;
import com.tongji.chaigrouping.mapper.UserMapper;
import com.tongji.chaigrouping.service.impl.AuthServiceImpl;
import com.tongji.chaigrouping.utils.JwtTokenUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthServiceImplTest {
    private UserMapper userMapper;
    private JwtTokenUtil jwtTokenUtil;
    private AuthServiceImpl service;

    @BeforeEach
    void setUp() {
        userMapper = mock(UserMapper.class);
        jwtTokenUtil = mock(JwtTokenUtil.class);
        service = new AuthServiceImpl();
        ReflectionTestUtils.setField(service, "userMapper", userMapper);
        ReflectionTestUtils.setField(service, "jwtTokenUtil", jwtTokenUtil);
    }

    @Test
    void testRegisterSuccess() {
        when(userMapper.exists(any(QueryWrapper.class))).thenReturn(false);
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        service.register("u","p");
        verify(userMapper).insert(captor.capture());
        User inserted = captor.getValue();
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        assertTrue(encoder.matches("p", inserted.getPassword()));
    }

    @Test
    void testRegisterDuplicate() {
        when(userMapper.exists(any(QueryWrapper.class))).thenReturn(true);
        assertThrows(InvalidLoginException.class, () -> service.register("u","p"));
    }



    @Test
    void testRegisterEmptyUsername() {
        when(userMapper.exists(any(QueryWrapper.class))).thenReturn(false);
        assertThrows(InvalidLoginException.class, () -> service.register("", "p"));
    }

    @Test
    void testRegisterEmptyPassword() {
        when(userMapper.exists(any(QueryWrapper.class))).thenReturn(false);
        assertThrows(InvalidLoginException.class, () -> service.register("u", ""));
    }


    @Test
    void testRegisterUsernameTooLong() {
        when(userMapper.exists(any(QueryWrapper.class))).thenReturn(false);
        String username = "a".repeat(65);
        assertThrows(InvalidLoginException.class,
                () -> service.register(username, "password"));
    }

    @Test
    void testRegisterPasswordTooLong() {
        when(userMapper.exists(any(QueryWrapper.class))).thenReturn(false);
        String password = "a".repeat(129);
        assertThrows(InvalidLoginException.class,
                () -> service.register("username", password));
    }

    @Test
    void testLoginSuccess() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = new User();
        user.setUserId(1);
        user.setUsername("u");
        user.setPassword(encoder.encode("p"));
        when(userMapper.selectOne(any(QueryWrapper.class))).thenReturn(user);
        when(jwtTokenUtil.generateToken(1)).thenReturn("token");

        LoginResultDto dto = service.login("u","p");
        assertEquals(1, dto.getUserId());
        assertEquals("token", dto.getToken());
    }

    @Test
    void testLoginFail() {
        when(userMapper.selectOne(any(QueryWrapper.class))).thenReturn(null);
        assertThrows(InvalidLoginException.class, () -> service.login("u","p"));
    }

    @Test
    void testLoginWrongPassword() {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        User user = new User();
        user.setUserId(1);
        user.setUsername("u");
        user.setPassword(encoder.encode("correct"));
        when(userMapper.selectOne(any(QueryWrapper.class))).thenReturn(user);
        assertThrows(InvalidLoginException.class, () -> service.login("u","wrong"));
    }

    @Test
    void testLoginEmptyUsername() {
        when(userMapper.selectOne(any(QueryWrapper.class))).thenReturn(null);
        assertThrows(InvalidLoginException.class, () -> service.login("", "p"));
    }
}
