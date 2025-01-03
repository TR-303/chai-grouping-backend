package com.tongji.chaigrouping.groupservice.client;

import com.tongji.chaigrouping.commonutils.dto.CreateNotificationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-service")
public interface NotificationServiceClient {
    @PostMapping("/api/notifications/send/{receiver_id}")
    void sendNotification(@PathVariable("receiver_id") Integer receiverId, @RequestBody CreateNotificationDto createNotificationDto);
}
