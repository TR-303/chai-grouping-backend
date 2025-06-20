package com.tongji.chaigrouping.service;

import com.tongji.chaigrouping.dto.ResumeDto;
import com.tongji.chaigrouping.entity.User;
import com.tongji.chaigrouping.exception.InvalidUserException;
import com.tongji.chaigrouping.mapper.UserMapper;
import com.tongji.chaigrouping.service.impl.ResumeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ResumeServiceImplTest {
    private UserMapper userMapper;
    private ResumeServiceImpl service;

    @BeforeEach
    void setUp() {
        userMapper = mock(UserMapper.class);
        service = new ResumeServiceImpl();
        ReflectionTestUtils.setField(service, "userMapper", userMapper);
    }

    @Test
    void testGetResumeNotFound() {
        when(userMapper.selectById(1)).thenReturn(null);
        assertThrows(InvalidUserException.class, () -> service.getResume(1));
    }

    @Test
    void testGetResumeSuccess() {
        User user = new User();
        ResumeDto dto = new ResumeDto();
        dto.setSchool("A");
        user.setResume(dto);
        when(userMapper.selectById(1)).thenReturn(user);
        ResumeDto result = service.getResume(1);
        assertEquals("A", result.getSchool());
    }

    @Test
    void testUpdateResumeSuccess() {
        User user = new User();
        when(userMapper.selectById(1)).thenReturn(user);
        ResumeDto dto = new ResumeDto();
        service.updateResume(1,dto);
        verify(userMapper).updateById(user);
    }

    @Test
    void testUpdateResumeNotFound() {
        when(userMapper.selectById(1)).thenReturn(null);
        ResumeDto dto = new ResumeDto();
        assertThrows(InvalidUserException.class, () -> service.updateResume(1, dto));
    }
}
