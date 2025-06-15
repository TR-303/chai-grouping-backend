package com.tongji.chaigrouping.controller;

import com.tongji.chaigrouping.dto.LoginDto;
import com.tongji.chaigrouping.exception.InvalidLoginException;
import com.tongji.chaigrouping.exception.InvalidRegisterException;
import com.tongji.chaigrouping.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Operation(summary = "用户注册", security = @SecurityRequirement(name = "none"))
    @PostMapping("/register")
    public ResponseEntity<Object> register(@RequestBody LoginDto loginDto) {
        try {
            authService.register(loginDto.getUsername(), loginDto.getPassword());
            return ResponseEntity.ok(Map.of("message", "Register successfully"));
        } catch (InvalidRegisterException e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    @Operation(summary = "用户登录", security = @SecurityRequirement(name = "none"))
    @PostMapping("/login")
    public ResponseEntity<Object> login(@RequestBody LoginDto loginDto) {
        try {
            return ResponseEntity.ok(authService.login(loginDto.getUsername(), loginDto.getPassword()));
        } catch (InvalidLoginException e) {
            return ResponseEntity.status(401).body(Map.of("message", e.getMessage()));
        }
    }
}
