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

}
