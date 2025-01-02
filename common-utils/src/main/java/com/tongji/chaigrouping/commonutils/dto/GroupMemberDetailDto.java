package com.tongji.chaigrouping.commonutils.dto;

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
    /*  "user_id": 1,
  "username": "Alice",
  "role": "leader"/"member",
  "join_date": "2024-12-01",
  "school": "Tongji Univ.",
  "grade": "Freshman",
  "skillDescription": "Singing, dancing, rapping and basketball",*/
}
