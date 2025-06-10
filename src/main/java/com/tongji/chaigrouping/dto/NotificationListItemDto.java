package com.tongji.chaigrouping.dto;

import lombok.Data;

import java.util.Date;

@Data
public class NotificationListItemDto {
    private Integer notificationId;
    private String title;
    private Date createTime;
    private Integer read;
}
