package com.tongji.chaigrouping.aiservice;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TaskMatchRequest {
    private String description;
    private List<CandidateItem> candidates;

    @Data
    @AllArgsConstructor
    public static class CandidateItem{
        private Integer user_id;
        private String skill_description;
    }
}
