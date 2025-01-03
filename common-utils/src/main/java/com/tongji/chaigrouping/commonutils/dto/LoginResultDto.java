package com.tongji.chaigrouping.commonutils.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResultDto {
    public String message;
    public Integer userId;
    public String token;
}
