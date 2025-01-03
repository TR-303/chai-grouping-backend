package com.tongji.chaigrouping.commonutils.dto;

import lombok.Data;

import java.util.Date;

@Data
public class GroupListDto {
    Integer groupId;
    String name;
    String description;
    Integer currentMembers;
    Integer volume;
    Date createdAt;
    Integer approvalRequired;
}
