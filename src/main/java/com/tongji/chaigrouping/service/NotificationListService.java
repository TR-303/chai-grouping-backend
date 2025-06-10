package com.tongji.chaigrouping.service;

import com.tongji.chaigrouping.dto.CreateNotificationDto;
import com.tongji.chaigrouping.dto.NotificationListItemDto;

import java.util.List;

public interface NotificationListService {
    List<NotificationListItemDto> getNotificationList(Integer userId);

    void readAllNotifications(Integer userId);

    void sendNotification(Integer receiverId, CreateNotificationDto createNotificationDto);

}