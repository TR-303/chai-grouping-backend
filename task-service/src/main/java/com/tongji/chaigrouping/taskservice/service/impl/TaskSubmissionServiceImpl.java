package com.tongji.chaigrouping.taskservice.service.impl;

import com.tongji.chaigrouping.commonutils.entity.Submission;
import com.tongji.chaigrouping.commonutils.mapper.SubmissionMapper;
import com.tongji.chaigrouping.taskservice.service.TaskSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;
import org.springframework.core.io.UrlResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class TaskSubmissionServiceImpl implements TaskSubmissionService {
    @Autowired
    private SubmissionMapper submissionMapper;

    private static final String uploadDir = "C:/database/uploads";

    @Override
    public void submitTask(Integer userId, Integer taskId, String text, MultipartFile file) {
        try {
            Submission submission = new Submission();
            if (file != null && !file.isEmpty()) {
                saveFile(file, submission);
            }
            submission.setUserId(userId);
            submission.setTaskId(taskId);
            submission.setText(text);
            submissionMapper.insert(submission);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save file");
        }
    }

    @Override
    public void updateTaskSubmission(Integer submissionId, String text, MultipartFile file) {
        try {
            Submission submission = submissionMapper.selectById(submissionId);
            if (file != null && !file.isEmpty()) {
                // Delete the old file if it exists
                String oldFilePath = submission.getFilePath();
                if (oldFilePath != null) {
                    File oldFile = new File(uploadDir, oldFilePath);
                    if (oldFile.exists()) {
                        oldFile.delete();
                    }
                }

                saveFile(file, submission);
            }
            submission.setText(text);
            submissionMapper.updateById(submission);
        } catch (Exception e) {
            throw new RuntimeException("Failed to save file");
        }
    }

    private void saveFile(MultipartFile file, Submission submission) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String uniqueFilename = System.currentTimeMillis() + "_" + originalFilename;
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        File destinationFile = new File(uploadPath.toFile(), uniqueFilename);
        file.transferTo(destinationFile);

        submission.setFileName(originalFilename);
        submission.setFilePath(uniqueFilename);
    }


    @Override
    public void deleteTaskSubmission(Integer submissionId) {
        submissionMapper.deleteById(submissionId);
    }

    @Override
    public ResponseEntity<Resource> downloadFile(Integer submissionId) {
        try {
            Submission submission = submissionMapper.selectById(submissionId);
            String filename = submission.getFilePath();

            Path filePath = Paths.get(uploadDir).resolve(filename).normalize();

            Resource resource = new UrlResource(filePath.toUri());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            throw new RuntimeException("文件下载失败", e);
        }
    }
}
