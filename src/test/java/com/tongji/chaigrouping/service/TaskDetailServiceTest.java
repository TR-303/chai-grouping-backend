package com.tongji.chaigrouping.service;

import com.tongji.chaigrouping.dto.TaskDetailDto;
import com.tongji.chaigrouping.dto.SubmissionListItemDto;
import com.tongji.chaigrouping.mapper.TaskMapper;
import com.tongji.chaigrouping.service.impl.GroupTaskManagementServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Date;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class TaskDetailServiceTest {  // 修改了类名

    @Mock
    private TaskMapper taskMapper;

    private GroupTaskManagementService groupTaskManagementService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        groupTaskManagementService = new GroupTaskManagementServiceImpl(taskMapper);
    }

    // UT_TC_003_005_001: 输入taskId为null
    @Test
    void testGetTaskDetail_TaskIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> groupTaskManagementService.getTaskDetail(null));
    }

    // UT_TC_003_005_002: 输入非正整数taskId
    @Test
    void testGetTaskDetail_InvalidTaskId() {
        assertThrows(IllegalArgumentException.class, () -> {
            groupTaskManagementService.getTaskDetail(-1);
        });
    }

    // UT_TC_003_005_003: 输入taskId不存在
    @Test
    void testGetTaskDetail_TaskIdDoesNotExist() {
        when(taskMapper.getTaskDetailById(999)).thenReturn(null);

        TaskDetailDto result = groupTaskManagementService.getTaskDetail(999);
        assertNull(result);
    }

    // UT_TC_003_005_004: 输入taskId存在但无提交记录
    @Test
    void testGetTaskDetail_NoSubmission() {
        TaskDetailDto taskDetailDto = new TaskDetailDto();
        taskDetailDto.setTaskId(1);
        taskDetailDto.setSubmissions(null);  // 无提交记录

        when(taskMapper.getTaskDetailById(1)).thenReturn(taskDetailDto);

        TaskDetailDto result = groupTaskManagementService.getTaskDetail(1);
        assertNotNull(result);
        assertNull(result.getSubmissions());  // 验证提交相关字段为null
    }

    // UT_TC_003_005_005: 输入taskId存在且有提交记录
    @Test
    void testGetTaskDetail_WithSubmission() {
        SubmissionListItemDto submission1 = new SubmissionListItemDto();
        submission1.setSubmissionId(101);
        submission1.setFileName("file.txt");
        submission1.setText("Sample Submission");
        submission1.setUsername("john_doe");

        List<SubmissionListItemDto> submissions = Arrays.asList(submission1);

        TaskDetailDto taskDetailDto = new TaskDetailDto();
        taskDetailDto.setTaskId(2);
        taskDetailDto.setSubmissions(submissions);  // 设置提交记录

        when(taskMapper.getTaskDetailById(2)).thenReturn(taskDetailDto);

        TaskDetailDto result = groupTaskManagementService.getTaskDetail(2);
        assertNotNull(result);
        assertEquals(1, result.getSubmissions().size());  // 验证返回的提交记录大小
        assertEquals("file.txt", result.getSubmissions().get(0).getFileName());
        assertEquals("Sample Submission", result.getSubmissions().get(0).getText());
        assertEquals("john_doe", result.getSubmissions().get(0).getUsername());
    }

    // UT_TC_003_005_006: 验证返回的任务基本信息
    @Test
    void testGetTaskDetail_TaskBasicInfo() {
        TaskDetailDto taskDetailDto = new TaskDetailDto();
        taskDetailDto.setTaskId(3);
        taskDetailDto.setTitle("Task Title");
        taskDetailDto.setDescription("Task Description");
        long timestamp = 1758324600000L; // 假设这是目标时间戳
        Date date = new Date(timestamp);
        taskDetailDto.setDeadline(date);
        taskDetailDto.setState("In Progress");

        when(taskMapper.getTaskDetailById(3)).thenReturn(taskDetailDto);

        TaskDetailDto result = groupTaskManagementService.getTaskDetail(3);
        assertNotNull(result);
        assertEquals("Task Title", result.getTitle());
        assertEquals("Task Description", result.getDescription());
        assertEquals(date, result.getDeadline());
        assertEquals("In Progress", result.getState());
    }

    // UT_TC_003_005_007: 验证返回的任务小组信息
    @Test
    void testGetTaskDetail_GroupInfo() {
        TaskDetailDto taskDetailDto = new TaskDetailDto();
        taskDetailDto.setTaskId(4);
        taskDetailDto.setGroupId(10);
        taskDetailDto.setGroupName("Group A");

        when(taskMapper.getTaskDetailById(4)).thenReturn(taskDetailDto);

        TaskDetailDto result = groupTaskManagementService.getTaskDetail(4);
        assertNotNull(result);
        assertEquals(10, result.getGroupId());
        assertEquals("Group A", result.getGroupName());
    }

    // UT_TC_003_005_008: 验证返回的用户信息
    @Test
    void testGetTaskDetail_UserInfo() {
        TaskDetailDto taskDetailDto = new TaskDetailDto();
        taskDetailDto.setTaskId(5);
        taskDetailDto.setUserId(20);
        taskDetailDto.setUsername("alice");

        when(taskMapper.getTaskDetailById(5)).thenReturn(taskDetailDto);

        TaskDetailDto result = groupTaskManagementService.getTaskDetail(5);
        assertNotNull(result);
        assertEquals(20, result.getUserId());
        assertEquals("alice", result.getUsername());
    }

    // UT_TC_003_005_009: 验证返回的提交信息
    @Test
    void testGetTaskDetail_SubmissionInfo() {
        SubmissionListItemDto submission1 = new SubmissionListItemDto();
        submission1.setSubmissionId(102);
        submission1.setFileName("file2.txt");
        submission1.setText("Another submission");
        submission1.setUsername("bob");

        List<SubmissionListItemDto> submissions = Arrays.asList(submission1);

        TaskDetailDto taskDetailDto = new TaskDetailDto();
        taskDetailDto.setTaskId(6);
        taskDetailDto.setSubmissions(submissions);  // 设置提交记录

        when(taskMapper.getTaskDetailById(6)).thenReturn(taskDetailDto);

        TaskDetailDto result = groupTaskManagementService.getTaskDetail(6);
        assertNotNull(result);
        assertEquals(1, result.getSubmissions().size());  // 验证返回的提交记录大小
        assertEquals("file2.txt", result.getSubmissions().get(0).getFileName());
        assertEquals("Another submission", result.getSubmissions().get(0).getText());
        assertEquals("bob", result.getSubmissions().get(0).getUsername());
    }

    // UT_TC_003_005_010: 验证无提交记录时相关字段为null
    @Test
    void testGetTaskDetail_NoSubmissionInfo() {
        TaskDetailDto taskDetailDto = new TaskDetailDto();
        taskDetailDto.setTaskId(7);
        taskDetailDto.setSubmissions(null);  // 无提交记录

        when(taskMapper.getTaskDetailById(7)).thenReturn(taskDetailDto);

        TaskDetailDto result = groupTaskManagementService.getTaskDetail(7);
        assertNotNull(result);
        assertNull(result.getSubmissions());  // 验证提交相关字段为null
    }
}
