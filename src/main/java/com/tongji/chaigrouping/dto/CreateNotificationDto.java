package com.tongji.chaigrouping.dto;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class CreateNotificationDto {
    private String title;
    private String content;
    private Integer joinRequestId;
}
