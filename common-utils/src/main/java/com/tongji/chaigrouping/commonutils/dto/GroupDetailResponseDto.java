package com.tongji.chaigrouping.commonutils.dto;

import lombok.Data;

import java.util.List;

@Data
public class GroupDetailResponseDto {
    private Integer groupId;
    private String name;
    private String description;
    private Integer volume;
    private Integer visibility;
    private List<GroupMemberBriefDto> members;
}
