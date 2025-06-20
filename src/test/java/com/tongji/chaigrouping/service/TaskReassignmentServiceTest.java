package com.tongji.chaigrouping.service;

import com.tongji.chaigrouping.entity.Task;
import com.tongji.chaigrouping.mapper.TaskMapper;
import com.tongji.chaigrouping.service.impl.GroupTaskManagementServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;

public class TaskReassignmentServiceTest {

    @Mock
    private TaskMapper taskMapper;

    private GroupTaskManagementService groupTaskManagementService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        groupTaskManagementService = new GroupTaskManagementServiceImpl(taskMapper);
    }

    // UT_TC_003_004_001: 输入taskId为null
    @Test
    void testReassignTask_TaskIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> groupTaskManagementService.reassignTask(null, 1));
    }

    // UT_TC_003_004_002: 输入assigneeId为null
    @Test
    void testReassignTask_AssigneeIdIsNull() {
        assertThrows(IllegalArgumentException.class, () -> groupTaskManagementService.reassignTask(1, null));
    }

    // UT_TC_003_004_003: 输入taskId在数据库中不存在
    @Test
    void testReassignTask_TaskIdDoesNotExist() {
        when(taskMapper.selectById(999)).thenReturn(null);  // 确保返回null表示任务不存在

        assertThrows(IllegalArgumentException.class, () -> groupTaskManagementService.reassignTask(999, 1));
    }

    // UT_TC_003_004_004: 输入assigneeId在数据库中不存在
    @Test
    void testReassignTask_AssigneeIdDoesNotExist() {
        Task task = new Task();
        task.setTaskId(1);
        when(taskMapper.selectById(1)).thenReturn(task);  // 模拟任务存在
        when(taskMapper.selectById(999)).thenReturn(null);  // 模拟assigneeId不存在

        assertThrows(IllegalArgumentException.class, () -> groupTaskManagementService.reassignTask(1, 999));
    }

    // UT_TC_003_004_005: 输入taskId和assigneeId均存在
    @Test
    void testReassignTask_ValidTaskIdAndAssigneeId() {
        Task task = new Task();
        task.setTaskId(1);
        task.setUserId(2);  // 设置任务分配人
        task.setState("未完成");
        when(taskMapper.selectById(1)).thenReturn(task);  // 模拟返回任务

        groupTaskManagementService.reassignTask(1, 2);

        verify(taskMapper, times(1)).updateById(task);  // 验证updateById是否被调用
    }

    // UT_TC_003_004_006: 输入taskId或assigneeId为非法值
    @Test
    void testReassignTask_InvalidTaskIdAndAssigneeId() {
        assertThrows(IllegalArgumentException.class, () -> groupTaskManagementService.reassignTask(-1, 2));
        assertThrows(IllegalArgumentException.class, () -> groupTaskManagementService.reassignTask(1, -2));
    }

    // UT_TC_003_004_007: 输入taskId存在但任务状态不允许重新分配
    @Test
    void testReassignTask_TaskStatusNotAllowReassign() {
        Task task = new Task();
        task.setTaskId(1);
        task.setState("已完成");  // 设置状态为“已完成”
        when(taskMapper.selectById(1)).thenReturn(task);  // 模拟返回任务

        assertThrows(IllegalStateException.class, () -> groupTaskManagementService.reassignTask(1, 2));  // 验证任务状态不允许重新分配
    }
}
