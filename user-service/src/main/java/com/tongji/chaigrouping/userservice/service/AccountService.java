package com.tongji.chaigrouping.userservice.service;

import com.tongji.chaigrouping.commonutils.dto.AccountInfoDto;
import com.tongji.chaigrouping.userservice.exception.InvalidAccountInfoException;

public interface AccountService {
    AccountInfoDto getAccountInfo(Integer userId);

    void updateAccountInfo(Integer userId, AccountInfoDto accountInfoDto) throws InvalidAccountInfoException;

}
