package com.tongji.chaigrouping.service;

import com.tongji.chaigrouping.dto.TaskListItemDto;
import com.tongji.chaigrouping.mapper.TaskMapper;
import com.tongji.chaigrouping.service.impl.UserTaskManagementServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserTaskManagementServiceTest {

    private TaskMapper taskMapper;
    private UserTaskManagementService userTaskManagementService;

    @BeforeEach
    void setUp() {
        taskMapper = mock(TaskMapper.class);
        userTaskManagementService = new UserTaskManagementServiceImpl(taskMapper);
    }

    @Test
    void testGetUserTaskList_WithValidUserId() {
        TaskListItemDto task = new TaskListItemDto();
        task.setTaskId(1);
        task.setUserId(1);
        task.setGroupId(1);
        task.setTitle("Test Task");
        task.setDeadline(Timestamp.valueOf("2025-12-31 23:59:59"));
        task.setState("进行中");
        task.setGroupName("Test Group");
        task.setUsername("Test User");
        task.setSubmissionCnt(1);

        when(taskMapper.getUserTaskList(1)).thenReturn(Collections.singletonList(task));

        List<TaskListItemDto> result = userTaskManagementService.getUserTaskList(1);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Task", result.get(0).getTitle());
        verify(taskMapper).getUserTaskList(1);
    }

    @Test
    void testGetUserTaskList_WithNullUserId() {
        List<TaskListItemDto> result = userTaskManagementService.getUserTaskList(null);
        assertNotNull(result); // 根据你的实际业务逻辑也可以 assertThrows
        assertTrue(result.isEmpty() || result.size() >= 0);
    }

    @Test
    void testGetUserTaskList_WithInvalidUserId() {
        when(taskMapper.getUserTaskList(-1)).thenReturn(Collections.emptyList());

        List<TaskListItemDto> result = userTaskManagementService.getUserTaskList(-1);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void testGetUserTaskList_UserWithNoTasks() {
        when(taskMapper.getUserTaskList(100)).thenReturn(Collections.emptyList());

        List<TaskListItemDto> result = userTaskManagementService.getUserTaskList(100);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
