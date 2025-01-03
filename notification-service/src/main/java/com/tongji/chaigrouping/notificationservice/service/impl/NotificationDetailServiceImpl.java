package com.tongji.chaigrouping.notificationservice.service.impl;

import com.tongji.chaigrouping.commonutils.dto.NotificationDetailDto;
import com.tongji.chaigrouping.commonutils.entity.Notification;
import com.tongji.chaigrouping.commonutils.mapper.NotificationMapper;
import com.tongji.chaigrouping.notificationservice.service.NotificationDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NotificationDetailServiceImpl implements NotificationDetailService {
    @Autowired
    private NotificationMapper notificationMapper;

    @Override
    public NotificationDetailDto readNotification(Integer notificationId) {
        Notification notification = notificationMapper.selectById(notificationId);
        notification.setHasRead(1);
        notificationMapper.updateById(notification);
        return notificationMapper.getNotificationDetailById(notificationId);
    }

}
