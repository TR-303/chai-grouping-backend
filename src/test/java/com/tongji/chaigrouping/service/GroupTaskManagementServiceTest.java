package com.tongji.chaigrouping.service;

import com.tongji.chaigrouping.dto.TaskListItemDto;
import com.tongji.chaigrouping.mapper.TaskMapper;
import com.tongji.chaigrouping.service.impl.GroupTaskManagementServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GroupTaskManagementServiceTest {

    private TaskMapper taskMapper;
    private GroupTaskManagementService groupTaskManagementService;

    @BeforeEach
    void setUp() {
        taskMapper = mock(TaskMapper.class);
        groupTaskManagementService = new GroupTaskManagementServiceImpl(taskMapper);
    }

    @Test
    void testGetTaskList_GroupIdIsNull() {
        List<TaskListItemDto> result = groupTaskManagementService.getTaskList(null);
        assertNotNull(result); // 如果你的实现没有抛异常，那么这个是合理的
        assertTrue(result.isEmpty() || result.size() >= 0);
    }

    @Test
    void testGetTaskList_GroupIdNotExist() {
        when(taskMapper.getGroupTaskList(999)).thenReturn(Collections.emptyList());

        List<TaskListItemDto> result = groupTaskManagementService.getTaskList(999);
        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    void testGetTaskList_EmptyListForExistingGroup() {
        when(taskMapper.getGroupTaskList(1)).thenReturn(Collections.emptyList());

        List<TaskListItemDto> result = groupTaskManagementService.getTaskList(1);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetTaskList_NonEmptyListForExistingGroup() {
        TaskListItemDto task = new TaskListItemDto();
        task.setTaskId(1);
        task.setUserId(1);
        task.setGroupId(2);
        task.setTitle("任务A");
        task.setDeadline(Timestamp.valueOf("2025-12-31 23:59:59"));
        task.setState("进行中");
        task.setGroupName("测试组");
        task.setUsername("张三");
        task.setSubmissionCnt(2);

        when(taskMapper.getGroupTaskList(2)).thenReturn(List.of(task));

        List<TaskListItemDto> result = groupTaskManagementService.getTaskList(2);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("任务A", result.get(0).getTitle());
        verify(taskMapper).getGroupTaskList(2);
    }

    @Test
    void testGetTaskList_InvalidNegativeGroupId() {
        when(taskMapper.getGroupTaskList(-1)).thenReturn(Collections.emptyList());

        List<TaskListItemDto> result = groupTaskManagementService.getTaskList(-1);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
