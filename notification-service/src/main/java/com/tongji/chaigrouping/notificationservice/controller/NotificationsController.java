package com.tongji.chaigrouping.notificationservice.controller;

import com.tongji.chaigrouping.commonutils.dto.NotificationListItemDto;
import com.tongji.chaigrouping.notificationservice.service.NotificationDetailService;
import com.tongji.chaigrouping.notificationservice.service.NotificationListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationsController {
    @Autowired
    private NotificationListService notificationListService;

    @Autowired
    private NotificationDetailService notificationDetailService;

    @GetMapping
    public ResponseEntity<List<NotificationListItemDto>> getNotifications(@RequestHeader("X-User-Id") Integer userId) {
        return ResponseEntity.ok(notificationListService.getNotificationList(userId));
    }

    @GetMapping("/read-all")
    public ResponseEntity<Object> markAllAsRead(@RequestHeader("X-User-Id") Integer userId) {
        notificationListService.readAllNotifications(userId);
        return ResponseEntity.ok(Map.of("message", "所有消息被设置为已读，申请已拒绝"));
    }
}
