package com.tongji.chaigrouping.commonutils.entity;

import com.tongji.chaigrouping.commonutils.dto.SubmissionListItemDto;
import lombok.Data;

import java.util.Date;

@Data
public class Submission {
    private Integer submissionId;
    private Integer taskId;
    private Integer userId;
    private Date creation_date;
    private String text;
    private String file_name;
    private String file_path;

    private User user;

    public SubmissionListItemDto toSubmissionListItemDto() {
        SubmissionListItemDto dto = new SubmissionListItemDto();
        dto.setSubmissionId(this.submissionId);
        dto.setUserId(this.userId);
        dto.setCreationDate(this.creation_date);
        dto.setText(this.text);
        dto.setFileName(this.file_name);
        dto.setUsername(this.user.getUsername());
        return dto;
    }
}
