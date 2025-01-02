package com.tongji.chaigrouping.commonutils.dto;

import lombok.Data;

import java.util.Date;

@Data
public class UserGroupListDto {
    private Integer groupId;
    private String name;
    private Integer isLeader;
    private Integer memberCount;
    private Integer volume;
    private Date joinDate;
}
