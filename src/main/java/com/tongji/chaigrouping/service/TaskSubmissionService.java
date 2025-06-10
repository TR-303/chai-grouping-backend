package com.tongji.chaigrouping.service;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface TaskSubmissionService {
    void submitTask(Integer userId, Integer taskId, String text, MultipartFile file);

    void updateTaskSubmission(Integer submissionId, String text, MultipartFile file);

    void deleteTaskSubmission(Integer submissionId);

    ResponseEntity<Resource> downloadFile(Integer submissionId);
}