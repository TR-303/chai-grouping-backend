package com.tongji.chaigrouping.commonutils.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.tongji.chaigrouping.commonutils.dto.TaskCreationDto;
import com.tongji.chaigrouping.commonutils.dto.TaskDetailDto;
import com.tongji.chaigrouping.commonutils.dto.TaskListItemDto;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

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
