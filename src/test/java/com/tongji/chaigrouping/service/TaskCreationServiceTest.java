package com.tongji.chaigrouping.service;

import com.tongji.chaigrouping.dto.CreateNotificationDto;
import com.tongji.chaigrouping.dto.TaskCreationDto;
import com.tongji.chaigrouping.entity.Group;
import com.tongji.chaigrouping.entity.Task;
import com.tongji.chaigrouping.mapper.GroupMapper;
import com.tongji.chaigrouping.mapper.TaskMapper;
import com.tongji.chaigrouping.service.impl.TaskCreationServiceImpl;
import com.tongji.chaigrouping.service.impl.NotificationListServiceImpl;

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

    @Test
    public void testCreateTaskWithAssigneeSuccess() {
        // Arrange
        Integer groupId = 1;
        Integer assigneeId = 42;
        String groupName = "测试组";

        TaskCreationDto taskCreationDto = mock(TaskCreationDto.class);
        when(taskCreationDto.getAssigneeId()).thenReturn(assigneeId);

        Group group = new Group();
        group.setName(groupName);
        when(groupMapper.selectById(groupId)).thenReturn(group);

        try (MockedConstruction<Task> mocked = mockConstruction(Task.class, (mockTask, context) -> {
            doNothing().when(mockTask).initTask(groupId, taskCreationDto);
            when(mockTask.getTaskId()).thenReturn(100);
            when(mockTask.getDescription()).thenReturn("写接口文档");
        })) {

            // Act
            Integer returnedTaskId = service.createTask(groupId, taskCreationDto);

            // Assert
            assertEquals(100, returnedTaskId);
            verify(taskMapper).insert(any(Task.class));
            verify(notificationListServiceImpl).sendNotification(eq(assigneeId), any(CreateNotificationDto.class));
        }
    }

    @Test
    public void testCreateTaskWithoutAssigneeNoNotification() {
        // Arrange
        Integer groupId = 2;

        TaskCreationDto taskCreationDto = mock(TaskCreationDto.class);
        when(taskCreationDto.getAssigneeId()).thenReturn(null);

        Group group = new Group();
        group.setName("无分配组");
        when(groupMapper.selectById(groupId)).thenReturn(group);

        try (MockedConstruction<Task> mocked = mockConstruction(Task.class, (mockTask, context) -> {
            doNothing().when(mockTask).initTask(groupId, taskCreationDto);
            when(mockTask.getTaskId()).thenReturn(200);
        })) {

            // Act
            Integer taskId = service.createTask(groupId, taskCreationDto);

            // Assert
            assertEquals(200, taskId);
            verify(taskMapper).insert(any(Task.class));
            verify(notificationListServiceImpl, never()).sendNotification(any(), any());
        }
    }
}
