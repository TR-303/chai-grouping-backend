package com.tongji.chaigrouping.userservice.service;

import com.tongji.chaigrouping.userservice.exception.InvalidRegisterException;

public interface AuthService {
    void register(String username, String password) throws InvalidRegisterException;

    String login(String username, String password) throws InvalidRegisterException;
}
