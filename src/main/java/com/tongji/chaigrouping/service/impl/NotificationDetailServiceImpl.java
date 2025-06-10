package com.tongji.chaigrouping.service.impl;

import com.tongji.chaigrouping.dto.NotificationDetailDto;
import com.tongji.chaigrouping.entity.Notification;
import com.tongji.chaigrouping.mapper.NotificationMapper;
import com.tongji.chaigrouping.service.NotificationDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationDetailServiceImpl implements NotificationDetailService {
    @Autowired
    private NotificationMapper notificationMapper;

    @Override
    public NotificationDetailDto readNotification(Integer notificationId) {
        Notification notification = notificationMapper.selectById(notificationId);
        if(notification.getJoinRequestId() == null)
            notification.setHasRead(1);
        notificationMapper.updateById(notification);
        return notificationMapper.getNotificationDetailById(notificationId);
    }
}