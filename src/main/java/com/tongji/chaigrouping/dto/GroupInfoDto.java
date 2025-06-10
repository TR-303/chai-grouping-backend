package com.tongji.chaigrouping.dto;

import lombok.Data;

@Data
public class GroupInfoDto {
    String name;
    String description;
    Integer volume;
    Integer visibility;
    Integer approvalRequired;
}
