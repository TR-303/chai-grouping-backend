package com.tongji.chaigrouping.notificationservice.service;

import com.tongji.chaigrouping.commonutils.dto.NotificationDetailDto;

public interface NotificationDetailService {
    NotificationDetailDto readNotification(Integer notificationId);
}
