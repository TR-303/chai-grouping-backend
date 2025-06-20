package com.tongji.chaigrouping.service;

import com.tongji.chaigrouping.dto.CreateNotificationDto;
import com.tongji.chaigrouping.dto.TaskCreationDto;
import com.tongji.chaigrouping.entity.Group;
import com.tongji.chaigrouping.entity.Task;
import com.tongji.chaigrouping.mapper.GroupMapper;
import com.tongji.chaigrouping.mapper.TaskMapper;
import com.tongji.chaigrouping.service.impl.NotificationListServiceImpl;
import com.tongji.chaigrouping.service.impl.TaskCreationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class TaskCreationServiceTest {

    @InjectMocks
    private TaskCreationServiceImpl service;

    @Mock
    private TaskMapper taskMapper;

    @Mock
    private GroupMapper groupMapper;

    @Mock
    private NotificationListServiceImpl notificationListServiceImpl;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // UT_TC_002_001_001 - groupId 为 null
    @Test
    public void testCreateTask_GroupIdNull_Variant1() {
        TaskCreationDto dto = mock(TaskCreationDto.class);
        when(dto.getDescription()).thenReturn("测试任务");
        assertThrows(IllegalArgumentException.class, () -> service.createTask(null, dto));
    }

    @Test
    public void testCreateTask_GroupIdNull_Variant2() {
        assertThrows(IllegalArgumentException.class, () -> service.createTask(null, null));
    }

    @Test
    public void testCreateTask_GroupIdNull_Variant3() {
        TaskCreationDto dto = mock(TaskCreationDto.class);
        when(dto.getAssigneeId()).thenReturn(10);
        assertThrows(IllegalArgumentException.class, () -> service.createTask(null, dto));
    }

    // UT_TC_002_001_002 - 有效 groupId，不分配 assignee
    @Test
    public void testCreateTask_ValidGroupId_NoAssignee_DescriptionNull() {
        Integer groupId = 1;
        Group group = new Group();
        group.setName("组A");
        when(groupMapper.selectById(groupId)).thenReturn(group);

        TaskCreationDto dto = mock(TaskCreationDto.class);
        when(dto.getAssigneeId()).thenReturn(null);

        try (MockedConstruction<Task> mocked = mockConstruction(Task.class, (mockTask, context) -> {
            doNothing().when(mockTask).initTask(groupId, dto);
            when(mockTask.getTaskId()).thenReturn(301);
        })) {
            Integer taskId = service.createTask(groupId, dto);
            assertEquals(301, taskId);
            verify(taskMapper).insert(any(Task.class));
            verify(notificationListServiceImpl, never()).sendNotification(any(), any());
        }
    }

    @Test
    public void testCreateTask_ValidGroupId_NoAssignee_EmptyDescription() {
        Integer groupId = 1;
        Group group = new Group();
        group.setName("组A");
        when(groupMapper.selectById(groupId)).thenReturn(group);

        TaskCreationDto dto = mock(TaskCreationDto.class);
        when(dto.getAssigneeId()).thenReturn(null);
        when(dto.getDescription()).thenReturn("");

        try (MockedConstruction<Task> mocked = mockConstruction(Task.class, (mockTask, context) -> {
            doNothing().when(mockTask).initTask(groupId, dto);
            when(mockTask.getTaskId()).thenReturn(302);
        })) {
            Integer taskId = service.createTask(groupId, dto);
            assertEquals(302, taskId);
        }
    }

    @Test
    public void testCreateTask_ValidGroupId_NoAssignee_LongDescription() {
        Integer groupId = 1;
        Group group = new Group();
        group.setName("组A");
        when(groupMapper.selectById(groupId)).thenReturn(group);

        TaskCreationDto dto = mock(TaskCreationDto.class);
        when(dto.getAssigneeId()).thenReturn(null);
        when(dto.getDescription()).thenReturn("A".repeat(255));

        try (MockedConstruction<Task> mocked = mockConstruction(Task.class, (mockTask, context) -> {
            doNothing().when(mockTask).initTask(groupId, dto);
            when(mockTask.getTaskId()).thenReturn(303);
        })) {
            Integer taskId = service.createTask(groupId, dto);
            assertEquals(303, taskId);
        }
    }

    // UT_TC_002_001_003 - 有效 groupId + assignee 存在
    @Test
    public void testCreateTask_WithAssignee_OnlyId() {
        Integer groupId = 1;
        Integer assigneeId = 10;
        Group group = new Group();
        group.setName("组A");
        when(groupMapper.selectById(groupId)).thenReturn(group);

        TaskCreationDto dto = mock(TaskCreationDto.class);
        when(dto.getAssigneeId()).thenReturn(assigneeId);

        try (MockedConstruction<Task> mocked = mockConstruction(Task.class, (mockTask, context) -> {
            doNothing().when(mockTask).initTask(groupId, dto);
            when(mockTask.getTaskId()).thenReturn(401);
        })) {
            Integer taskId = service.createTask(groupId, dto);
            assertEquals(401, taskId);
            verify(notificationListServiceImpl).sendNotification(eq(assigneeId), any(CreateNotificationDto.class));
        }
    }

    @Test
    public void testCreateTask_WithAssignee_AndDescription() {
        Integer groupId = 1;
        Integer assigneeId = 10;
        Group group = new Group();
        group.setName("组A");
        when(groupMapper.selectById(groupId)).thenReturn(group);

        TaskCreationDto dto = mock(TaskCreationDto.class);
        when(dto.getAssigneeId()).thenReturn(assigneeId);
        when(dto.getDescription()).thenReturn("紧急");

        try (MockedConstruction<Task> mocked = mockConstruction(Task.class, (mockTask, context) -> {
            doNothing().when(mockTask).initTask(groupId, dto);
            when(mockTask.getTaskId()).thenReturn(402);
        })) {
            Integer taskId = service.createTask(groupId, dto);
            assertEquals(402, taskId);
        }
    }

    @Test
    public void testCreateTask_WithAssignee_AndDueDate() {
        Integer groupId = 1;
        Integer assigneeId = 10;
        Group group = new Group();
        group.setName("组A");
        when(groupMapper.selectById(groupId)).thenReturn(group);

        TaskCreationDto dto = mock(TaskCreationDto.class);
        when(dto.getAssigneeId()).thenReturn(assigneeId);
        // assume other fields like dueDate handled internally, mock as needed

        try (MockedConstruction<Task> mocked = mockConstruction(Task.class, (mockTask, context) -> {
            doNothing().when(mockTask).initTask(groupId, dto);
            when(mockTask.getTaskId()).thenReturn(403);
        })) {
            Integer taskId = service.createTask(groupId, dto);
            assertEquals(403, taskId);
        }
    }

    // UT_TC_002_001_004 - 无效 groupId
    @Test
    public void testCreateTask_InvalidGroupId_NotFound_999() {
        Integer groupId = 999;
        when(groupMapper.selectById(groupId)).thenReturn(null);

        TaskCreationDto dto = mock(TaskCreationDto.class);
        when(dto.getAssigneeId()).thenReturn(10);

        assertThrows(IllegalArgumentException.class, () -> service.createTask(groupId, dto));
    }

    @Test
    public void testCreateTask_InvalidGroupId_Zero() {
        Integer groupId = 0;
        when(groupMapper.selectById(groupId)).thenReturn(null);

        TaskCreationDto dto = mock(TaskCreationDto.class);
        when(dto.getAssigneeId()).thenReturn(10);

        assertThrows(IllegalArgumentException.class, () -> service.createTask(groupId, dto));
    }

    @Test
    public void testCreateTask_InvalidGroupId_Negative() {
        Integer groupId = -1;
        when(groupMapper.selectById(groupId)).thenReturn(null);

        TaskCreationDto dto = mock(TaskCreationDto.class);
        when(dto.getAssigneeId()).thenReturn(10);

        assertThrows(IllegalArgumentException.class, () -> service.createTask(groupId, dto));
    }
}
