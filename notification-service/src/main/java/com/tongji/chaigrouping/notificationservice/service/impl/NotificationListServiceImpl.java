package com.tongji.chaigrouping.notificationservice.service.impl;

import com.tongji.chaigrouping.commonutils.dto.CreateNotificationDto;
import com.tongji.chaigrouping.commonutils.dto.NotificationListItemDto;
import com.tongji.chaigrouping.commonutils.entity.JoinRequest;
import com.tongji.chaigrouping.commonutils.entity.Notification;
import com.tongji.chaigrouping.commonutils.mapper.GroupMapper;
import com.tongji.chaigrouping.commonutils.mapper.JoinRequestMapper;
import com.tongji.chaigrouping.commonutils.mapper.NotificationMapper;
import com.tongji.chaigrouping.notificationservice.service.NotificationListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class NotificationListServiceImpl implements NotificationListService {
    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private JoinRequestMapper joinRequestMapper;

    @Autowired
    private GroupMapper groupMapper;


    @Override
    public List<NotificationListItemDto> getNotificationList(Integer userId) {
        return notificationMapper.getNotificationsByUserId(userId);
    }

    @Override
    public void readAllNotifications(Integer userId) {
        List<JoinRequest> joinRequests = notificationMapper.getJoinRequestByReceiverId(userId);
        for (JoinRequest joinRequest : joinRequests) {
            // 如果是等待状态的加入请求，且userId仍然还是队长，直接拒绝
            if (joinRequest.getState().equals("PENDING") && groupMapper.isLeader(joinRequest.getGroupId(), userId)) {
                joinRequest.setState("REJECTED");
                joinRequestMapper.updateById(joinRequest);
            }
        }
        notificationMapper.markAllAsRead(userId);
    }

    /*
    *   receiver     situation
    * 1. 所有组员      组长编辑了小组xx的信息。
    * 2. 被踢走的人    组长把你移出了小组。
    * 3. 成功入队的人   Request通过
    * 4. 申请被拒绝的人  Request被拒绝
    * 5. 所有组员+组长  组解散
    * 6. 任务负责人     任务 更改
    *
    * */
    @Override
    public void sendNotification(Integer receiverId, CreateNotificationDto createNotificationDto) {
        Notification notification = new Notification(null, receiverId, createNotificationDto.getTitle(),
                createNotificationDto.getContent(), createNotificationDto.getJoinRequestId(), 0, new Date());
        notificationMapper.insert(notification);
    }


}
