package com.tongji.chaigrouping.commonutils.dto;

import lombok.Data;

@Data
public class GroupInfoDto {
    String name;
    String description;
    Integer volume;
    Integer visibility;
    Integer approvalRequired;
}
