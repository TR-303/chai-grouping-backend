package com.tongji.chaigrouping.dto;

import lombok.Data;

import java.util.Date;

@Data
public class NotificationDetailDto {
    private Integer notificationId;
    private String title;
    private String content;
    private Date creationTime;
    private Integer read;
    private JoinRequestDto joinRequest;
}
