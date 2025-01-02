package com.tongji.chaigrouping.userservice.controller;

import com.tongji.chaigrouping.commonutils.dto.AccountInfoDto;
import com.tongji.chaigrouping.commonutils.dto.ResumeDto;
import com.tongji.chaigrouping.userservice.exception.InvalidAccountInfoException;
import com.tongji.chaigrouping.userservice.service.AccountService;
import com.tongji.chaigrouping.userservice.service.ResumeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ResumeService resumeService;

    @GetMapping("/profile")
    public ResponseEntity<Object> getProfile(@RequestHeader("X-User-id") Integer userId) {
        AccountInfoDto userDto = accountService.getAccountInfo(userId);
        Map<String, String> response = new HashMap<>();
        response.put("username", userDto.getUsername());
        response.put("profile", userDto.getProfile());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/profile")
    public ResponseEntity<Object> updateProfile(@RequestHeader("X-User-id") Integer userId, @RequestBody AccountInfoDto updateProfileDto) {
        try {
            accountService.updateAccountInfo(userId, updateProfileDto);
            return ResponseEntity.ok(Map.of("message", "账户信息更新成功"));
        } catch (InvalidAccountInfoException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/resume")
    public ResponseEntity<Object> getResume(@RequestHeader("X-User-id") Integer userId) {
        ResumeDto resumeDto = resumeService.getResume(userId);
        return ResponseEntity.ok(resumeDto);
    }

    @PutMapping("/resume")
    public ResponseEntity<Object> updateResume(@RequestHeader("X-User-id") Integer userId, @RequestBody ResumeDto resumeDto) {
        resumeService.updateResume(userId, resumeDto);
        return ResponseEntity.ok(Map.of("message", "简历更新成功"));
    }
}
