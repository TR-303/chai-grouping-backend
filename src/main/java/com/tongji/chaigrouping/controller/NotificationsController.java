package com.tongji.chaigrouping.controller;

import com.tongji.chaigrouping.dto.CreateNotificationDto;
import com.tongji.chaigrouping.dto.NotificationListItemDto;
import com.tongji.chaigrouping.service.NotificationDetailService;
import com.tongji.chaigrouping.service.NotificationListService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<NotificationListItemDto>> getNotifications(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("X-User-id");
        return ResponseEntity.ok(notificationListService.getNotificationList(userId));
    }

    @PutMapping("/read-all")
    public ResponseEntity<Object> markAllAsRead(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("X-User-id");
        notificationListService.readAllNotifications(userId);
        return ResponseEntity.ok(Map.of("message", "所有消息被设置为已读，申请已拒绝"));
    }

    @GetMapping("/{notification_id}")
    public ResponseEntity<Object> getNotificationDetail(HttpServletRequest request, @PathVariable("notification_id") Integer notificationId) {
        Integer userId = (Integer) request.getAttribute("X-User-id");
        return ResponseEntity.ok(notificationDetailService.readNotification(notificationId));
    }

    @PostMapping("/send/{receiver_id}")
    public void sendNotification(@PathVariable("receiver_id") Integer receiverId, @RequestBody CreateNotificationDto createNotificationDto) {
        notificationListService.sendNotification(receiverId, createNotificationDto);
    }

}