package com.tongji.chaigrouping.service;

import com.tongji.chaigrouping.dto.CreateNotificationDto;
import com.tongji.chaigrouping.dto.CreateRequestDto;
import com.tongji.chaigrouping.dto.RespondToRequestDto;
import com.tongji.chaigrouping.entity.Group;
import com.tongji.chaigrouping.entity.JoinRequest;
import com.tongji.chaigrouping.entity.Membership;
import com.tongji.chaigrouping.mapper.*;
import com.tongji.chaigrouping.service.impl.JoinRequestServiceImpl;
import com.tongji.chaigrouping.service.impl.NotificationListServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class JoinRequestServiceImplTest {
    private JoinRequestMapper joinRequestMapper;
    private MembershipMapper membershipMapper;
    private GroupMapper groupMapper;
    private UserMapper userMapper;
    private NotificationListServiceImpl notificationService;
    private NotificationMapper notificationMapper;
    private JoinRequestServiceImpl service;

    @BeforeEach
    void setUp() {
        joinRequestMapper = mock(JoinRequestMapper.class);
        membershipMapper = mock(MembershipMapper.class);
        groupMapper = mock(GroupMapper.class);
        userMapper = mock(UserMapper.class);
        notificationService = mock(NotificationListServiceImpl.class);
        notificationMapper = mock(NotificationMapper.class);
        service = new JoinRequestServiceImpl();
        ReflectionTestUtils.setField(service, "joinRequestMapper", joinRequestMapper);
        ReflectionTestUtils.setField(service, "membershipMapper", membershipMapper);
        ReflectionTestUtils.setField(service, "groupMapper", groupMapper);
        ReflectionTestUtils.setField(service, "userMapper", userMapper);
        ReflectionTestUtils.setField(service, "notificationListServiceImpl", notificationService);
        ReflectionTestUtils.setField(service, "notificationMapper", notificationMapper);
    }

    @Test
    void testCreateRequestDirectJoin() {
        Group g = new Group();
        g.setApprovalRequired(0);
        g.setLeaderId(10);
        g.setName("group");
        g.setVolume(3);
        when(groupMapper.selectById(1)).thenReturn(g);
        when(groupMapper.getMemberCount(1)).thenReturn(2);
        when(userMapper.selectById(2)).thenReturn(new com.tongji.chaigrouping.entity.User());

        CreateRequestDto dto = new CreateRequestDto();
        dto.setDescription("hi");
        service.createRequest(2,1,dto);

        verify(membershipMapper).insert(any(Membership.class));
        verify(notificationService).sendNotification(eq(10), any(CreateNotificationDto.class));
        verify(joinRequestMapper, never()).insert((JoinRequest) any());
    }

    @Test
    void testCreateRequestGroupNotFound() {
        when(groupMapper.selectById(1)).thenReturn(null);
        when(userMapper.selectById(2)).thenReturn(new com.tongji.chaigrouping.entity.User());
        CreateRequestDto dto = new CreateRequestDto();
        assertThrows(NullPointerException.class, () -> service.createRequest(2,1,dto));
    }

    @Test
    void testCreateRequestUserNotFound() {
        Group g = new Group();
        g.setApprovalRequired(0);
        g.setLeaderId(10);
        g.setName("g");
        g.setVolume(1);
        when(groupMapper.selectById(1)).thenReturn(g);
        when(groupMapper.getMemberCount(1)).thenReturn(0);
        when(userMapper.selectById(2)).thenReturn(null);
        CreateRequestDto dto = new CreateRequestDto();
        assertThrows(NullPointerException.class, () -> service.createRequest(2,1,dto));
    }

    @Test
    void testCreateRequestNeedApproval() {
        Group g = new Group();
        g.setApprovalRequired(1);
        g.setLeaderId(10);
        g.setName("group");
        when(groupMapper.selectById(1)).thenReturn(g);
        com.tongji.chaigrouping.entity.User u = new com.tongji.chaigrouping.entity.User();
        u.setUsername("user");
        when(userMapper.selectById(2)).thenReturn(u);

        CreateRequestDto dto = new CreateRequestDto();
        dto.setDescription("hi");
        service.createRequest(2,1,dto);

        verify(joinRequestMapper).insert(any(JoinRequest.class));
        verify(notificationService).sendNotification(eq(10), any(CreateNotificationDto.class));
        verify(membershipMapper, never()).insert(any(Membership.class));
    }

    @Test
    void testCreateRequestGroupFull() {
        Group g = new Group();
        g.setApprovalRequired(0);
        g.setLeaderId(10);
        g.setName("group");
        g.setVolume(1);
        when(groupMapper.selectById(1)).thenReturn(g);
        when(groupMapper.getMemberCount(1)).thenReturn(1);
        com.tongji.chaigrouping.entity.User u = new com.tongji.chaigrouping.entity.User();
        u.setUsername("user");
        when(userMapper.selectById(2)).thenReturn(u);
        CreateRequestDto dto = new CreateRequestDto();
        assertThrows(RuntimeException.class, () -> service.createRequest(2,1,dto));
        verify(membershipMapper, never()).insert((Membership) any());
        verify(joinRequestMapper, never()).insert((JoinRequest) any());
    }

    @Test
    void testRespondToRequestInvalidAction() {
        JoinRequest jr = new JoinRequest(1,2,1,new Date(),"d","PENDING");
        when(joinRequestMapper.selectById(1)).thenReturn(jr);
        RespondToRequestDto dto = new RespondToRequestDto();
        dto.setAction("UNKNOWN");
        assertThrows(RuntimeException.class, () -> service.respondToRequest(10,1,dto));
    }

    @Test
    void testRespondToRequestApprove() {
        JoinRequest jr = new JoinRequest(1,2,1,new Date(),"d","PENDING");
        when(joinRequestMapper.selectById(1)).thenReturn(jr);
        Group g = new Group();
        g.setVolume(3);
        when(groupMapper.getMemberCount(1)).thenReturn(1);
        when(groupMapper.selectById(1)).thenReturn(g);
        RespondToRequestDto dto = new RespondToRequestDto();
        dto.setAction("APPROVE");
        service.respondToRequest(10,1,dto);
        verify(joinRequestMapper).updateById(jr);
        verify(membershipMapper).insert(any(Membership.class));
    }

    @Test
    void testRespondToRequestRequestNotFound() {
        when(joinRequestMapper.selectById(1)).thenReturn(null);
        RespondToRequestDto dto = new RespondToRequestDto();
        dto.setAction("APPROVE");
        assertThrows(NullPointerException.class, () -> service.respondToRequest(10,1,dto));
    }

    @Test
    void testRespondToRequestApproveGroupFull() {
        JoinRequest jr = new JoinRequest(1,2,1,new Date(),"d","PENDING");
        when(joinRequestMapper.selectById(1)).thenReturn(jr);
        Group g = new Group();
        g.setVolume(1);
        when(groupMapper.getMemberCount(1)).thenReturn(1);
        when(groupMapper.selectById(1)).thenReturn(g);
        RespondToRequestDto dto = new RespondToRequestDto();
        dto.setAction("APPROVE");
        assertThrows(RuntimeException.class, () -> service.respondToRequest(10,1,dto));
    }

    @Test
    void testRespondToRequestReject() {
        JoinRequest jr = new JoinRequest(1,2,1,new Date(),"d","PENDING");
        when(joinRequestMapper.selectById(1)).thenReturn(jr);
        RespondToRequestDto dto = new RespondToRequestDto();
        dto.setAction("REJECT");
        service.respondToRequest(10,1,dto);
        verify(joinRequestMapper).updateById(jr);
        verify(membershipMapper, never()).insert((Membership) any());
    }
}
