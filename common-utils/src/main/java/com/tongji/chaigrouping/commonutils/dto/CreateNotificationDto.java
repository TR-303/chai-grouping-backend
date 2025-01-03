package com.tongji.chaigrouping.commonutils.dto;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class CreateNotificationDto {
    private String title;
    private String content;
    private Integer joinRequestId;
}
