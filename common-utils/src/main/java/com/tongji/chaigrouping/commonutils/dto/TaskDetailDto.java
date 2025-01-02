package com.tongji.chaigrouping.commonutils.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class TaskDetailDto {
    private Integer taskId;
    private Integer groupId;
    private Integer userId;
    private String username;
    private String title;
    private String description;
    private String state;
    private Date deadline;
    private List<SubmissionListItemDto> submissions;
}
