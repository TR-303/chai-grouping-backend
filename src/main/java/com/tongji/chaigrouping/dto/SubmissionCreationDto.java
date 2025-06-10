package com.tongji.chaigrouping.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class SubmissionCreationDto {
    private String text;
    private MultipartFile file;
}
