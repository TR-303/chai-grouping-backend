package com.tongji.chaigrouping.dto;

import lombok.Data;

@Data
public class GroupMemberDetailDto {
    Integer userId;
    String username;
    String role;
    String joinDate;
    String school;
    String grade;
    String skillDescription;
}
