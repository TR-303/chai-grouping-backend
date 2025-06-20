package com.tongji.chaigrouping.service;

import com.tongji.chaigrouping.dto.CreateNotificationDto;
import com.tongji.chaigrouping.dto.CreateRequestDto;
import com.tongji.chaigrouping.dto.RespondToRequestDto;
import com.tongji.chaigrouping.entity.Group;
import com.tongji.chaigrouping.entity.JoinRequest;
import com.tongji.chaigrouping.entity.Membership;
import com.tongji.chaigrouping.mapper.GroupMapper;
import com.tongji.chaigrouping.mapper.JoinRequestMapper;
import com.tongji.chaigrouping.mapper.MembershipMapper;
import com.tongji.chaigrouping.mapper.UserMapper;
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
    private JoinRequestServiceImpl service;

    @BeforeEach
    void setUp() {
        joinRequestMapper = mock(JoinRequestMapper.class);
        membershipMapper = mock(MembershipMapper.class);
        groupMapper = mock(GroupMapper.class);
        userMapper = mock(UserMapper.class);
        notificationService = mock(NotificationListServiceImpl.class);
        service = new JoinRequestServiceImpl();
        ReflectionTestUtils.setField(service, "joinRequestMapper", joinRequestMapper);
        ReflectionTestUtils.setField(service, "membershipMapper", membershipMapper);
        ReflectionTestUtils.setField(service, "groupMapper", groupMapper);
        ReflectionTestUtils.setField(service, "userMapper", userMapper);
        ReflectionTestUtils.setField(service, "notificationListServiceImpl", notificationService);
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
    void testRespondToRequestInvalidAction() {
        JoinRequest jr = new JoinRequest(1,2,1,new Date(),"d","PENDING");
        when(joinRequestMapper.selectById(1)).thenReturn(jr);
        RespondToRequestDto dto = new RespondToRequestDto();
        dto.setAction("UNKNOWN");
        assertThrows(RuntimeException.class, () -> service.respondToRequest(10,1,dto));
    }
}
