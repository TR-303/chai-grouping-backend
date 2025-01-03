package com.tongji.chaigrouping.notificationservice.service.impl;

import com.tongji.chaigrouping.commonutils.dto.NotificationListItemDto;
import com.tongji.chaigrouping.commonutils.entity.JoinRequest;
import com.tongji.chaigrouping.commonutils.mapper.JoinRequestMapper;
import com.tongji.chaigrouping.commonutils.mapper.NotificationMapper;
import com.tongji.chaigrouping.notificationservice.service.NotificationListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationListServiceDto implements NotificationListService {
    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private JoinRequestMapper joinRequestMapper;

    @Override
    public List<NotificationListItemDto> getNotificationList(Integer userId) {
        return notificationMapper.getNotificationsByUserId(userId);
    }

    @Override
    public void readAllNotifications(Integer userId) {
        List<JoinRequest> joinRequests = notificationMapper.getJoinRequestByUserId(userId);
        for (JoinRequest joinRequest : joinRequests) {
            joinRequest.setState("REJECTED");
            joinRequestMapper.updateById(joinRequest);
        }
        notificationMapper.markAllAsRead(userId);
    }

    @Override
    public void sendNotification(Integer userId, String content) {

    }

}
