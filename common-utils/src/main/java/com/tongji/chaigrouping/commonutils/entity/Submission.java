package com.tongji.chaigrouping.commonutils.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.tongji.chaigrouping.commonutils.dto.SubmissionListItemDto;
import lombok.Data;

import java.util.Date;

@Data
public class Submission {
    @TableId(type = IdType.AUTO)
    private Integer submissionId;
    private Integer taskId;
    private Integer userId;
    private Date creationDate;
    private String text;
    private String fileName;
    private String filePath;

    @TableField(exist = false)
    private User user;

    public SubmissionListItemDto toSubmissionListItemDto() {
        SubmissionListItemDto dto = new SubmissionListItemDto();
        dto.setSubmissionId(this.submissionId);
        dto.setUserId(this.userId);
        dto.setCreationDate(this.creationDate);
        dto.setText(this.text);
        dto.setFileName(this.fileName);
        dto.setUsername(this.user.getUsername());
        return dto;
    }
}
