package com.tongji.chaigrouping.aiservice;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MatchGroupRequest {
    private ResumeItem applicator;
    private List<GroupItem> groups;

    @Data
    @AllArgsConstructor
    public static class GroupItem {
        public Integer group_id;
        public String name;
        public String description;
        public List<ResumeItem> members;
    }

    @Data
    @AllArgsConstructor
    public static class ResumeItem {
        public String school;
        public String grade;
        public String skill_description;
    }

}
