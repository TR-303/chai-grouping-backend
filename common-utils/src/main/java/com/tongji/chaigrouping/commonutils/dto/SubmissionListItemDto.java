package com.tongji.chaigrouping.commonutils.dto;

import lombok.Data;

import java.util.Date;

@Data
public class SubmissionListItemDto {
    private Integer submissionId;
    private Integer userId;
    private String username;
    private Date creationDate;
    private String text;
    private String fileName;
}
