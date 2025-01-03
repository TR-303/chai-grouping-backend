package com.tongji.chaigrouping.commonutils.dto;

import com.tongji.chaigrouping.commonutils.entity.JoinRequest;
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
