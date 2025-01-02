package com.tongji.chaigrouping.commonutils.dto;

import lombok.Data;

import java.util.Date;

@Data
public class TaskCreationDto {
    private String title;
    private String description;
    private Date deadline;
    private Integer assigneeId;
}
