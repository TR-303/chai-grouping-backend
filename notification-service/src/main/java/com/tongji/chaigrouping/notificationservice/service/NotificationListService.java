package com.tongji.chaigrouping.notificationservice.service;

import com.tongji.chaigrouping.commonutils.dto.NotificationListItemDto;

import java.util.List;

public interface NotificationListService {
    List<NotificationListItemDto> getNotificationList(Integer userId);

    void readAllNotifications(Integer userId);

    void sendNotification(Integer userId, String content);
}
