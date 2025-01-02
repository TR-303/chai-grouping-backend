package com.tongji.chaigrouping.commonutils.dto;

import lombok.Data;

@Data
public class GroupMemberBriefDto {
    Integer userId;
    String username;
    Integer isLeader = 0;
}
