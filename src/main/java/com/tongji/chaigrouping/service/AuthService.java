package com.tongji.chaigrouping.service;

import com.tongji.chaigrouping.dto.LoginResultDto;
import com.tongji.chaigrouping.exception.InvalidRegisterException;

public interface AuthService {
    void register(String username, String password) throws InvalidRegisterException;

     LoginResultDto login(String username, String password) throws InvalidRegisterException;
}