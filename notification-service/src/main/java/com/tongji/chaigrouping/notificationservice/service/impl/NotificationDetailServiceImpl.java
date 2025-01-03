package com.tongji.chaigrouping.notificationservice.service.impl;

import com.tongji.chaigrouping.commonutils.dto.NotificationDetailDto;
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
        return notificationMapper.getNotificationDetailById(notificationId);
    }

}
