package com.tongji.chaigrouping.commonutils.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class RespondToRequestDto {
    String action; // APPROVE, REJECT
}
