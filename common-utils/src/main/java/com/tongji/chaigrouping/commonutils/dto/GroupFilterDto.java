package com.tongji.chaigrouping.commonutils.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class GroupFilterDto {
    private String keyword;
    private Integer maxVolume;
    private Integer minVolume;
    private Date createdBefore;
    private Date createdAfter;
}
