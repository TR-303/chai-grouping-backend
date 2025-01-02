package com.tongji.chaigrouping.taskservice.controller;

import com.tongji.chaigrouping.commonutils.dto.SubmissionCreationDto;
import com.tongji.chaigrouping.taskservice.service.TaskSubmissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/submissions")
public class SubmissionsController {
    @Autowired
    private TaskSubmissionService taskSubmissionService;

    @PostMapping(value = "/submit/{task_id}", consumes = "multipart/form-data")
    public ResponseEntity<Object> submitTask(@RequestHeader("X-User-id") Integer userId, @PathVariable("task_id") Integer taskId, @ModelAttribute SubmissionCreationDto submissionCreationDto) {
        taskSubmissionService.submitTask(userId, taskId, submissionCreationDto.getText(), submissionCreationDto.getFile());
        return ResponseEntity.ok(Map.of("message", "Task submitted successfully"));
    }

    @PostMapping(value = "/{submission_id}/update", consumes = "multipart/form-data")
    public ResponseEntity<Object> updateTaskSubmission(@PathVariable("submission_id") Integer submissionId, @RequestBody SubmissionCreationDto submissionCreationDto) {
        taskSubmissionService.updateTaskSubmission(submissionId, submissionCreationDto.getText(), submissionCreationDto.getFile());
        return ResponseEntity.ok(Map.of("message", "Task submission updated successfully"));
    }

    @DeleteMapping("/{submission_id}/delete")
    public ResponseEntity<Object> deleteTaskSubmission(@PathVariable("submission_id") Integer submissionId) {
        taskSubmissionService.deleteTaskSubmission(submissionId);
        return ResponseEntity.ok(Map.of("message", "Task submission deleted successfully"));
    }

    @GetMapping("/{submission_id}/download")
    public ResponseEntity<Resource> downloadFile(@PathVariable Integer submission_id) {
        try {
            return taskSubmissionService.downloadFile(submission_id);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

}
