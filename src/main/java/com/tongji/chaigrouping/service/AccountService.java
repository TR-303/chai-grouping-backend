package com.tongji.chaigrouping.service;

import com.tongji.chaigrouping.dto.AccountInfoDto;
import com.tongji.chaigrouping.exception.InvalidAccountInfoException;

public interface AccountService {
    AccountInfoDto getAccountInfo(Integer userId);

    void updateAccountInfo(Integer userId, AccountInfoDto accountInfoDto) throws InvalidAccountInfoException;

}