package com.tongji.chaigrouping.userservice.service;

import com.tongji.chaigrouping.commonutils.dto.LoginResultDto;
import com.tongji.chaigrouping.userservice.exception.InvalidRegisterException;

public interface AuthService {
    void register(String username, String password) throws InvalidRegisterException;

     LoginResultDto login(String username, String password) throws InvalidRegisterException;
}
