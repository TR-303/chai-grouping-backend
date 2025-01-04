package com.tongji.chaigrouping.commonutils.dto;

import lombok.Data;

import java.util.Date;

@Data
public class TaskListItemDto {
    private Integer taskId;
    private Integer userId;
    private String username;
    private String title;
    private Date deadline;
    private String state;
    private Integer submissionCnt;
    private Integer groupId;
    private String groupName;
    private String description;
}
