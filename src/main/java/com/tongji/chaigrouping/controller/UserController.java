package com.tongji.chaigrouping.controller;

import com.tongji.chaigrouping.dto.AccountInfoDto;
import com.tongji.chaigrouping.dto.ResumeDto;
import com.tongji.chaigrouping.exception.InvalidAccountInfoException;
import com.tongji.chaigrouping.service.AccountService;
import com.tongji.chaigrouping.service.ResumeService;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<Object> getProfile(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("X-User-id");
        AccountInfoDto userDto = accountService.getAccountInfo(userId);
        Map<String, String> response = new HashMap<>();
        response.put("username", userDto.getUsername());
        response.put("profile", userDto.getProfile());
        return ResponseEntity.ok(response);
    }

    @PutMapping("/profile")
    public ResponseEntity<Object> updateProfile(HttpServletRequest request, @RequestBody AccountInfoDto updateProfileDto) {
        Integer userId = (Integer) request.getAttribute("X-User-id");
        try {
            accountService.updateAccountInfo(userId, updateProfileDto);
            return ResponseEntity.ok(Map.of("message", "账户信息更新成功"));
        } catch (InvalidAccountInfoException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @GetMapping("/resume")
    public ResponseEntity<Object> getResume(HttpServletRequest request) {
        Integer userId = (Integer) request.getAttribute("X-User-id");
        ResumeDto resumeDto = resumeService.getResume(userId);
        return ResponseEntity.ok(resumeDto);
    }

    @PutMapping("/resume")
    public ResponseEntity<Object> updateResume(HttpServletRequest request, @RequestBody ResumeDto resumeDto) {
        Integer userId = (Integer) request.getAttribute("X-User-id");
        resumeService.updateResume(userId, resumeDto);
        return ResponseEntity.ok(Map.of("message", "简历更新成功"));
    }
}