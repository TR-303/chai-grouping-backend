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

    @TableField(exist = false)
    private User user;

    @TableField(exist = false)
    private Group group;

    @TableField(exist = false)
    private List<Submission> submissions;

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

    public TaskListItemDto toTaskListItemDto() {
        TaskListItemDto taskListItemDto = new TaskListItemDto();
        taskListItemDto.setTaskId(this.taskId);
        taskListItemDto.setUserId(this.userId);
        taskListItemDto.setUsername(this.user == null ? null : this.user.getUsername());
        taskListItemDto.setTitle(this.title);
        taskListItemDto.setDeadline(this.deadline);
        taskListItemDto.setState(this.state);
        taskListItemDto.setSubmissionCnt(this.submissions.size());
        taskListItemDto.setGroupId(this.groupId);
        taskListItemDto.setGroupName(this.group.getName());
        return taskListItemDto;
    }

    public TaskDetailDto toTaskInfoDto() {
        TaskDetailDto taskDetailDto = new TaskDetailDto();
        taskDetailDto.setTaskId(this.taskId);
        taskDetailDto.setGroupId(this.groupId);
        taskDetailDto.setUserId(this.userId);
        taskDetailDto.setTitle(this.title);
        taskDetailDto.setDescription(this.description);
        taskDetailDto.setState(this.state);
        taskDetailDto.setDeadline(this.deadline);
        taskDetailDto.setUsername(this.user == null ? null : this.user.getUsername());
        taskDetailDto.setSubmissions(this.submissions.stream().map(Submission::toSubmissionListItemDto).collect(Collectors.toList()));
        return taskDetailDto;
    }
}
