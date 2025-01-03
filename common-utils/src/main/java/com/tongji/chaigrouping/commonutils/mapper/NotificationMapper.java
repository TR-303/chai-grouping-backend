package com.tongji.chaigrouping.commonutils.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.tongji.chaigrouping.commonutils.dto.NotificationDetailDto;
import com.tongji.chaigrouping.commonutils.dto.NotificationListItemDto;
import com.tongji.chaigrouping.commonutils.entity.JoinRequest;
import com.tongji.chaigrouping.commonutils.entity.Notification;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface NotificationMapper extends BaseMapper<Notification> {
    List<NotificationListItemDto> getNotificationsByUserId(@Param("userId") Integer userId);

    void markAllAsRead(@Param("userId") Integer userId);

    NotificationDetailDto getNotificationDetailById(Integer notificationId);

    List<JoinRequest> getJoinRequestByUserId(Integer UserId);

}