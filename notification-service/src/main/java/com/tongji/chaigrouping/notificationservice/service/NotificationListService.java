package com.tongji.chaigrouping.notificationservice.service;

import com.tongji.chaigrouping.commonutils.dto.NotificationListItemDto;

import java.util.List;

public interface NotificationListService {
    List<NotificationListItemDto> getNotificationList(Integer userId);

    void readAllNotifications(Integer userId);

    void sendTextNotification(Integer userId, String title, String content);

    void sendJoinRequestNotification(Integer userId, String title, String content, Integer joinRequestId);

    void sendTaskNotification(Integer userId, String title, String content, Integer taskId);


}
