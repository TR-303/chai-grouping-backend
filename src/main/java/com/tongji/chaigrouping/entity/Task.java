package com.tongji.chaigrouping.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.tongji.chaigrouping.dto.TaskCreationDto;
import lombok.Data;

import java.util.Date;

@Data
public class Task {
    @TableId(type = IdType.AUTO)
    private Integer taskId;
    private Integer groupId;
    private Integer userId;
    private String title;
    private String description;
    private String state;
    private Date deadline;

    public void initTask(Integer groupId, TaskCreationDto taskCreationDto) {
        this.groupId = groupId;
        this.title = taskCreationDto.getTitle();
        this.description = taskCreationDto.getDescription();
        this.deadline = taskCreationDto.getDeadline();
        this.userId = taskCreationDto.getAssigneeId();
        this.state = this.userId == null ? "unassigned" : "ongoing";
    }

    public void reassign(Integer userId) {
        this.userId = userId;
        this.state = "ongoing";
    }
}
