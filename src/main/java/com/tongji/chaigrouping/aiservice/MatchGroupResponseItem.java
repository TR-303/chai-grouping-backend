package com.tongji.chaigrouping.aiservice;

import lombok.Data;

@Data
public class MatchGroupResponseItem {
    private Integer group_id;
    private String reason;
    private Double rating;
}
