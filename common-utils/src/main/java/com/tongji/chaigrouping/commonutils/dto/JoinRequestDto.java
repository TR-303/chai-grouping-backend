package com.tongji.chaigrouping.commonutils.dto;

import lombok.Data;

import java.util.Date;

@Data
public class JoinRequestDto {
    private Integer joinRequestId;
    private Integer requesterId;
    private String requesterName;
    private Integer groupId;
    private String groupName;
    private String description;
    private Date creationTime;
    private String status;
}
