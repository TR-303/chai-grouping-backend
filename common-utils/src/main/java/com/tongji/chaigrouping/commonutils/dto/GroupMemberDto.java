package com.tongji.chaigrouping.commonutils.dto;

import lombok.Data;

@Data
public class GroupMemberDto {
    Integer userId;
    String username;
    Integer isLeader = 0;
}
