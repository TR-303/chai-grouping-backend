package com.tongji.chaigrouping.dto;

import lombok.Data;

import java.util.List;

@Data
public class GroupDetailResponseDto {
    private Integer groupId;
    private String name;
    private String description;
    private Integer volume;
    private Integer visibility;
    private Integer approvalRequired;
    private List<GroupMemberBriefDto> members;
}
