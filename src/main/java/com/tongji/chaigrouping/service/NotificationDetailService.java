package com.tongji.chaigrouping.service;

import com.tongji.chaigrouping.dto.NotificationDetailDto;

public interface NotificationDetailService {
    NotificationDetailDto readNotification(Integer notificationId);
}