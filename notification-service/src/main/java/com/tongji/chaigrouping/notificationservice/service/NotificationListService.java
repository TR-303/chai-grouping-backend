package com.tongji.chaigrouping.notificationservice.service;

import com.tongji.chaigrouping.commonutils.entity.Notification;

import java.util.List;

public interface NotificationListService {
    List<Notification> getNotificationList(Integer userId);
    void readAllNotifications(Integer userId);
}
