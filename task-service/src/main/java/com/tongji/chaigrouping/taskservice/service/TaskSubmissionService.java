package com.tongji.chaigrouping.taskservice.service;

import java.io.File;

public interface TaskSubmissionService {
    void submitTask(String text, File file);

    void updateTaskSubmission(Integer submissionId, String text, File file);

    void deleteTaskSubmission(Integer submissionId);

}
